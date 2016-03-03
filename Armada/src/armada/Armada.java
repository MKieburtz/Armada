package armada;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.SwingUtilities;

/**
 * @author Michael Kieburtz
 */
public class Armada implements GameActionListener
{
    private final ArmadaWindow window;
    private final Resources resources;
    private final ScheduledExecutorService drawingTimer;
    private final SelectionRect selectionRect;
    
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<Ship> selectedShips = new ArrayList<>();
    
    private GameState state = GameState.opening;
    
    public Armada()
    {
        drawingTimer = Executors.newSingleThreadScheduledExecutor();
        
        resources = new Resources();
        GameData.initResources(resources);
        window = new ArmadaWindow(this);
        selectionRect = new SelectionRect();
        DrawingData.setSelectionRect(selectionRect);
        addShips();
        DrawingData.setShips(ships);
        state = GameState.opening;
        drawingTimer.schedule(new UpdateAndDrawingService(), 0, TimeUnit.MILLISECONDS);
    }
    
    private void addShips()
    {
        ships.add(new Ship(new Point2D.Double(100, 100)));
        ships.add(new Ship(new Point2D.Double(200, 100)));
        ships.add(new Ship(new Point2D.Double(100, 200)));
        ships.add(new Ship(new Point2D.Double(200, 200)));
    }
    
    @Override
    public void doneOpening() 
    {
        state = GameState.mainMenu;
    }
    
    @Override
    public void startButtonPressed()
    {
        state = GameState.playing;
    }
    
    @Override
    public void mousePressed(MouseEvent e) 
    {
        switch (state)
        {
            case mainMenu:
                break;
            case playing:
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        DrawingData.setSelectionRect(selectionRect);
                        selectionRect.activateRect(Calculator.compensateForBorders(e.getPoint()));
                        for (Ship s : ships)
                        {
                            if (s.checkMousePressed(Calculator.compensateForBorders(e.getPoint())))
                            {
                                if (!selectedShips.contains(s))
                                {
                                    selectedShips.add(s);
                                }
                            }
                            else
                            {
                                if (selectedShips.contains(s))
                                {
                                    selectedShips.remove(s);
                                }
                            }
                        }
                    }
                    else if (SwingUtilities.isRightMouseButton(e))
                    {
                        for (Ship s : selectedShips)
                        {
                            s.move(new MovementCommand(Calculator.compensateForBorders(e.getPoint())));
                        }
                    }
                break;
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) 
    {
        switch (state)
        {
            case mainMenu:
                    window.checkMouseMovedForMainMenu(e);
                break;
            case playing:
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) 
    {
        switch (state)
        {
            case mainMenu:
                break;
            case playing:
                selectionRect.updateRect(Calculator.compensateForBorders(e.getPoint()));
                DrawingData.setSelectionRect(selectionRect);

                for (Ship s : ships)
                {
                    if (selectionRect.checkForIntersection(s.getBoundingRect()))
                    {
                        if (!selectedShips.contains(s))
                        {
                            s.select();
                            selectedShips.add(s);
                        }
                    }
                    else
                    {
                        s.deSelect();
                        if (selectedShips.contains(s))
                        {
                            selectedShips.remove(s);
                        }
                    }
                }
            break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
        switch (state)
        {
            case mainMenu:
                    window.CheckMousePressedForMainMenu(e);
                break;
            case playing:
                    selectionRect.deactivateRect();
                    DrawingData.setSelectionRect(selectionRect);
                break;
        }
    }
    
    class UpdateAndDrawingService implements Runnable
    {
        @Override
        public void run() 
        {
            window.draw(state);
            drawingTimer.schedule(new UpdateAndDrawingService(), 10, TimeUnit.MILLISECONDS);
        }
    }
    
    public static void main(String[] args) 
    {
        new Armada();
    }
}

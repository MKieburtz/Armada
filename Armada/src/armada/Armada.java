package armada;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.*;

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
        ships.add(new Ship());
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
                    window.CheckMousePressedForMainMenu(e);
                break;
            case playing:
                    ships.get(0).checkMousePressed(window.compensateForBorders(e.getPoint()));
                    selectionRect.activateRect(window.compensateForBorders(e.getPoint()));
                    DrawingData.setSelectionRect(selectionRect);
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
        if (state == GameState.playing)
        {
            selectionRect.updateRect(window.compensateForBorders(e.getPoint()));
            DrawingData.setSelectionRect(selectionRect);
            
            for (Ship s : ships)
            {
                if (selectionRect.checkForIntersection(s.getBoundingRect()))
                {
                    s.select();
                }
                else
                {
                    s.deSelect();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
        selectionRect.deactivateRect();
        DrawingData.setSelectionRect(selectionRect);
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
        Vector v = new Vector(new Point2D.Double(3, -4));
        System.out.println(Calculator.normalizeAngle(v.getDirectionAndMagnitude().x));
        new Armada();
    }
}

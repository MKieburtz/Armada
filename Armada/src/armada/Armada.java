package armada;

import java.awt.BufferCapabilities;
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
    private final ScheduledExecutorService updatingTimer;
    private final ScheduledExecutorService fpsRecorder;
    private final SelectionRect selectionRect;
    
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<Ship> selectedShips = new ArrayList<>();
    
    private GameState state = GameState.opening;
    
    private int framesDrawn = 0;
    private int FPS = 0;
    private int updates = 0;
    private int UPS = 0;
    
    public Armada()
    {
        drawingTimer = Executors.newScheduledThreadPool(1);
        updatingTimer = Executors.newScheduledThreadPool(1);
        fpsRecorder = Executors.newSingleThreadScheduledExecutor();
        resources = new Resources();
        GameData.initResources(resources);
        window = new ArmadaWindow(this);
        selectionRect = new SelectionRect();
        DrawingData.setSelectionRect(selectionRect);
        DrawingData.setFps(0);
        addShips();
        DrawingData.setShips(ships);
        state = GameState.opening;
        drawingTimer.schedule(new DrawingService(), 0, TimeUnit.MILLISECONDS);
        updatingTimer.schedule(new UpdateService(), 0, TimeUnit.MILLISECONDS);
        fpsRecorder.schedule(new RecorderService(), 0, TimeUnit.MILLISECONDS);
    }
    
    private void addShips()
    {
        ships.add(new Ship(new Point2D.Double(100, 100)));
//        ships.add(new Ship(new Point2D.Double(200, 100)));
//        ships.add(new Ship(new Point2D.Double(100, 200)));
//        ships.add(new Ship(new Point2D.Double(200, 200)));
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
    {        switch (state)
        {
            case mainMenu:
                break;
            case playing:
                    if (e.getButton() == 1)
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
                    else if (e.getButton() == 3)
                    {   
                        if (selectionRect.isActive())
                        {
                            selectionRect.deactivateRect();
                        }
                        else
                        {
                            for (Ship s : selectedShips)
                            {
                                s.move(new MovementCommand(Calculator.compensateForBorders(e.getPoint())));
                            }
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
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    if (selectionRect.isActive())
                    {
                        selectionRect.updateRect(Calculator.compensateForBorders(e.getPoint()));
                    }
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
    
    class UpdateService implements Runnable
    {
        @Override
        public void run() 
        {
            if (state == GameState.playing)
            {
                for (Ship s : ships)
                {
                    s.update();
                }
            }
            updates++;
            drawingTimer.schedule(new UpdateService(), 10, TimeUnit.MILLISECONDS);
        }
    }
    
    class DrawingService implements Runnable
    {
        @Override
        public void run() 
        {
            SwingUtilities.invokeLater(new Runnable() 
            {
                @Override
                public void run() 
                {
                    window.draw(state);
                    framesDrawn++;
                    drawingTimer.schedule(new DrawingService(), 1, TimeUnit.MILLISECONDS);
                }
            });
        }
    }
    
    class RecorderService implements Runnable
    {
        @Override
        public void run()
        {
            FPS = framesDrawn;
            framesDrawn = 0;
            UPS = updates;
            updates = 0;
            DrawingData.setFps(FPS);
            DrawingData.setUPS(UPS);
            fpsRecorder.schedule(new RecorderService(), 1, TimeUnit.SECONDS);
        }
    }
    
    public static void main(String[] args) 
    {
        Thread.setDefaultUncaughtExceptionHandler(new EDTExceptionHandler());
        System.setProperty("sun.awt.exception.handler", EDTExceptionHandler.class.getName());
        
        new Armada();
    }
}

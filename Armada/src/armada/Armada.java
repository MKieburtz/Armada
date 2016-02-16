package armada;

import java.awt.event.MouseEvent;
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
    
    private ArrayList<Ship> ships = new ArrayList<>();
    
    private GameState state;
    
    public Armada()
    {
        drawingTimer = Executors.newSingleThreadScheduledExecutor();
        
        resources = new Resources();
        GameData.initResources(resources);
        window = new ArmadaWindow(this);
        
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
                    ships.get(0).checkMousePressed(e.getPoint());
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

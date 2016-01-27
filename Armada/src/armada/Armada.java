package armada;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Michael Kieburtz
 */
public class Armada
{
    private final ArmadaWindow window;
    private final Resources resources;
    private final ScheduledExecutorService drawingTimer;
    
    private ArrayList<Ship> ships = new ArrayList<>();
            
    public Armada()
    {
        drawingTimer = Executors.newSingleThreadScheduledExecutor();
        
        resources = new Resources();
        GameData.initResources(resources);
        window = new ArmadaWindow();
        addShips();
        DrawingData.setShips(ships);
        drawingTimer.schedule(new UpdateAndDrawingService(), 0, TimeUnit.MILLISECONDS);
    }
    
    private void addShips()
    {
        ships.add(new Ship());
    }
    
    class UpdateAndDrawingService implements Runnable
    {
        @Override
        public void run() 
        {
            window.draw();
            drawingTimer.schedule(new UpdateAndDrawingService(), 10, TimeUnit.MILLISECONDS);
        }
    }
    
    public static void main(String[] args) 
    {
        new Armada();
    }
}

package armada;

import java.util.concurrent.*;

/**
 * @author Michael Kieburtz
 */
public class Armada
{
    private ArmadaWindow window;
    private Resources resources;
    private ScheduledExecutorService drawingTimer;
            
    public Armada()
    {
        drawingTimer = Executors.newSingleThreadScheduledExecutor();
        
        resources = new Resources();
        GameData.initResources(resources);
        window = new ArmadaWindow();
        
        drawingTimer.schedule(new UpdateAndDrawingService(), 500, TimeUnit.MILLISECONDS);
    }
    
    class UpdateAndDrawingService implements Runnable
    {
        @Override
        public void run() 
        {
            
            drawingTimer.schedule(new UpdateAndDrawingService(), 500, TimeUnit.MILLISECONDS);
        }
    }
    
    public static void main(String[] args) 
    {
        new Armada();
    }
}

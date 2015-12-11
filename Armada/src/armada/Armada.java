package armada;

import java.util.concurrent.*;

/**
 * @author Michael Kieburtz
 */
public class Armada
{
    private final ArmadaWindow window;
    private final Resources resources;
    private final ScheduledExecutorService drawingTimer;
            
    public Armada()
    {
        drawingTimer = Executors.newSingleThreadScheduledExecutor();
        
        resources = new Resources();
        GameData.initResources(resources);
        window = new ArmadaWindow();
        
        drawingTimer.schedule(new UpdateAndDrawingService(), 0, TimeUnit.MILLISECONDS);
    }
    
    class UpdateAndDrawingService implements Runnable
    {
        @Override
        public void run() 
        {
            window.draw();
            drawingTimer.schedule(new UpdateAndDrawingService(), 17, TimeUnit.MILLISECONDS);
        }
    }
    
    public static void main(String[] args) 
    {
        new Armada();
    }
}

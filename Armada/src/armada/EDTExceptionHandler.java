package armada;

/**
 * @author Michael Kieburtz
 */
public class EDTExceptionHandler implements Thread.UncaughtExceptionHandler
{

    @Override
    public void uncaughtException(Thread thread, Throwable thrwbl) 
    {
        handleException(thread.getName(), thrwbl);
    }
    
    protected void handleException(String tname, Throwable thrown)
    {
        System.err.println("Exception on " + tname);
        thrown.printStackTrace();
    }
}

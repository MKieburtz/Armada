package armada;

/**
 * @author Michael Kieburtz
 */
public class GameData 
{
    public static Resources resources;
    
    public static void initResources(Resources r)
    {
        resources = r;
    }
    public static Resources getResources()
    {
        return resources;
    }
}

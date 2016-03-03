package armada;

import java.awt.Dimension;

/**
 * @author Michael Kieburtz
 */
public class GameData 
{
    private static Resources resources;
    
    public static void initResources(Resources r)
    {
        resources = r;
    }
    public static Resources getResources()
    {
        return resources;
    }
    //////////////////////////////////////////////////////////////////
    private static Dimension borderSize;
    
    public static void setBorderSize(Dimension b)
    {
        borderSize = b;
    }
    
    public static Dimension getBorderSize()
    {
        return borderSize;
    }
}

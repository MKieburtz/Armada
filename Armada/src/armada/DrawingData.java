package armada;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Michael Kieburtz
 */
public class DrawingData 
{
    public static volatile ArrayList<Ship> ships = new ArrayList<>();
    
    public static void setShips(ArrayList<Ship> newShips) 
    {
        ships = newShips;
    }
    
    public static ArrayList<Ship> getShips()
    {
        return ships;
    }
    
    public static volatile BufferedImage sideBorder;
    
    public static void setSideBorder(BufferedImage newSideBorder) 
    {
        sideBorder = newSideBorder;
    }
    
    public static BufferedImage getSideBorder()
    {
        return sideBorder;
    }
    
    public static volatile BufferedImage topBorder;

    public static void setTopBorder(BufferedImage newTopBorder) 
    {
        topBorder = newTopBorder;
    }   
    
    public static BufferedImage getTopBorder()
    {
        return topBorder;
    }
    
    public static volatile Dimension screenSize;

    public static Dimension getScreenSize() 
    {
        return screenSize;
    }

    public static void setScreenSize(Dimension screenSize) 
    {
        DrawingData.screenSize = screenSize;
    }
    
}

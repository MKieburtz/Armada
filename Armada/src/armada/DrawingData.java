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
    ///////////////////////////////////////////////////////////////////
    public static volatile BufferedImage verticalBorder;
    
    public static void setVerticalBorder(BufferedImage newVerticalBorder) 
    {
        verticalBorder = newVerticalBorder;
    }
    
    public static BufferedImage getVerticalBorder()
    {
        return verticalBorder;
    }
    ///////////////////////////////////////////////////////////////////
    public static volatile BufferedImage initBorder;

    public static BufferedImage getInitBorder() 
    {
        return initBorder;
    }

    public static void setInitBorder(BufferedImage initBorder) 
    {
        DrawingData.initBorder = initBorder;
    }
    ///////////////////////////////////////////////////////////////////
    public static volatile BufferedImage horizontalBorder;

    public static void setHorizontalBorder(BufferedImage newHorizontalBorder) 
    {
        horizontalBorder = newHorizontalBorder;
    }   
    
    public static BufferedImage getHorizontalBorder()
    {
        return horizontalBorder;
    }
    ///////////////////////////////////////////////////////////////////
    public static volatile Dimension screenSize;

    public static Dimension getScreenSize() 
    {
        return screenSize;
    }

    public static void setScreenSize(Dimension screenSize) 
    {
        DrawingData.screenSize = screenSize;
    }
    ///////////////////////////////////////////////////////////////////
    
}

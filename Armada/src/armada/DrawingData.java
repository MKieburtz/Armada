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
    
    public static ArrayList<Ship> getShips()
    {
        return ships;
    }
    
    public static void setShips(ArrayList<Ship> newShips) 
    {
        ships = newShips;
    }
    ///////////////////////////////////////////////////////////////////
    public static volatile BufferedImage verticalBorder;
    
    public static BufferedImage getVerticalBorder()
    {
        return verticalBorder;
    }
    
    public static void setVerticalBorder(BufferedImage newVerticalBorder) 
    {
        verticalBorder = newVerticalBorder;
    }
    ///////////////////////////////////////////////////////////////////
    public static volatile BufferedImage initBorder;

    public static BufferedImage getInitBorder() 
    {
        return initBorder;
    }

    public static void setInitBorder(BufferedImage newInitBorder) 
    {
        initBorder = newInitBorder;
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

    public static void setScreenSize(Dimension newScreenSize) 
    {
        screenSize = newScreenSize;
    }
    ///////////////////////////////////////////////////////////////////
    public static BufferedImage[] horizontalBorderFrames;

    public static BufferedImage[] getHorizontalBorderFrames() 
    {
        return horizontalBorderFrames;
    }

    public static void setHorizontalBorderFrames(BufferedImage[] newHorizontalBorderFrames) 
    {
        horizontalBorderFrames = newHorizontalBorderFrames;
    }
    ///////////////////////////////////////////////////////////////////
    public static BufferedImage[] initBorderFrames;

    public static BufferedImage[] getInitBorderFrames() 
    {
        return initBorderFrames;
    }

    public static void setInitBorderFrames(BufferedImage[] newInitBorderFrames) 
    {
        initBorderFrames =  newInitBorderFrames;
    }
}

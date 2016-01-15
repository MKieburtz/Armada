package armada;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Michael Kieburtz
 */
public class DrawingData 
{
    ArrayList<Ship> ships = new ArrayList<>();
    BufferedImage sideBorder;
    BufferedImage topBorder;

    public void setShips(ArrayList<Ship> ships) 
    {
        this.ships = ships;
    }

    public void setSideBorder(BufferedImage sideBorder) 
    {
        this.sideBorder = sideBorder;
    }

    public void setTopBorder(BufferedImage topBorder) 
    {
        this.topBorder = topBorder;
    }   
}

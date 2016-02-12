package armada;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Michael Kieburtz
 */
public abstract class GameEntity 
{
    protected final ArrayList<String> imagePaths = new ArrayList<>();
    protected final ArrayList<BufferedImage> images = new ArrayList<>();
    protected Point2D.Double location;
    
    public abstract void draw(Graphics2D g2d);
}

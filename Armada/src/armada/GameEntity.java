package armada;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Michael Kieburtz
 */
public abstract class GameEntity 
{
    protected final ArrayList<String> imagePaths = new ArrayList<>();
    protected final ArrayList<BufferedImage> images = new ArrayList<>();
    
    public abstract void draw(Graphics2D g2d);
}

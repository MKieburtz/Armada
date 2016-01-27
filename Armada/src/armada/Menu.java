package armada;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Michael Kieburtz
 */
public class Menu 
{
    private final ArrayList<BufferedImage> images = new ArrayList<>();
    private final ArrayList<String> imagePaths = new ArrayList<>();
    private final HashMap<Buttons, Rectangle2D.Double> buttonRects = new HashMap<>();
    
    
    enum Buttons
    {
        start
    }
    
    public Menu()
    {
        imagePaths.add("Resources/StartButtonNoHover.png");
        imagePaths.add("Resources/StartButtonHover.png");
        
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
    }
    
}

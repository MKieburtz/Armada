package armada;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Michael Kieburtz
 */
public class MainMenu 
{
    private final ArrayList<BufferedImage> images = new ArrayList<>();
    private final ArrayList<String> imagePaths = new ArrayList<>();
    private final HashMap<Buttons, Rectangle2D.Double> buttonRects = new HashMap<>();
    private final GameActionListener gameActionListener;
    
    private final int START_BUTTON_NO_HOVER = 0;
    private final int START_BUTTON_HOVER = 1;
    
    enum Buttons
    {
        start
    }
    
    public MainMenu(GameActionListener actionListener)
    {
        this.gameActionListener = actionListener;
        imagePaths.add("Resources/StartButtonNoHover.png");
        imagePaths.add("Resources/StartButtonHover.png");
        
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
    }
    
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(images.get(START_BUTTON_NO_HOVER), 500, 500, null);
    }
    
}

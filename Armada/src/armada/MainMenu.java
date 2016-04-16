package armada;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * @author Michael Kieburtz
 */
public class MainMenu 
{
    private final ArrayList<BufferedImage> images = new ArrayList<>();
    private final ArrayList<String> imagePaths = new ArrayList<>();
    private final HashMap<Buttons, Rectangle2D.Double> buttonRects = new HashMap<>();
    private final HashMap<Buttons, Boolean> buttonHovers = new HashMap<>();
    private final GameActionListener gameActionListener;
    
    private final int START_BUTTON_NO_HOVER = 0;
    private final int START_BUTTON_HOVER = 1;
    private final Dimension StartButtonDimensions;
    
    enum Buttons
    {
        START
    }
    
    public MainMenu(GameActionListener actionListener)
    {
        this.gameActionListener = actionListener;
        imagePaths.add("Resources/StartButton.png");
        imagePaths.add("Resources/StartButtonHover.png");
        
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
        
        StartButtonDimensions = new Dimension(images.get(0).getWidth(), images.get(1).getHeight());
    }
    
    public void setButtonRects()
    {
        buttonRects.put(Buttons.START, new Rectangle2D.Double(DrawingData.getScreenSize().width / 2 - StartButtonDimensions.width / 2,
                DrawingData.getScreenSize().height / 2 - StartButtonDimensions.height / 2,
                StartButtonDimensions.width,
                StartButtonDimensions.height));
        buttonHovers.put(Buttons.START, false);
    }
    
    private final float backgroundOpacity = .1f;
    public void draw(Graphics2D g2d)
    {
        AffineTransform originalTransform = g2d.getTransform();
        AffineTransform transform = (AffineTransform)originalTransform.clone();
        
        Composite originalComposite = g2d.getComposite();
        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backgroundOpacity);
        
        g2d.setComposite(comp);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, DrawingData.getScreenSize().width, DrawingData.getScreenSize().height);
        g2d.setComposite(originalComposite);
        
        transform.translate(DrawingData.getScreenSize().width / 2 - StartButtonDimensions.width / 2,
                DrawingData.getScreenSize().height / 2 - StartButtonDimensions.height / 2);
        
        g2d.setTransform(transform);
        
        g2d.drawImage(images.get(buttonHovers.get(Buttons.START) ? START_BUTTON_HOVER : START_BUTTON_NO_HOVER), 0, 0, null);
        
        g2d.setTransform(originalTransform);
        //g2d.draw(buttonRects.get(Buttons.start));
    }
    
    public void checkMousePressed(Point location)
    {
        if (buttonRects.get(Buttons.START).contains(location))
        {
            gameActionListener.startButtonPressed();
        }
    }
    
    public void checkMouseMoved(Point location)
    {
        if (buttonRects.get(Buttons.START).contains(location))
        {
            buttonHovers.replace(Buttons.START, true);
        }
        else
        {
            buttonHovers.replace(Buttons.START, false);
        }
    }
}

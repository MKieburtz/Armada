package armada;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.util.*;
import javax.sound.sampled.*;

/**
 * @author Michael Kieburtz
 */
public class Resources 
{
    private final MediaLoader loader = new MediaLoader();

    private final ArrayList<String> imagePaths = new ArrayList<>();
    private final LinkedHashMap<String, BufferedImage> images = new LinkedHashMap<>();    
    private final HashMap<GeneratedImagesType, BufferedImage[]> generatedImages = new HashMap<>();
    
    private final ArrayList<String> soundPaths = new ArrayList<>();
    private final HashMap<String, Clip> sounds = new HashMap<>();

    private final ArrayList<FontInfo> fontData = new ArrayList<>();
    private final HashMap<FontInfo, Font> fonts = new HashMap<>();
    
    private final int TOP_BORDER_INDEX = 1;
    private final int TOP_BORDER_HEIGHT = 30;
    private final int TOP_BORDER_WIDTH = 2000;
    
    private final int INIT_BORDER_INDEX = 2;
    private final int INIT_BORDER_HEIGHT = 2000;
    private final int INIT_BORDER_WIDTH = 15;
    
    public Resources()
    {
        // for HUD
        imagePaths.add("Resources/SideBorder.png");
        imagePaths.add("Resources/TopBorder.png");
        imagePaths.add("Resources/InitBorder.png");
        imagePaths.add("Resources/InitVerticalBorder.png");
        imagePaths.add("Resources/Ship.png");
        imagePaths.add("Resources/SelectedShip.png");
        imagePaths.add("Resources/StartButtonHover.png");
        imagePaths.add("Resources/StartButton.png");
        
        // load the images
        loadAllImages();
        
        generateImages();
    }
    
    public static enum GeneratedImagesType
    {
        ANIMATED_INIT_BORDER,
        ANIMATED_HORIZONTAL_BORDER
    }
    
    private void generateImages()
    {
        // init border first
        BufferedImage[] initImages = new BufferedImage[80];
        
        for (int i = 0; i < initImages.length; i++)
        {
            initImages[i] = new BufferedImage(20, 2000, BufferedImage.TYPE_INT_ARGB);
        }
        
        for (int i = 0; i < 80; i++)
        {
            Rectangle2D.Double totalArea = new Rectangle2D.Double(0, 0, INIT_BORDER_WIDTH, INIT_BORDER_HEIGHT);
            Rectangle2D.Double clipping;
            
            clipping = new Rectangle2D.Double(0, 0, INIT_BORDER_WIDTH, INIT_BORDER_HEIGHT - i * 25);
            
            Area clippingArea = new Area(totalArea);
            clippingArea.subtract(new Area(clipping));
            
            Graphics2D g2d = initImages[i].createGraphics();
            
            g2d.clip(clippingArea);
            g2d.drawImage(new ArrayList<>(images.values()).get(INIT_BORDER_INDEX), 0, 0, null);
            g2d.dispose();
        }
        // add the images
        generatedImages.put(GeneratedImagesType.ANIMATED_INIT_BORDER, initImages);
        
        // then generate the top borders
        BufferedImage[] horizontalBorders = new BufferedImage[79];

        for (int i = 0; i < horizontalBorders.length; i++)
        {
            horizontalBorders[i] = new BufferedImage(2000, 20, BufferedImage.TYPE_INT_ARGB);
        }
                
        for (int i = 0; i < horizontalBorders.length; i++)
        {
            // ensure that the first image has width, that the entire thing isn't cut
            boolean isFirstImage = i == 0;
            Rectangle2D.Double totalArea = new Rectangle2D.Double(0, 0, TOP_BORDER_WIDTH, TOP_BORDER_HEIGHT);
            Rectangle2D.Double clipping = new Rectangle2D.Double(0, 0, TOP_BORDER_WIDTH - (isFirstImage ? 25 : (i+1) * 25), TOP_BORDER_HEIGHT);
            
            Area clippingArea = new Area(totalArea);
            clippingArea.subtract(new Area(clipping));
            
            Graphics2D g2d = horizontalBorders[i].createGraphics();
            
            g2d.clip(clippingArea);
            g2d.drawImage(new ArrayList<>(images.values()).get(TOP_BORDER_INDEX), 0, 0, null);
            g2d.dispose();
        }
        
        // add the images
        generatedImages.put(GeneratedImagesType.ANIMATED_HORIZONTAL_BORDER, horizontalBorders);
    }
    
    public BufferedImage[] getGeneratedImagesForObject(GeneratedImagesType type)
    {
        return generatedImages.get(type);
    }
            
            
    public ArrayList<BufferedImage> getImagesForObject(ArrayList<String> paths) 
    {
        ArrayList<BufferedImage> imagesToReturn = new ArrayList<>();
        
        for (String s : paths)
        {
            imagesToReturn.add(images.get(s));
        }
        
        return imagesToReturn;
    }
    
    public BufferedImage getImageForObject(String path)
    {
        return images.get(path);
    }
    
    public ArrayList<Clip> getSoundsForObject(ArrayList<String> paths)
    {
        ArrayList<Clip> soundsToReturn = new ArrayList<>();
        
        for (String s : paths)
        {
            soundsToReturn.add(sounds.get(s));
        }
        
        return soundsToReturn;
    }
    
    public Clip getSoundForObject(String path)
    {
        return sounds.get(path);
    }

    public ArrayList<Font> getFontsForObject(ArrayList<FontInfo> fontInfos)
    {
        ArrayList<Font> fontsToReturn = new ArrayList<>();
        
        for (FontInfo info : fontInfos)
        {
            fontsToReturn.add(fonts.get(info));
        }
        
        return fontsToReturn;
    }
    
    public Font getFontForObject(String path)
    {
        return fonts.get(path);
    }
    
    private void loadAllImages() 
    {
        for (String s : imagePaths) 
        {
            images.put(s, loader.loadImage(s));
        }
    }
}

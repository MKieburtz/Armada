package armada;

import java.awt.*;
import java.awt.geom.AffineTransform;
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
    
    private final ArrayList<String> soundPaths = new ArrayList<>();
    private final HashMap<String, Clip> sounds = new HashMap<>();

    private final ArrayList<FontInfo> fontData = new ArrayList<>();
    private final HashMap<FontInfo, Font> fonts = new HashMap<>();
    
    private final int BORDER_IMAGE_INDEX = 0;
    
    public Resources()
    {
        // for HUD
        imagePaths.add("Resources/SideBorder.png");
        imagePaths.add("Resources/TopBorder.png");
        
        // load the images
        loadAllImages();
    }
    
//    public static enum GeneratedImageIndexes
//    {
//        rotatedBorders
//    }
//    
//    public BufferedImage getGeneratedImage(GeneratedImageIndexes generatedIndexes)
//    {
//        return generatedImages.get(generatedIndexes);
//    }
    
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

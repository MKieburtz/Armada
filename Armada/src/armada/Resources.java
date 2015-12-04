package armada;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.sound.sampled.*;

/**
 * @author Michael Kieburtz
 */
public class Resources 
{
    private MediaLoader loader = new MediaLoader();

    private ArrayList<String> imagePaths = new ArrayList<>();
    private HashMap<String, BufferedImage> images = new HashMap<>();

    private ArrayList<String> soundPaths = new ArrayList<>();
    private HashMap<String, Clip> sounds = new HashMap<>();

    private ArrayList<FontInfo> fontData = new ArrayList<>();
    private HashMap<FontInfo, Font> fonts = new HashMap<>();
    
    public Resources()
    {
        // for HUD
        imagePaths.add("Resources/Border.png"); //brighter border
        imagePaths.add("Resources/Border2.png"); // darker but glowier border
        
        // load the images
        loadAllImages();
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

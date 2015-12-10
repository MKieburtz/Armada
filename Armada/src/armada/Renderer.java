package armada;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
/**
 * @author Michael Kieburtz
 */
public class Renderer 
{
    public void drawScreen(BufferStrategy bufferStrategy, BufferedImage imageToDraw)
    {
        do 
        {
            do 
            {                
                Graphics2D graphics = (Graphics2D)bufferStrategy.getDrawGraphics();
        
                graphics.drawImage(imageToDraw, 30, 30, null);
                graphics.dispose();
                
            } while (bufferStrategy.contentsRestored());
            
            bufferStrategy.show();
            
        } while (bufferStrategy.contentsLost());
    }
}

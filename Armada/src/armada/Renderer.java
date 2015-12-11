package armada;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
/**
 * @author Michael Kieburtz
 */
public class Renderer 
{
    private final int BORDER_OFFSET = 13;
    public void drawScreen(BufferStrategy bufferStrategy, BufferedImage imageToDraw,
            Dimension windowSize)
    {
        do 
        {
            do 
            {                
                Graphics2D g2d = (Graphics2D)bufferStrategy.getDrawGraphics();
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, (int)windowSize.getWidth(), (int)windowSize.getHeight());
                g2d.drawImage(imageToDraw, -BORDER_OFFSET, 0, null);
                g2d.drawImage(imageToDraw, windowSize.width - imageToDraw.getWidth() + BORDER_OFFSET, 0, null);
                g2d.dispose();
                
            } while (bufferStrategy.contentsRestored());
            
            bufferStrategy.show();
            
        } while (bufferStrategy.contentsLost());
    }
}

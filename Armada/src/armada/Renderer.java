package armada;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
/**
 * @author Michael Kieburtz
 */
public class Renderer 
{
    private final boolean isMac;
    private final int VERTICAL_BORDER_OFFSET;
    private final int HORIZONTAL_BORDER_OFFSET_TOP;
    private final int HORIZONTAL_BORDER_OFFSET_SIDE;
    private final int HORIZONTAL_BORDER_OFFSET_BOTTOM;
    private final int INIT_BORDER_OFFSET;
    
    public Renderer(boolean isMac)
    {
        // because Macs don't have borders on the windows except at the top
        this.isMac = isMac;
        VERTICAL_BORDER_OFFSET = isMac ? 16 : 6; 
        HORIZONTAL_BORDER_OFFSET_TOP = 6;
        HORIZONTAL_BORDER_OFFSET_SIDE = -22;
        HORIZONTAL_BORDER_OFFSET_BOTTOM = 14;
        INIT_BORDER_OFFSET = 10;
    }
    
    public void drawScreen(BufferStrategy bufferStrategy, BufferedImage verticalBorder, BufferedImage horizontalBorder,
            BufferedImage initBorder, Dimension windowSize)
    {
        do 
        {
            do 
            {                
                Graphics2D g2d = (Graphics2D)bufferStrategy.getDrawGraphics();
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, (int)windowSize.getWidth(), (int)windowSize.getHeight());
//                g2d.drawImage(verticalBorder, -VERTICAL_BORDER_OFFSET, 0, null);
//                g2d.drawImage(verticalBorder, windowSize.width - verticalBorder.getWidth() + VERTICAL_BORDER_OFFSET, 0, null);
//                g2d.drawImage(horizontalBorder, HORIZONTAL_BORDER_OFFSET_SIDE, HORIZONTAL_BORDER_OFFSET_TOP, null);
//                g2d.drawImage(horizontalBorder, HORIZONTAL_BORDER_OFFSET_SIDE, windowSize.height - HORIZONTAL_BORDER_OFFSET_BOTTOM, null);
                g2d.drawImage(initBorder, windowSize.width / 2 - INIT_BORDER_OFFSET, 0, null);
                g2d.dispose();
                
            } while (bufferStrategy.contentsRestored());
            
            bufferStrategy.show();
            
        } while (bufferStrategy.contentsLost());
    }
}

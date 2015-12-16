package armada;

import java.awt.*;
import java.awt.geom.AffineTransform;
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
    private int numInitFrames;
    private int initBorderFrame = 0;
    
    public Renderer(boolean isMac, Dimension windowSize)
    {
        // because Macs don't have borders on the windows except at the top
        this.isMac = isMac;
        VERTICAL_BORDER_OFFSET = isMac ? 16 : 8; 
        HORIZONTAL_BORDER_OFFSET_TOP = isMac ? 6 : 14;
        HORIZONTAL_BORDER_OFFSET_SIDE = isMac ? -22 : -15;
        HORIZONTAL_BORDER_OFFSET_BOTTOM = isMac ? 14 : 22;
        INIT_BORDER_OFFSET = 10;
        numInitFrames = (int)Math.ceil(windowSize.getHeight() / 25);
    }
    
    public void drawScreen(BufferStrategy bufferStrategy, BufferedImage verticalBorder, BufferedImage horizontalBorder,
            BufferedImage[] initBorderFrames, Dimension windowSize) {
        do 
        {
            do 
            {                
                Graphics2D g2d = (Graphics2D)bufferStrategy.getDrawGraphics();
                AffineTransform original = g2d.getTransform();
                AffineTransform transform = (AffineTransform)original.clone();
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, (int)windowSize.getWidth(), (int)windowSize.getHeight());
//                g2d.drawImage(verticalBorder, -VERTICAL_BORDER_OFFSET, 0, null);
//                g2d.drawImage(verticalBorder, windowSize.width - verticalBorder.getWidth() + VERTICAL_BORDER_OFFSET, 0, null);
//                g2d.drawImage(horizontalBorder, HORIZONTAL_BORDER_OFFSET_SIDE, HORIZONTAL_BORDER_OFFSET_TOP, null);
//                g2d.drawImage(horizontalBorder, HORIZONTAL_BORDER_OFFSET_SIDE, windowSize.height - HORIZONTAL_BORDER_OFFSET_BOTTOM, null);
                transform.translate(windowSize.width / 2 - INIT_BORDER_OFFSET, windowSize.height / 2 - 1012.5 + (initBorderFrame + 1) * 12.5);
                g2d.transform(transform);
                g2d.drawImage(initBorderFrames[initBorderFrame], 0, 0, null);
                g2d.setTransform(original);
                initBorderFrame++;
                g2d.dispose();
                
            } while (bufferStrategy.contentsRestored());
            
            bufferStrategy.show();
            
        } while (bufferStrategy.contentsLost());
    }
}

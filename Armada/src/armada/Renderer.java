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
    private int numHorizontalFrames;
    
    public Renderer(boolean isMac, Dimension windowSize)
    {
        // because Macs don't have borders on the windows except at the top
        this.isMac = isMac;
        VERTICAL_BORDER_OFFSET = isMac ? 16 : 8; 
        HORIZONTAL_BORDER_OFFSET_TOP = isMac ? 6 : 14;
        HORIZONTAL_BORDER_OFFSET_SIDE = isMac ? -22 : -15;
        HORIZONTAL_BORDER_OFFSET_BOTTOM = isMac ? 14 : 22;
        INIT_BORDER_OFFSET = 5;
        numInitFrames = (int)Math.ceil(windowSize.getHeight() / 25);
        numHorizontalFrames = (int)Math.ceil(windowSize.getWidth() / 25) + 1;
    }
    
    public void drawScreen(BufferStrategy bufferStrategy) {
        do 
        {
            do 
            {                
                Graphics2D g2d = (Graphics2D)bufferStrategy.getDrawGraphics();
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, (int)DrawingData.getScreenSize().getWidth(), (int)DrawingData.getScreenSize().getHeight());
                g2d.drawImage(DrawingData.getVerticalBorder(), -VERTICAL_BORDER_OFFSET, 0, null);
                g2d.drawImage(DrawingData.getVerticalBorder(), DrawingData.getScreenSize().width - DrawingData.getVerticalBorder().getWidth() + VERTICAL_BORDER_OFFSET, 0, null);
                g2d.drawImage(DrawingData.getHorizontalBorder(), HORIZONTAL_BORDER_OFFSET_SIDE, HORIZONTAL_BORDER_OFFSET_TOP, null);
                g2d.drawImage(DrawingData.getHorizontalBorder(), HORIZONTAL_BORDER_OFFSET_SIDE, DrawingData.getScreenSize().height - HORIZONTAL_BORDER_OFFSET_BOTTOM, null);
                g2d.dispose();
                
            } while (bufferStrategy.contentsRestored());
            
            bufferStrategy.show();
            
        } while (bufferStrategy.contentsLost());
    }
    
    private int initBorderFrame = 0;
    private boolean doneWithInit = false;
    private int horizontalBorderFrame = 0;
    public boolean drawInitBorders(BufferStrategy bs, BufferedImage[] initBorderFrames, BufferedImage[] horizontalBorderFrames) {
        do 
        {
            do 
            {                
                Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, (int)DrawingData.getScreenSize().getWidth(), (int)DrawingData.getScreenSize().getHeight());
                AffineTransform original = g2d.getTransform();
                AffineTransform transform = (AffineTransform)original.clone();
                if (!doneWithInit)
                {
                    transform.translate(DrawingData.getScreenSize().width / 2 - INIT_BORDER_OFFSET, DrawingData.getScreenSize().height / 2 - 2000 + 12.5 + (initBorderFrame + 1) * 12.5);
                    g2d.transform(transform);
                    g2d.drawImage(initBorderFrames[initBorderFrame], 0, 0, null);
                    g2d.setTransform(original);
                    if (initBorderFrame != numInitFrames)
                    {
                        initBorderFrame++;
                    }
                    else
                    {
                        doneWithInit = true;
                    }
                }
                else
                {
                    // set up and draw the top horizontal border
                    transform.translate(DrawingData.getScreenSize().width / 2 - 2012.5 + (horizontalBorderFrame + 1) * 12.5 + 25, HORIZONTAL_BORDER_OFFSET_TOP);
                    g2d.transform(transform);
                    g2d.drawImage(horizontalBorderFrames[horizontalBorderFrame], 0, 0, null);
                    g2d.setTransform(original);
                    // set up and draw the bottom horizontal border
                    transform.setToIdentity();
                    transform.translate(DrawingData.getScreenSize().width / 2 - 2012.5 + (horizontalBorderFrame + 1) * 12.5 + 25, DrawingData.getScreenSize().height - HORIZONTAL_BORDER_OFFSET_BOTTOM);
                    g2d.transform(transform);
                    g2d.drawImage(horizontalBorderFrames[horizontalBorderFrame], 0, 0, null);
                    g2d.setTransform(original);
                    transform.setToIdentity();
                    // draw the two vertical borders
                    // right
                    transform.translate(DrawingData.getScreenSize().width / 2 + 12.5 * (horizontalBorderFrame + 1) - 24, 0);
                    g2d.transform(transform);
                    g2d.drawImage(DrawingData.getInitBorder(), 0, 0, null);
                    g2d.setTransform(original);
                    transform.setToIdentity();
                    // left
                    transform.translate(DrawingData.getScreenSize().width / 2 - 12.5 * (horizontalBorderFrame + 1), 0);
                    g2d.transform(transform);
                    g2d.drawImage(DrawingData.getInitBorder(), 0, 0, null);
                    g2d.setTransform(original);
                    horizontalBorderFrame++;
                }
                g2d.dispose();
                
            } while (bs.contentsRestored());
            
            bs.show();
            
        } while (bs.contentsLost());
        
        return horizontalBorderFrame == numHorizontalFrames;
    }
}

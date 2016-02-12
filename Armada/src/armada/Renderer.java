package armada;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
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
    
    private void drawBorders(Graphics2D g2d)
    {          
        g2d.drawImage(DrawingData.getVerticalBorder(), -VERTICAL_BORDER_OFFSET, 0, null);
        g2d.drawImage(DrawingData.getVerticalBorder(), DrawingData.getScreenSize().width - DrawingData.getVerticalBorder().getWidth() + VERTICAL_BORDER_OFFSET, 0, null);
        g2d.drawImage(DrawingData.getHorizontalBorder(), HORIZONTAL_BORDER_OFFSET_SIDE, HORIZONTAL_BORDER_OFFSET_TOP, null);
        g2d.drawImage(DrawingData.getHorizontalBorder(), HORIZONTAL_BORDER_OFFSET_SIDE, DrawingData.getScreenSize().height - HORIZONTAL_BORDER_OFFSET_BOTTOM, null);
    }
    
    public void drawScreen(BufferStrategy bs) 
    {
        do 
        {
            do 
            {                
                Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, (int)DrawingData.getScreenSize().getWidth(), (int)DrawingData.getScreenSize().getHeight());
                drawBorders(g2d);
                
                Composite originalComposite = g2d.getComposite();
                Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .07f);
        
                g2d.setComposite(comp);
                g2d.setColor(Color.GREEN);
                g2d.fillRect(0, 0, DrawingData.getScreenSize().width, DrawingData.getScreenSize().height);
                g2d.setComposite(originalComposite);
                
                for (Ship s : DrawingData.getShips())
                {
                    s.draw(g2d);
                }
                g2d.dispose();
                
            } while (bs.contentsRestored());
            
            bs.show();
            
        } while (bs.contentsLost());
    }
    
    private int initBorderFrame = 0;
    private boolean doneWithInit = false;
    private int horizontalBorderFrame = 0;
    public boolean drawInitBorders(BufferStrategy bs) 
    {
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
                    transform.translate(DrawingData.getScreenSize().width / 2 - INIT_BORDER_OFFSET,
                            DrawingData.getScreenSize().height / 2 - 2000 + 12.5 + (initBorderFrame + 1) * 12.5);
                    g2d.transform(transform);
                    g2d.drawImage(DrawingData.getInitBorderFrames()[initBorderFrame], 0, 0, null);
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
                    double borderLeftX = DrawingData.getScreenSize().width / 2 - 2012.5 + (horizontalBorderFrame + 1) * 12.5 + 25;
                    int borderWidth = (int)((horizontalBorderFrame + 1) * 12.5) * 2;
                    
                    Composite originalComposite = g2d.getComposite();
                    Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .07f);
                    
                    g2d.setComposite(comp);
                    g2d.setColor(Color.GREEN);
                    transform.translate(DrawingData.getScreenSize().width / 2 - (horizontalBorderFrame + 1) * 12.5, 0);
                    g2d.transform(transform);
                    g2d.fillRect(0, 0, borderWidth, DrawingData.getScreenSize().height);
                    g2d.setComposite(originalComposite);
                    g2d.setTransform(original);
                    
                    // set up and draw the top horizontal border
                    transform.setToIdentity();
                    transform.translate(borderLeftX, HORIZONTAL_BORDER_OFFSET_TOP);
                    g2d.transform(transform);
                    g2d.drawImage(DrawingData.getHorizontalBorderFrames()[horizontalBorderFrame], 0, 0, null);
                    g2d.setTransform(original);
                    // set up and draw the bottom horizontal border
                    transform.setToIdentity();
                    transform.translate(DrawingData.getScreenSize().width / 2 - 2012.5 + (horizontalBorderFrame + 1) * 12.5 + 25, DrawingData.getScreenSize().height - HORIZONTAL_BORDER_OFFSET_BOTTOM);
                    g2d.transform(transform);
                    g2d.drawImage(DrawingData.getHorizontalBorderFrames()[horizontalBorderFrame], 0, 0, null);
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
    
    public void drawMainMenu(BufferStrategy bs, MainMenu mainMenu)
    {
        do 
        {
            do 
            {                
                Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, (int)DrawingData.getScreenSize().getWidth(), (int)DrawingData.getScreenSize().getHeight());
                drawBorders(g2d);
                mainMenu.draw(g2d);
                
                g2d.dispose();
                
            } while (bs.contentsRestored());
            
            bs.show();
            
        } while (bs.contentsLost());
    }
}

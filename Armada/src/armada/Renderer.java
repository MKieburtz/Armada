package armada;

import java.awt.*;
import java.awt.event.MouseEvent;
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
    private final int numInitFrames;
    private final int numHorizontalFrames;
    
    private final GraphicsConfiguration graphicsConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    private VolatileImage drawingImage  = graphicsConfig.createCompatibleVolatileImage(1, 1);
    
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
    
    public void sizeChanged() 
    {
        if (isMac)
        {
            drawingImage = graphicsConfig.createCompatibleVolatileImage((int)DrawingData.getScreenSize().getWidth(), (int)DrawingData.getScreenSize().getHeight(), Transparency.TRANSLUCENT);
        } 
        else 
        {
            drawingImage = graphicsConfig.createCompatibleVolatileImage((int)DrawingData.getScreenSize().getWidth(), (int)DrawingData.getScreenSize().getHeight());
        }
    }
    
    private void drawBorders(Graphics2D g2d)
    {          
        g2d.drawImage(DrawingData.getVerticalBorder(), -VERTICAL_BORDER_OFFSET, 0, null);
        g2d.drawImage(DrawingData.getVerticalBorder(), DrawingData.getScreenSize().width - DrawingData.getVerticalBorder().getWidth() + VERTICAL_BORDER_OFFSET, 0, null);
        g2d.drawImage(DrawingData.getHorizontalBorder(), HORIZONTAL_BORDER_OFFSET_SIDE, HORIZONTAL_BORDER_OFFSET_TOP, null);
        g2d.drawImage(DrawingData.getHorizontalBorder(), HORIZONTAL_BORDER_OFFSET_SIDE, DrawingData.getScreenSize().height - HORIZONTAL_BORDER_OFFSET_BOTTOM, null);
    }
    
    public void drawScreen(Graphics g) 
    {           
        Graphics2D g2d = drawingImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, (int)DrawingData.getScreenSize().getWidth(), (int)DrawingData.getScreenSize().getHeight());

        drawBorders(g2d);

        Composite originalComposite = g2d.getComposite();
        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f);

        g2d.setComposite(comp);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, DrawingData.getScreenSize().width, DrawingData.getScreenSize().height);
        g2d.setComposite(originalComposite);

        for (Ship s : DrawingData.getShips())
        {
            s.draw(g2d);
        }
        if (DrawingData.getSelectionRect().isActive())
        {
            DrawingData.getSelectionRect().draw(g2d);
        }
        g.drawImage(drawingImage, 0, 0, null);
        g2d.dispose();
    }
    
    private int initBorderFrame = 0;
    private boolean doneWithInit = false;
    private int horizontalBorderFrame = 0;
    public boolean drawInitBorders(Graphics g) 
    {           
        Graphics2D g2d = drawingImage.createGraphics();
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
            Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f);

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
        
        g.drawImage(drawingImage, 0, 0, null);
        
        g2d.dispose();
        
        return horizontalBorderFrame == numHorizontalFrames;
    }
    
    public void drawMainMenu(Graphics g, MainMenu mainMenu)
    {      
        Graphics2D g2d = drawingImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, (int)DrawingData.getScreenSize().getWidth(), (int)DrawingData.getScreenSize().getHeight());
        drawBorders(g2d);
        mainMenu.draw(g2d);

        g.drawImage(drawingImage, 0, 0, null);
        
        g2d.dispose();
    }
}

package armada;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import sun.nio.ch.DatagramSocketAdaptor;

/**
 * @author Michael Kieburtz
 */
public class ArmadaWindow extends JFrame
{
    private final Panel panel;
    private final ArrayList<String> imagePaths = new ArrayList<>();
    private final ArrayList<BufferedImage> images = new ArrayList<>();
    private final BufferedImage[] initBorderFrames;
    private final BufferedImage[] horizontalBorderFrames;
    private final Renderer renderer;
    private Dimension windowSize;
        
    public ArmadaWindow()
    {
        renderer = new Renderer(System.getProperty("os.name").contains("OS X"), new Dimension(1000, 600));
        imagePaths.add("Resources/SideBorder.png");
        imagePaths.add("Resources/TopBorder.png");
        
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
        initBorderFrames = GameData.getResources().getGeneratedImagesForObject(Resources.GeneratedImagesType.animatedInitBorder);
        horizontalBorderFrames = GameData.getResources().getGeneratedImagesForObject(Resources.GeneratedImagesType.animatedHoritontalBorder);
        
        panel = new Panel();
        setUpWindow();
    }

    private void setUpWindow()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Armada");
        setMinimumSize(new Dimension(1000, 600));
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        
        windowSize = getSize();

        getContentPane().add(panel);
        
        addComponentListener(new ComponentListener()
        {
            @Override
            public void componentResized(ComponentEvent e) 
            {
                windowSize = getSize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {}
        });
        
        setVisible(true);
        panel.initBufferStrategy();
    }
    
    public final class Panel extends JPanel
    {
        private BufferStrategy bufferStrategy;
        public Panel()
        {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setBackground(Color.BLACK);
        }
        
        public void initBufferStrategy()
        {
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }

        public BufferStrategy getDrawingStrategy() 
        {
            return bufferStrategy;
        }
    }
    
    public void draw()
    {
        //renderer.drawScreen(panel.getDrawingStrategy(), images.get(0), images.get(1), windowSize);
        renderer.drawInitBorders(panel.getDrawingStrategy(), initBorderFrames, horizontalBorderFrames, windowSize);
    }
}

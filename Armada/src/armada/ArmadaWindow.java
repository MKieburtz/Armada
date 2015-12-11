package armada;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @author Michael Kieburtz
 */
public class ArmadaWindow extends JFrame
{
    private Panel panel;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private ArrayList<BufferedImage> images = new ArrayList<>();
    private Renderer renderer;
    private Dimension windowSize;
        
    public ArmadaWindow()
    {
        renderer = new Renderer();
        
        imagePaths.add("Resources/Border.png");
        
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Armada");
        setMinimumSize(new Dimension(1000, 600));
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        
        windowSize = getSize();
        
        panel = new Panel();
        getContentPane().add(panel);
        
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
        renderer.drawScreen(panel.getDrawingStrategy(), images.get(0), windowSize);
    }
}

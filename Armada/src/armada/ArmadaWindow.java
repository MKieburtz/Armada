package armada;

import java.awt.Color;
import java.awt.Dimension;
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
    
    public ArmadaWindow()
    {
        imagePaths.add("Resources/Border.png");
        imagePaths.add("Resources/Border2.png");
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Armada");
        setMinimumSize(new Dimension(1000, 600));
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        
        panel = new Panel();
        getContentPane().add(panel);
        
        setVisible(true);
    }
    
    public class Panel extends JPanel
    {
        public Panel()
        {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setBackground(Color.BLACK);
        }
    }
}

package armada;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;

/**
 * @author Michael Kieburtz
 */
public class ArmadaWindow extends JFrame
{
    private Panel panel;
    public ArmadaWindow()
    {
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

package armada;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;

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
    private final GameActionListener gameActionListener;
    private final MainMenu mainMenu;
    private final Dimension borderSize;
        
    public ArmadaWindow(GameActionListener actionListener)
    {
        this.gameActionListener = actionListener;
        renderer = new Renderer(System.getProperty("os.name").contains("OS X"), new Dimension(1000, 600));   
        
        mainMenu = new MainMenu(actionListener);
        imagePaths.add("Resources/SideBorder.png");
        imagePaths.add("Resources/TopBorder.png");
        imagePaths.add("Resources/InitBorder.png");
        imagePaths.add("Resources/InitVerticalBorder.png");
        imagePaths.add("Resources/Background.png");
        
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
        DrawingData.setVerticalBorder(images.get(0));
        DrawingData.setHorizontalBorder(images.get(1));
        DrawingData.setInitBorder(images.get(2));
        DrawingData.setBackgroundImage(images.get(4));
        initBorderFrames = GameData.getResources().getGeneratedImagesForObject(Resources.GeneratedImagesType.ANIMATED_INIT_BORDER);
        horizontalBorderFrames = GameData.getResources().getGeneratedImagesForObject(Resources.GeneratedImagesType.ANIMATED_HORIZONTAL_BORDER);
        DrawingData.setHorizontalBorderFrames(horizontalBorderFrames);
        DrawingData.setInitBorderFrames(initBorderFrames);
        panel = new Panel();
        setUpWindow();
        if (System.getProperty("os.name").contains("Mac"))
        {
            borderSize = new Dimension(getSize().width - getContentPane().getSize().width,
                getSize().height - getContentPane().getSize().height - 25);
        }
        else
        {
            borderSize = new Dimension(getSize().width - getContentPane().getSize().width - 15,
                getSize().height - getContentPane().getSize().height - 35);
        }
        GameData.setBorderSize(borderSize);
        mainMenu.setButtonRects();
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
                DrawingData.setScreenSize(panel.getSize());
                renderer.sizeChanged();
                mainMenu.setButtonRects();
            }

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {}
        });
        
        setVisible(true);
        DrawingData.setScreenSize(panel.getSize());
        renderer.sizeChanged();
    }
    
    public final class Panel extends JPanel
    {
        public Panel()
        {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            
            addMouseListener(new MouseAdapter() 
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    gameActionListener.mousePressed(e);
                }
                
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    gameActionListener.mouseReleased(e);
                }
            });
            
            addMouseMotionListener(new MouseMotionAdapter() 
            {
                @Override
                public void mouseMoved(MouseEvent e) 
                {
                    gameActionListener.mouseMoved(e);
                }
                
                @Override
                public void mouseDragged(MouseEvent e)
                {
                    gameActionListener.mouseDragged(e);
                }
            });
        }
    }
    
    public void CheckMousePressedForMainMenu(MouseEvent e)
    {
        mainMenu.checkMousePressed(Calculator.compensateForBorders(e.getPoint()));
    }
    
    public void checkMouseMovedForMainMenu(MouseEvent e)
    {
        mainMenu.checkMouseMoved(Calculator.compensateForBorders(e.getPoint()));
    }
    
    private boolean doneWithInit = false;
    public void draw(GameState state)
    {
        
        switch (state)
        {
            case OPENING:
                if (renderer.drawInitBorders(panel.getGraphics()))
                {
                    gameActionListener.doneOpening();
                }
                break;
            case MAIN_MENU:
                renderer.drawMainMenu(panel.getGraphics(), mainMenu);
                break;
            case PLAYING:
                renderer.drawScreen(panel.getGraphics());
                break;
        }
    }
}

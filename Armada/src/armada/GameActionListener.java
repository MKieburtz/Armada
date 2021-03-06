package armada;

import java.awt.event.MouseEvent;

/**
 * @author Michael Kieburtz
 */
public interface GameActionListener 
{
    public void doneOpening();
    public void startButtonPressed();
    public void mousePressed(MouseEvent e);
    public void mouseMoved(MouseEvent e);
    public void mouseDragged(MouseEvent e);
    public void mouseReleased(MouseEvent e);
}

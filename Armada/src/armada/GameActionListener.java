package armada;

import java.awt.event.MouseEvent;

/**
 * @author 543021
 */
public interface GameActionListener 
{
    public void doneOpening();
    public void startButtonPressed();
    public void mousePressed(MouseEvent e);
    public void mouseMoved(MouseEvent e);
}

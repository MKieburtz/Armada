package armada;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Michael Kieburtz
 */
public class SelectionRect 
{
    private Rectangle rect = new Rectangle();
    private Point anchorPoint = new Point();
    private boolean active;
    
    public SelectionRect()
    {
        active = false;
        rect = new Rectangle(0, 0, 0, 0);
    }
    
    public void draw(Graphics2D g2d)
    {
        g2d.setColor(Color.CYAN);
        g2d.draw(rect);
    }
    
    public void updateRect(Point newPoint)
    {
        rect.width = newPoint.x - rect.x;
        rect.height = newPoint.y - rect.y;
        if (newPoint.x < anchorPoint.x)
        {
            rect.width = anchorPoint.x - newPoint.x;
            rect.x = newPoint.x;
        }
        if (newPoint.y < anchorPoint.y)
        {
            rect.height = anchorPoint.y - newPoint.y;
            rect.y = newPoint.y;
        }         
    }
    
    public void activateRect(Point anchorPoint)
    {
        active = true;
        this.anchorPoint = anchorPoint;
        rect.x = anchorPoint.x;
        rect.y = anchorPoint.y;
    }
    
    public void deactivateRect()
    {
        active = false;
        resetRect();
    }
    
    private void resetRect()
    {
        rect.x = 0;
        rect.y = 0;
        rect.width = 0;
        rect.height = 0;
    }
    
    public boolean isActive()
    {
        return active;
    }
    
    public boolean checkForIntersection(Rectangle2D.Double other)
    {
        return rect.intersects(other) || other.contains(anchorPoint);
    }
}

package armada;

import java.awt.*;

/**
 * @author 543021
 */
public class SelectionRect 
{
    private Rectangle rect = new Rectangle();
    private Point anchorPoint;
    private boolean active;
    
    public SelectionRect()
    {
        active = false;
        rect = new Rectangle(0, 0, 0, 0);
    }
    
    public void draw(Graphics2D g2d)
    {
        g2d.setColor(Color.WHITE);
        g2d.draw(rect);
    }
    
    public void updateRect(Point newPoint)
    {
        if (newPoint.x < anchorPoint.x)
        {
            rect.x = newPoint.x;
        }
        if (newPoint.y < anchorPoint.y)
        {
            rect.y = newPoint.y;
        }         
        rect.width = Math.abs(newPoint.x - rect.x);
        rect.height = Math.abs(newPoint.y - rect.y);
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
}

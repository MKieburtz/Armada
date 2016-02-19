package armada;

import java.awt.*;
import java.awt.geom.*;

/**
 * @author Michael Kieburtz
 */
public class Ship extends GameEntity
{  
    private final int NORMAL_SHIP = 0;
    private final int SELECTED_SHIP = 1; 
    private State state;
    private Rectangle2D.Double boundingRect;
    
    public Ship()
    {
        location = new Point2D.Double(400, 400);
        imagePaths.add("Resources/Ship.png");
        imagePaths.add("Resources/SelectedShip.png");
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
        boundingRect = new Rectangle2D.Double(location.x, location.y, images.get(NORMAL_SHIP).getWidth(), images.get(NORMAL_SHIP).getHeight());
        state = State.idle;
    }
    
    enum State
    {
        idle,
        selected
    }

    @Override
    public void draw(Graphics2D g2d) 
    {
        AffineTransform original = g2d.getTransform();
        AffineTransform transform = (AffineTransform)original.clone();
        
        transform.translate(location.x, location.y);
        g2d.transform(transform);
        switch (state)
        {
            case idle:
                g2d.drawImage(images.get(NORMAL_SHIP), 0, 0, null);
                break;
            case selected:
                g2d.drawImage(images.get(SELECTED_SHIP), 0, 0, null);
                break;
        }
        g2d.setTransform(original);
        //g2d.drawRect((int)boundingRect.x, (int)boundingRect.y, (int)boundingRect.width, (int)boundingRect.height);
    }
    
    public void checkMousePressed(Point location)
    {
        if (boundingRect.contains(location))
        {
            state = State.selected;
        }
        else
        {
            state = State.idle;
        }
    }
    
    public Rectangle2D.Double getBoundingRect()
    {
        return boundingRect;
    }
    
    public void select()
    {
        state = State.selected;
    }
    
    public void deSelect()
    {
        state = State.idle;
    }
}

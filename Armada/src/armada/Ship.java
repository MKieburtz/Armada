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
    private final Rectangle2D.Double boundingRect;
    private final Point2D.Double centerPoint;
    private Vector velocityVector;
    private Vector accelerationVector;
    private double faceAngle;
    
    public Ship(Point2D.Double location)
    {
        this.location = location;
        imagePaths.add("Resources/Ship.png");
        imagePaths.add("Resources/SelectedShip.png");
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
        boundingRect = new Rectangle2D.Double(location.x, location.y, images.get(NORMAL_SHIP).getWidth(), images.get(NORMAL_SHIP).getHeight());
        centerPoint = new Point2D.Double(location.x + images.get(NORMAL_SHIP).getWidth() / 2, location.y + images.get(NORMAL_SHIP).getHeight() / 2);
        state = State.idle;
        accelerationVector = new Vector(-.1, 90);
        faceAngle = 90;
        velocityVector = new Vector(new Point2D.Double(0, 10));
    }
    
    enum State
    {
        idle,
        selected
    }
    
    public void move(MovementCommand command)
    {
        Point2D.Double p = new Point2D.Double(command.getDestination().x, command.getDestination().y);
        faceAngle = Calculator.getAngleBetweenTwoPoints(centerPoint, p);
        //System.out.println(faceAngle);
    }
    
    public void update()
    {
        velocityVector = velocityVector.add(accelerationVector);
        location.x += velocityVector.getComponents().getX();
        location.y += velocityVector.getComponents().getY();
    }

    @Override
    public void draw(Graphics2D g2d) 
    {
        AffineTransform original = g2d.getTransform();
        AffineTransform transform = (AffineTransform)original.clone();
        transform.rotate(Math.toRadians(Calculator.convertAngleForAffineTransform(faceAngle)), centerPoint.x, centerPoint.y);
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
    
    public boolean checkMousePressed(Point location)
    {
        if (boundingRect.contains(location))
        {
            state = State.selected;
            return true;
        }
        else
        {
            state = State.idle;
            return false;
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

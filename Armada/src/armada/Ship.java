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
    private final Dimension imageSize = new Dimension();

    private SelectionState state;
    private final Rectangle2D.Double boundingRect;
    private final Point2D.Double centerPoint;
    
    private Vector velocityVector;
    private Vector accelerationVector;
    private final int MAX_VELOCITY = 4;
    private final double MAX_POSITIVE_ACCELERATION = .1;
    private final double MAX_NEGATIVE_ACCELERATION = -.2;
    
    private double faceAngle;
    private double angularVelocity;
    private double angularAcceleration;
    
    private double targetAngle = 0;
    private Point2D.Double targetPoint = new Point2D.Double(-1, -1);
    
    
    public Ship(Point2D.Double location)
    {
        this.location = location;
        imagePaths.add("Resources/Ship.png");
        imagePaths.add("Resources/SelectedShip.png");
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
        
        imageSize.setSize(images.get(0).getWidth(), images.get(1).getHeight());
        
        boundingRect = new Rectangle2D.Double(location.x, location.y, images.get(NORMAL_SHIP).getWidth(), images.get(NORMAL_SHIP).getHeight());
        centerPoint = new Point2D.Double(location.x + images.get(NORMAL_SHIP).getWidth() / 2, location.y + images.get(NORMAL_SHIP).getHeight() / 2);
        
        state = SelectionState.IDLE;
        accelerationVector = new Vector(0, faceAngle);
        faceAngle = 0;
        velocityVector = new Vector(0, faceAngle);
        angularAcceleration = .1;
    }
    
    
    enum SelectionState
    {
        IDLE,
        SELECTED
    }
    
    enum MovementState
    {
        IDLE,
        CONSTANT,
        SLOWING_DOWN,
        SPEEDING_UP,
    }
    
    enum RotationDirection
    {
        IDLE,
        RIGHT,
        LEFT
    }
    
    enum RotationAcceleration
    {
        IDLE,
        SLOWING_DOWN,
        SPEEDING_UP
    }
    
    public void move(MovementCommand command)
    {
        targetPoint = new Point2D.Double(command.getDestination().x, command.getDestination().y);
        calculcateTargetAngle(targetPoint);
        velocityVector.setDirectionAndMagnitude(4, faceAngle);
    }
    
    private void calculcateTargetAngle(Point2D.Double targetPoint)
    {
        targetAngle = Calculator.getAngleBetweenTwoPoints(centerPoint, targetPoint);
        double[] angleDistances = Calculator.getDistancesBetweenAngles(faceAngle, targetAngle);
        angularAcceleration = angleDistances[0] < angleDistances[1] ? MAX_POSITIVE_ACCELERATION : MAX_NEGATIVE_ACCELERATION;
    }
    
    public void update()
    {
        velocityVector = velocityVector.add(accelerationVector);

//        if (!targetPoint.equals(new Point2D.Double(-1, -1)))
//        {
//            calculcateTargetAngle(targetPoint);
//        }
        angularVelocity += angularAcceleration;
        faceAngle += angularVelocity;
        faceAngle = Calculator.normalizeAngle(faceAngle);
        
        if (Math.abs(faceAngle - targetAngle) <= 1)
        {
            angularAcceleration = 0;
            angularVelocity = 0;
            targetPoint.setLocation(-1, -1);
        }
                
        velocityVector.setDirectionAndMagnitude(velocityVector.getDirectionAndMagnitude().y, faceAngle);
        location.x += velocityVector.getComponents().getX();
        location.y += velocityVector.getComponents().getY();        
        
        centerPoint.setLocation(location.x + imageSize.getWidth(), location.y + imageSize.getHeight());

        boundingRect.setFrame(location, new Dimension((int)boundingRect.getHeight(), (int)boundingRect.getWidth()));
    }
    
    @Override
    public void draw(Graphics2D g2d) 
    {
        AffineTransform original = g2d.getTransform();
        AffineTransform transform = (AffineTransform)original.clone();
        
        transform.translate(location.x, location.y);
        transform.rotate(Math.toRadians(Calculator.convertAngleForAffineTransform(faceAngle)), imageSize.getWidth() / 2, imageSize.getHeight() / 2);
        
        g2d.transform(transform);
        switch (state)
        {
            case IDLE:
                g2d.drawImage(images.get(NORMAL_SHIP), 0, 0, null);
                break;
            case SELECTED:
                g2d.drawImage(images.get(SELECTED_SHIP), 0, 0, null);
                break;
        }
        g2d.setTransform(original);
        //g2d.draw(boundingRect);
        //g2d.fillRect((int)(imageSize.getWidth()), (int)(imageSize.getHeight()), 2, 2);
    }
    
    public boolean checkMousePressed(Point location)
    {
        if (boundingRect.contains(location))
        {
            state = SelectionState.SELECTED;
            return true;
        }
        else
        {
            state = SelectionState.IDLE;
            return false;
        }
    }
    
    public Rectangle2D.Double getBoundingRect()
    {
        return boundingRect;
    }
    
    public void select()
    {
        state = SelectionState.SELECTED;
    }
    
    public void deSelect()
    {
        state = SelectionState.IDLE;
    }
}

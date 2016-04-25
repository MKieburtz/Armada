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

    private SelectionState selectionState;
    private MovementState movementState;
    private RotationDirection rotationDirection;
    private RotationState rotationState;
    
    private final Rectangle2D.Double boundingRect;
    private final Point2D.Double centerPoint;
    
    private Vector velocityVector;
    private Vector accelerationVector;
    private final double MAX_VELOCITY = 4;
    private final double MAX_ACCELERATION = .1;
    
    private final double MAX_ANGULAR_VELOCITY = 3;
    private final double MAX_ANGULAR_ACCELERATION = .1;
    
    private double faceAngle;
    private double angularVelocity;
    private double angularAcceleration;
    
    private boolean rotatingToTarget = false;
    private boolean movingToTarget = false;
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
        
        selectionState = SelectionState.IDLE;
        accelerationVector = new Vector(0, faceAngle);
        faceAngle = 0;
        velocityVector = new Vector(0, faceAngle);
        
        angularAcceleration = 0;
        angularVelocity = 0;
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
    
    enum RotationState
    {
        IDLE,
        CONSTANT,
        ACCELERATING_RIGHT,
        ACCELERATING_LEFT
    }
    
    public void move(MovementCommand command)
    {
        rotatingToTarget = true;
        movingToTarget = true;
        targetPoint = new Point2D.Double(command.getDestination().x, command.getDestination().y);
        calculcateTargetAngle(targetPoint);
        velocityVector.setDirectionAndMagnitude(4, faceAngle);
    }
    
    private void calculcateTargetAngle(Point2D.Double targetPoint)
    {
        targetAngle = Calculator.getAngleBetweenTwoPoints(centerPoint, targetPoint);
        double[] angleDistances = Calculator.getDistancesBetweenAngles(faceAngle, targetAngle);
        angularVelocity = angleDistances[0] < angleDistances[1] ? MAX_ANGULAR_VELOCITY : -MAX_ANGULAR_VELOCITY;
    }
    
    public void update()
    {
//        if (!targetPoint.equals(new Point2D.Double(-1, -1)))
//        {
//            calculcateTargetAngle(targetPoint);
//        }
        if (rotatingToTarget)
        {
            calculcateTargetAngle(targetPoint);
        }
        
        angularVelocity += angularAcceleration;
        faceAngle += angularVelocity;
        faceAngle = Calculator.normalizeAngle(faceAngle);
        
        if (rotatingToTarget && Math.abs(faceAngle - targetAngle) <= 5)
        {
            angularAcceleration = 0;
            angularVelocity = 0;
            rotatingToTarget = false;
        }
        
        if (movingToTarget && Calculator.getDistance(location, targetPoint) < 30)
        {
            velocityVector.setDirectionAndMagnitude(0, 0);
            accelerationVector.setDirectionAndMagnitude(0, 0);
            movingToTarget = false;
        }
        
        velocityVector = velocityVector.add(accelerationVector);        
        velocityVector.setDirectionAndMagnitude(velocityVector.getDirectionAndMagnitude().y, faceAngle);
        
        updateState();
        //printStates();
        
        location.x += velocityVector.getComponents().getX();
        location.y += velocityVector.getComponents().getY();        
        
        centerPoint.setLocation(location.x + imageSize.getWidth(), location.y + imageSize.getHeight());

        boundingRect.setFrame(location, new Dimension((int)boundingRect.getHeight(), (int)boundingRect.getWidth()));
    }
    
    private void updateState()
    {
        if (Math.abs(velocityVector.getDirectionAndMagnitude().y) > 0 && accelerationVector.getDirectionAndMagnitude().y == 0)
        {
            movementState = MovementState.CONSTANT;
        }
        else if (accelerationVector.getDirectionAndMagnitude().y < 0)
        {
            movementState = MovementState.SLOWING_DOWN;
        }
        else if (accelerationVector.getDirectionAndMagnitude().y > 0)
        {
            movementState = MovementState.SPEEDING_UP;
        }
        else 
        {
            movementState = MovementState.IDLE;
        }
        //////////////////////////////////////////////////////////////
        if (angularVelocity > 0)
        {
            rotationDirection = RotationDirection.LEFT;
        }
        else if (angularVelocity < 0)
        {
            rotationDirection = RotationDirection.RIGHT;
        }
        else
        {
            rotationDirection = RotationDirection.IDLE;
        }
        //////////////////////////////////////////////////////////////
        if (angularVelocity > 0 && angularAcceleration == 0)
        {
            rotationState = RotationState.CONSTANT;
        }
        else if (angularAcceleration < 0)
        {
            rotationState = RotationState.ACCELERATING_RIGHT;
        }
        else if (angularAcceleration > 0)
        {
            rotationState = RotationState.ACCELERATING_LEFT;
        }
        else
        {
            rotationState = RotationState.IDLE;
        }
    }
    
    private void printStates()
    {
        System.out.println("selection: " + selectionState);
        System.out.println("movement: " + movementState);
        System.out.println("rotation direction: " + rotationDirection);
        System.out.println("rotation state: " + rotationState + "\n");
    }
    
    @Override
    public void draw(Graphics2D g2d) 
    {
        AffineTransform original = g2d.getTransform();
        AffineTransform transform = (AffineTransform)original.clone();
        
        transform.translate(location.x, location.y);
        transform.rotate(Math.toRadians(Calculator.convertAngleForAffineTransform(faceAngle)), imageSize.getWidth() / 2, imageSize.getHeight() / 2);
        
        g2d.transform(transform);
        switch (selectionState)
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
            selectionState = SelectionState.SELECTED;
            return true;
        }
        else
        {
            selectionState = SelectionState.IDLE;
            return false;
        }
    }
    
    public Rectangle2D.Double getBoundingRect()
    {
        return boundingRect;
    }
    
    public void select()
    {
        selectionState = SelectionState.SELECTED;
    }
    
    public void deSelect()
    {
        selectionState = SelectionState.IDLE;
    }
}

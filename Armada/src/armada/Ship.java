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
    private double targetVelocityMagnitude;
    private Vector accelerationVector;
    private Vector targetVector = new Vector(0, 0);
    private final double MAX_VELOCITY = 10;
    private final double MAX_ACCELERATION = .2;
    
    private final double MAX_ANGULAR_VELOCITY = 5;
    private final double MAX_ANGULAR_ACCELERATION = .2;
   
    private double testingRangeForSlowingDownAngle;
    private double testingRangeForSlowingDown;
        
    private double faceAngle;
    private double angularVelocity;
    private double targetAngularVelocity;
    private double angularAcceleration;
    
    private boolean rotatingToTarget = false;
    private boolean movingToTarget = false;
    private boolean locked = false;
    private double targetAngle = 0;
    private Point2D.Double targetPoint = new Point2D.Double(0, 0);
    
    private final FontInfo dataFontInfo = new FontInfo("Resources/Orbitron-Regular.ttf", 15f);
    private final Font dataFont;
    
    //testing variables
    private Point2D.Double startingLocation;
    private Point2D.Double endingLocation;
    
    public Ship(Point2D.Double location)
    {
        this.location = location;
        imagePaths.add("Resources/Ship.png");
        imagePaths.add("Resources/SelectedShip.png");
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
        
        imageSize.setSize(images.get(0).getWidth(), images.get(1).getHeight());
        
        dataFont = GameData.getResources().getFontForObject(dataFontInfo);
        
        boundingRect = new Rectangle2D.Double(location.x, location.y, images.get(NORMAL_SHIP).getWidth(), images.get(NORMAL_SHIP).getHeight());
        centerPoint = new Point2D.Double(location.x + images.get(NORMAL_SHIP).getWidth() / 2, location.y + images.get(NORMAL_SHIP).getHeight() / 2);
        
        selectionState = SelectionState.IDLE;
        accelerationVector = new Vector(0, 0);
        velocityVector = new Vector(0, 0);
        
        // for testing the 'testingrange'
        angularAcceleration = MAX_ANGULAR_ACCELERATION;
        angularVelocity = -MAX_ANGULAR_VELOCITY;
        
        testingRangeForSlowingDownAngle = Math.pow(angularVelocity, 2) / (2 * angularAcceleration);
        testingRangeForSlowingDownAngle -= (2.5 * (Math.abs(angularVelocity) / MAX_ANGULAR_VELOCITY));
        
        angularAcceleration = 0;
        angularVelocity = 0;
        
        System.out.println(testingRangeForSlowingDownAngle);
        
        faceAngle = 0;
        
        startingLocation = new Point2D.Double(location.x, location.y);
        
        testingRangeForSlowingDown = Math.abs(Math.pow(velocityVector.getMagnitude(), 2) / (2 * accelerationVector.getMagnitude()));
        testingRangeForSlowingDown -= (5 * Math.abs(velocityVector.getMagnitude()) / MAX_VELOCITY);
        System.out.println(testingRangeForSlowingDown);
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
    
    /*
    Some stuff for movement:
    The acceleration required to stop the ship's rotation given the distance to stop and and current velocity is:
    α = (-ω^2)/(2*Δθ) !!!!!
    where omega is the current velocity and delta theta is the distance to the target angle.
    
    How to test: see if the acceleration required at the angle is under the max acceleration
    
    This also works for linear velocity!!
    */
    
    public void update()
    {
        if (rotatingToTarget)
        {
            calculcateTargetAngle(targetPoint);
        }
        
        if (Calculator.centeredSignum(angularVelocity, targetAngularVelocity) !=
                Calculator.centeredSignum(angularVelocity + angularAcceleration, targetAngularVelocity))
        {
            angularAcceleration = 0;
            angularVelocity = targetAngularVelocity;
        }
        
        angularVelocity += angularAcceleration;

        faceAngle += angularVelocity;

        faceAngle = Calculator.normalizeAngle(faceAngle);
        
        accelerationVector.setDirectionAndMagnitude(accelerationVector.getMagnitude(), faceAngle);
        velocityVector.setDirectionAndMagnitude(velocityVector.getMagnitude(), faceAngle);
        targetVector = new Vector(new Point2D.Double(targetPoint.x - centerPoint.x, targetPoint.y - centerPoint.y));
        if (rotatingToTarget && Calculator.centeredSignum(faceAngle, targetAngle) != Calculator.centeredSignum(faceAngle + angularVelocity, targetAngle))
        {
            faceAngle = targetAngle;
            locked = true;
            System.out.println("locked");
            angularAcceleration = 0;
            angularVelocity = 0;
        }
        
        //System.out.println(Calculator.getDistance(location, targetPoint));
        if (movingToTarget && Calculator.getDistance(centerPoint, targetPoint) < 30)
        {
            velocityVector.setDirectionAndMagnitude(0, 0);
            accelerationVector.setDirectionAndMagnitude(0, 0);
            movingToTarget = false;
            rotatingToTarget = false;
            locked = false;
            angularAcceleration = 0;
            angularVelocity = 0;
        }
        
        if (!locked)
        {            
            if (velocityVector.getMagnitude() < targetVelocityMagnitude) // if we need to speed up
            {                
                if (velocityVector.add(accelerationVector).getMagnitude() > targetVelocityMagnitude)
                {
                    velocityVector.setDirectionAndMagnitude(targetVelocityMagnitude, faceAngle);
                    accelerationVector.setDirectionAndMagnitude(0, 0);
                }
                else
                {
                    velocityVector = velocityVector.add(accelerationVector);
                }
            }
            else if (velocityVector.getMagnitude() > targetVelocityMagnitude) // if we need to slow down
            {
                if (velocityVector.add(accelerationVector).getMagnitude() < targetVelocityMagnitude)
                {
                    velocityVector.setDirectionAndMagnitude(targetVelocityMagnitude, faceAngle);
                    accelerationVector.setDirectionAndMagnitude(0, 0);
                }
                else
                {
                    velocityVector = velocityVector.add(accelerationVector);
                }
            }
        }
        else
        {
            if (velocityVector.getMagnitude() < MAX_VELOCITY) // only gets called once, when the ship becomes locked
            {
                accelerationVector.setDirectionAndMagnitude(MAX_ACCELERATION, faceAngle);
                targetVelocityMagnitude = MAX_VELOCITY;
            }
            
            if (velocityVector.add(accelerationVector).getMagnitude() > targetVelocityMagnitude)
            {
                velocityVector.setDirectionAndMagnitude(targetVelocityMagnitude, faceAngle);
                accelerationVector.setDirectionAndMagnitude(0, 0);
            }
            else
            {
                velocityVector = velocityVector.add(accelerationVector);
            }
        }
                
        if (velocityVector.getMagnitude() < .01 && targetVelocityMagnitude == 0) // make sure the velocity doesnt become negative from slowing down
        {
            velocityVector.setDirectionAndMagnitude(0, 0);
            accelerationVector.setDirectionAndMagnitude(0, 0);
            endingLocation = location;
            //System.out.println("Distance: " + Calculator.getDistance(startingLocation, endingLocation));
        }
        
        if (velocityVector.getMagnitude() > MAX_VELOCITY)
        {
            velocityVector.setDirectionAndMagnitude(MAX_VELOCITY, faceAngle);
            accelerationVector.setDirectionAndMagnitude(0, 0);
        }
        
        
        
        updateState();
        //printStates();
        
        location.x += velocityVector.getComponents().getX();
        location.y += velocityVector.getComponents().getY();        
        
        centerPoint.setLocation(location.x + imageSize.getWidth() / 2, location.y + imageSize.getHeight() / 2);

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
        
        g2d.setColor(Color.CYAN);
        g2d.fillRect((int)targetPoint.x, (int)targetPoint.y, 4, 4);
        g2d.setColor(Color.GREEN);
        g2d.drawLine((int)centerPoint.x, (int)centerPoint.y, (int)targetPoint.x, (int)targetPoint.y);
        g2d.setColor(Color.RED);
        Point2D.Double p = Calculator.rotatePointAroundPoint(new Point2D.Double(centerPoint.x + 200, centerPoint.y), centerPoint, faceAngle);
        g2d.drawLine((int)centerPoint.x, (int)centerPoint.y, (int)p.x, (int)p.y);
        g2d.setFont(dataFont);
        g2d.drawString(String.valueOf((int)Calculator.getDistance(centerPoint, targetPoint)), (float)location.x - 20, (float)location.y - 20);
        //g2d.drawString(String.valueOf(targetVector.angleBetween(velocityVector)), 100, 100);
        g2d.drawString(String.valueOf(faceAngle) + "\u00b0", (float)location.x + 50, (float)location.y + 50);
        
        
        //g2d.draw(boundingRect);
        //g2d.fillRect((int)(imageSize.getWidth()), (int)(imageSize.getHeight()), 2, 2);
    }
    
    public void move(MovementCommand command)
    {
        // Close distance = move less, rotate more
        rotatingToTarget = true;
        movingToTarget = true;
        locked = false;
        targetPoint = new Point2D.Double(command.getDestination().x, command.getDestination().y);
        double angleDistance = calculcateTargetAngle(targetPoint);
        double linearDistance = Calculator.getDistance(targetPoint, centerPoint);
        
        // frist decide if the turn is a short distance
        boolean smallDistance = false;
        
        if (!moving())
        {
            smallDistance = linearDistance < 130;
            if (smallDistance)
            {
                accelerationVector.setDirectionAndMagnitude(MAX_ACCELERATION / 2, faceAngle);
                targetVelocityMagnitude = MAX_VELOCITY / 4;
            }
            else 
            {
                accelerationVector.setDirectionAndMagnitude(MAX_ACCELERATION, faceAngle);
                targetVelocityMagnitude = MAX_VELOCITY;
            }
        }
        else
        {
            smallDistance = linearDistance < 200;
            if (smallDistance)
            {
                if (velocityVector.getMagnitude() > MAX_VELOCITY / 4)
                {
                    accelerationVector.setDirectionAndMagnitude(-MAX_ACCELERATION, faceAngle);
                    targetVelocityMagnitude = MAX_VELOCITY / 4;
                }
                else 
                {
                    accelerationVector.setDirectionAndMagnitude(MAX_ACCELERATION / 2, faceAngle);
                    targetVelocityMagnitude = MAX_VELOCITY / 4;
                }
            }
            else
            {
                if (velocityVector.getMagnitude() < MAX_VELOCITY)
                {
                    accelerationVector.setDirectionAndMagnitude(MAX_ACCELERATION, faceAngle);
                    targetVelocityMagnitude = MAX_VELOCITY;
                }
            }
        }
        
        System.out.println("angle: " + angleDistance);
        System.out.println("distance: " + linearDistance + "\n");
    }
    
    // returns the distance to the target angleooo
    private double calculcateTargetAngle(Point2D.Double targetPoint)
    {
        targetAngle = Calculator.getAngleBetweenTwoPoints(centerPoint, targetPoint);
        double[] angleDistances = Calculator.getDistancesBetweenAngles(faceAngle, targetAngle);
        if (angleDistances[0] < angleDistances[1])
        {
//            System.out.println(angleDistances[0]);
            angularAcceleration = MAX_ANGULAR_ACCELERATION;
            targetAngularVelocity = MAX_ANGULAR_VELOCITY;
            return angleDistances[0];
        }
        else
        {
            //System.out.println(angleDistances[1]);
            angularAcceleration = -MAX_ANGULAR_ACCELERATION;
            targetAngularVelocity = -MAX_ANGULAR_VELOCITY;
            return angleDistances[1];
        }
    }
    
    private boolean moving()
    {
        return velocityVector.getMagnitude() != 0;
    }

    private void updateState()
    {
        if (Math.abs(velocityVector.getMagnitude()) > 0 && accelerationVector.getMagnitude() == 0)
        {
            movementState = MovementState.CONSTANT;
        }
        else if (accelerationVector.getMagnitude() < 0)
        {
            movementState = MovementState.SLOWING_DOWN;
        }
        else if (accelerationVector.getMagnitude() > 0)
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

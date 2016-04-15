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

    private State state;
    private final Rectangle2D.Double boundingRect;
    private final Point2D.Double centerPoint;
    
    private Vector velocityVector;
    private final int MAX_VELOCITY = 4;
    private Vector accelerationVector;
    private final double MAX_POSITIVE_ACCELERATION = .1;
    private final double MAX_NEGATIVE_ACCELERATION = -.2;
    private double faceAngle;
    
    private double targetAngle = 0;
    private Point2D.Double targetPoint = new Point2D.Double(-1, -1);
    private RotationDirection directionToRotate = RotationDirection.idle;
    
    
    public Ship(Point2D.Double location)
    {
        this.location = location;
        imagePaths.add("Resources/Ship.png");
        imagePaths.add("Resources/SelectedShip.png");
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
        
        imageSize.setSize(images.get(0).getWidth(), images.get(1).getHeight());
        
        boundingRect = new Rectangle2D.Double(location.x, location.y, images.get(NORMAL_SHIP).getWidth(), images.get(NORMAL_SHIP).getHeight());
        centerPoint = new Point2D.Double(location.x + images.get(NORMAL_SHIP).getWidth() / 2, location.y + images.get(NORMAL_SHIP).getHeight() / 2);
        
        state = State.idle;
        accelerationVector = new Vector(0, faceAngle);
        faceAngle = 0;
        velocityVector = new Vector(0, faceAngle);
    }
    
    enum RotationDirection
    {
        idle,
        positive,
        negative
    }
    
    enum State
    {
        idle,
        selected
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
        directionToRotate = angleDistances[0] < angleDistances[1] ? RotationDirection.positive : RotationDirection.negative;
    }
    
    public void update()
    {
        velocityVector = velocityVector.add(accelerationVector);
        
        if (!targetPoint.equals(new Point2D.Double(-1, -1)))
        {
            calculcateTargetAngle(targetPoint);
        }
        switch(directionToRotate)
        {
            case positive:
                faceAngle++;
                faceAngle = Calculator.normalizeAngle(faceAngle);
                
                if (Math.abs(faceAngle - targetAngle) <= 1)
                {
                    directionToRotate = RotationDirection.idle;
                    targetPoint.setLocation(-1, -1);
                }
                else
                {
                    directionToRotate = RotationDirection.positive;
                }
                
                break;
            case negative:
                faceAngle--;
                faceAngle = Calculator.normalizeAngle(faceAngle);
                if (Math.abs(faceAngle - targetAngle) <= 1)
                {
                    directionToRotate = RotationDirection.idle;
                    targetPoint.setLocation(-1, -1);
                }
                else
                {
                    directionToRotate = RotationDirection.negative;
                }
                
                break;
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
            case idle:
                g2d.drawImage(images.get(NORMAL_SHIP), 0, 0, null);
                break;
            case selected:
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

package armada;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * @author Michael Kieburtz
 */
public class Calculator 
{
    public static double CalcAngleMoveX(double angle) 
    {
        return Math.cos(Math.toRadians(angle));
    }

    public static double CalcAngleMoveY(double angle) 
    {
        return Math.sin(Math.toRadians(angle));
    }
    
    // converts an angle into [0, 360]
    public static double normalizeAngle(double angle)
    {
        if (angle > 360)
        {
            angle %= 360;
        }
        else if (angle < 0)
        {
            angle += 360;
        }
        
        return angle;
    }
    
    public static double getDistance(Point2D.Double pt1, Point2D.Double pt2)
    {
        double dx = Math.abs(pt2.x - pt1.x);
        double dy = Math.abs(pt2.y - pt1.y);
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
    
    // converts an angle in [0, 360] to and angle that makes sense the rotate method of AffineTransform     
    public static double convertAngleForAffineTransform(double angle)
    {
        return 360 - angle;
    }
    
    public static double getAngleBetweenTwoPoints(Point2D.Double pt1, Point2D.Double pt2)
    {
        double angle = 360 - (double) Math.toDegrees(Math.atan2(pt2.y - pt1.y, pt2.x - pt1.x));
        return normalizeAngle(angle);
    }
    
    public static Point compensateForBorders(Point p)
    {
        return new Point(p.x + GameData.getBorderSize().width, p.y + GameData.getBorderSize().height);
    }
    
    /*
    Using the formula:
    X = cx + (px - cx) * cos(theta) - (py - cy) * sin(theta)
    Y = cy + (px - cx) * sin(theta) + (py - cy) * cos(theta)
    */
    
    public static Point2D.Double rotatePointAroundPoint(Point2D.Double point, Point2D.Double rotationPoint, double angle)
    {
        double newX = rotationPoint.x + (point.x - rotationPoint.x) * Math.cos(Math.toRadians(360 - angle)) - (point.y - rotationPoint.y) * Math.sin(Math.toRadians(360 - angle));
        double newY = rotationPoint.y + (point.x - rotationPoint.x) * Math.sin(Math.toRadians(360 - angle)) + (point.y - rotationPoint.y) * Math.cos(Math.toRadians(360 - angle));
        
        return new Point2D.Double(newX, newY);
    }
    
    // gives the two possible angle changes to advance the current angle to the target angle
    // first element is if the instance rotates in the positive direction
    // second element is if the instance rotates in the negative direction
    public static double[] getDistancesBetweenAngles(double currentAngle, double targetAngle) 
    {
        if (targetAngle > currentAngle)
        {
            return new double[] {targetAngle - currentAngle, 360 - (targetAngle - currentAngle)};
            
        } else if (currentAngle > targetAngle)
        {
            return new double[] {360 - (currentAngle - targetAngle), currentAngle - targetAngle };
        }
        
        return new double[] {0.0, 360};
    }
}

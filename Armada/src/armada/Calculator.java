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
}

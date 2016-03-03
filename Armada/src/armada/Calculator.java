package armada;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * @author Michael Kieburtz
 */
public class Calculator 
{
    // converts an angle from [180, -180] to [0, 360]
    public static double normalizeAngle(double angle)
    {
        if (angle > 180 || angle < -180)
        {
            angle %= 180;
        }
        
        return angle < 0 ? angle += 360 : angle;
    }
    
    public static double getAngleBetweenTwoPoints(Point2D.Double pt1, Point2D.Double pt2)
    {
        double angle = (double) Math.toDegrees(Math.atan2(pt2.y - pt1.y, pt2.x - pt1.x));
        return normalizeAngle(angle);
    }
    
    public static Point compensateForBorders(Point p)
    {
        return new Point(p.x + GameData.getBorderSize().width, p.y + GameData.getBorderSize().height);
    }
}

package armada;

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
}

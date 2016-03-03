package armada;

import java.awt.Point;


/**
 * @author Michael Kieburtz
 */
public class MovementCommand 
{
    private final Point destination;
    
    public MovementCommand(Point destination) 
    {
        this.destination = destination;
    }
        
    public Point getDestination()
    {
        return destination;
    }
}

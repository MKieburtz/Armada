package armada;

import java.awt.geom.Point2D;

/**
 * @author 543021
 */
public class Vector 
{
    private double magnitude;
    private double direction;
    private Point2D.Double components;
    
    public Vector(double magnitude, double direction)
    {
        this.magnitude = magnitude;
        this.direction = direction;
    }
    
    public Vector(Point2D.Double components)
    {
        this.components = components;
    }
    
    private void setComponents()
    {
        components.x = magnitude * Math.cos(Math.toRadians(direction));
        components.y = magnitude * Math.sin(Math.toRadians(direction));
    }
    
    private void setDirectionAndMagnitude()
    {
        magnitude = Math.sqrt(Math.pow(components.x, 2) + Math.pow(components.y, 2));
        direction = Math.atan(components.y / components.x);
    }
}

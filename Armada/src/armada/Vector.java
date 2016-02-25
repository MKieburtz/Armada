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
        setComponents();
    }
    
    public Vector(Point2D.Double components)
    {
        this.components = components;
        setDirectionAndMagnitude();
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
    
    public Point2D.Double getComponents()
    {
        return components;
    }
    
    public Point2D.Double getDirectionAndMagnitude()
    {
        return new Point2D.Double(direction, magnitude);
    }
}

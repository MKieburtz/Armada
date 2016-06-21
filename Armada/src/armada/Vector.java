package armada;

import java.awt.geom.Point2D;

/**
 * @author Michael Kieburtz
 */
public class Vector 
{
    private double magnitude;
    private double direction;
    private Point2D.Double components = new Point2D.Double();
    
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
        components.x = magnitude * Math.cos(Math.toRadians(360 - direction));
        components.y = magnitude * Math.sin(Math.toRadians(360 - direction));
    }
    
    public void setComponents(Point2D.Double components)
    {
        this.components = components;
        setDirectionAndMagnitude();
    }
    
    private void setDirectionAndMagnitude()
    {
        magnitude = Math.sqrt(Math.pow(components.x, 2) + Math.pow(components.y, 2));
        direction = Calculator.normalizeAngle(Math.toDegrees(Math.atan2(components.y, components.x)));
    }
    
    public void setDirectionAndMagnitude(double magnitude, double direction)
    {
        this.magnitude = magnitude;
        this.direction = direction;
        setComponents();
    }
    
    public Point2D.Double getComponents()
    {
        return components;
    }
    
    public Point2D.Double getDirectionAndMagnitude()
    {
        return new Point2D.Double(direction, magnitude);
    }
    
    public Vector add(Vector other)
    {
        Vector newVector = new Vector(this.getComponents());
        Point2D.Double newComponents = newVector.getComponents();
        Point2D.Double otherComponents = other.getComponents();
        newVector.setComponents(new Point2D.Double(newComponents.x + otherComponents.x, newComponents.y + otherComponents.y));
        return newVector;
    }
}

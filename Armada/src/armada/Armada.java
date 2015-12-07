package armada;

/**
 * @author Michael Kieburtz
 */
public class Armada
{
    private ArmadaWindow window;
    private Resources resources;
            
    public Armada()
    {
        resources = new Resources();
        window = new ArmadaWindow();
    }
    
    public static void main(String[] args) 
    {
        new Armada();
    }
}

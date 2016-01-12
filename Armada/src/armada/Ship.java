package armada;

/**
 * @author Michael Kieburtz
 */
public class Ship extends GameEntity
{
    public Ship()
    {
        imagePaths.add("Resources/Ship.png");
        images.addAll(GameData.getResources().getImagesForObject(imagePaths));
    }
}

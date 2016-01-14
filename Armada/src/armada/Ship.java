package armada;

import java.awt.*;

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

    @Override
    public void draw(Graphics2D g2d) 
    {
        g2d.drawImage(images.get(0), 300, 300, null);
    }
}

package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.aet.valleyday.texture.Textures;

/***
 * Creates a new Shovel class 
 * 
 * This class extends an Item which is hidden beneath the Debris at first 
 * and only visible after Debris was removed by Player
 * 
 * 
 */

public class Shovel extends hiddenObject implements Item{

    public Shovel(int x, int y, GameMap map) {
        super(x, y, map);
        super.texture = Textures.SHOVEL;
    }

    /* If a player picks up the shovel after removing the debris, the shovel gets removed from the map.
     * The shovel clears debris quicker. The respective message is displayed on the screen.
     */
    @Override
    public String pickup(Player player) {
        super.map.removeItem((int) super.getX(),(int) super.getY());
        player.equipShovel(); // equip the shovel
        return "Shovel was picked up!";
    }

    
}

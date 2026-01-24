package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.texture.Textures;

public class Dynamite extends hiddenObject implements Item {

    /**
     * The Dynamite class extends the hiddenObject class and implements the Item interface.
     * The constructor takes the GameMap as a parameter and the textures variable from the Parent class hiddenObject.
     * @param x
     * @param y
     * @param map
     */
    public Dynamite(int x, int y, GameMap map) {
        super(x, y, map);
        super.texture = Textures.DYNAMITE;
       
    }

    @Override
    public String pickup(Player player) {
        super.map.removeItem((int) super.getX(),(int) super.getY());
        player.equipDynamite(); // pickup Dynamite
        return "Dynamite was picked up!";
    }
    
}

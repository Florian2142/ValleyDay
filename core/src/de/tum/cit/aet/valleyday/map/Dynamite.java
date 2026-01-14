package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.texture.Textures;

public class Dynamite extends hiddenObject implements Item {

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

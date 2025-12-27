package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.texture.Textures;

public class Fertilizer extends hiddenObject implements Item{

    public Fertilizer(int x, int y, GameMap map) {
        super(x, y, map);
        super.texture = Textures.FERTILIZER;
    }

    @Override
    public String pickup(Player player) {
        // Remove the item from the map
        super.map.removeItem((int) super.getX(),(int) super.getY());
        super.map.fertilizing(); // calls the fertilizer from the GameMap
        return "Fertilizer was picked up! All active Crops grew by one Stage.";
    
    }
    
}

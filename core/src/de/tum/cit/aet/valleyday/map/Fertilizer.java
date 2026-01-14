package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.texture.Textures;

public class Fertilizer extends hiddenObject implements Item{

    /**
     * The Fertilizer class extends the Parent class "hiddenObject" and implements the "Item" interface.
     * The constructor takes the x and y coordinate of the hiddenObject which also takes the GameMap as an argument.
     * It also initializes the texture variable insidfe the HiddenObject class with the textures for the fertilizer.
     * @param x
     * @param y
     * @param map
     */

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

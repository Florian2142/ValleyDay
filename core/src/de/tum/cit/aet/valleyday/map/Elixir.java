
package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.screen.GameScreen;
import de.tum.cit.aet.valleyday.texture.Textures;

public class Elixir extends hiddenObject implements Item{

    /**
     * The Elixir class extends the hiddenObject class and implements the Item interface.
     * The constructor takes the GameMap as a parameter and the textures variable from the Parent class hiddenObject.
     * @param x
     * @param y
     * @param map
     */
    public Elixir(int x, int y, GameMap map) {
        super(x, y, map);
        super.texture = Textures.ELIXIR;
    }

    @Override
    public String pickup(Player player) {
        
    /* If a player picks up the Elixir after removing the debris, the Elixir gets removed from the map.
     * If the player has less than 3 hearts, 1 heart is added to the players life and displayed as a message on the screen.
     */    
        super.map.removeItem((int) super.getX(),(int) super.getY());
        player.setHealth(player.getHealth() + 1);
        return "Elixir was picked up!, increased life by 1!";
    }
    
    
}

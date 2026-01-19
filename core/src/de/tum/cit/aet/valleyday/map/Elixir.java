
package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.screen.GameScreen;
import de.tum.cit.aet.valleyday.texture.Textures;

public class Elixir extends hiddenObject implements Item{

    public Elixir(int x, int y, GameMap map) {
        super(x, y, map);
        super.texture = Textures.ELIXIR;
    }

    @Override
    public String pickup(Player player) {
        
        super.map.removeItem((int) super.getX(),(int) super.getY());
        player.setHealth(player.getHealth() + 1);
        return "Elixir was picked up!, increased life by 1!";
    }
    
    
}

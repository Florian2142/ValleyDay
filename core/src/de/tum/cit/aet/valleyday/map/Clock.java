package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.screen.GameScreen;
import de.tum.cit.aet.valleyday.texture.Textures;

public class Clock extends hiddenObject implements Item{

    /**
     * The clock class extends the hiddenObject class and implements the Item interface.
     * The constructor takes the GameMap as a parameter and the textures variable from the Parent class hiddenObject.
     * @param x
     * @param y
     * @param map
     */
    public Clock(int x, int y, GameMap map) {
        super(x, y, map);
        super.texture = Textures.TIMER;
    }

    @Override
    public String pickup(Player player) {
        
        super.map.removeItem((int) super.getX(),(int) super.getY());
        GameScreen currScreen = ((GameScreen)map.getGame().getScreen());
        currScreen.setRemainingTime(currScreen.getRemainingTime() + 30f);
        return "Clock was picked up!";
    }
    
    
}

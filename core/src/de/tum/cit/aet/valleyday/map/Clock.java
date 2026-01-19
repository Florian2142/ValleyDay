package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.screen.GameScreen;
import de.tum.cit.aet.valleyday.texture.Textures;

public class Clock extends hiddenObject implements Item{

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

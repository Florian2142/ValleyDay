package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.Game;

/**
 * Implements an Interface which will destruct objects
 * 
 * Objects like Debris are destructible and hence must be removed from the map
 * 
 */
public interface Destructible {

    public boolean isDestructible();

    /**
     * destructs the destructible :D 
     * @param gamemap
     * @param damageif player has item for removing faster then increase damage
     */

    public void destruct(GameMap gamemap, int damage);


    
}

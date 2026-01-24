package de.tum.cit.aet.valleyday.map;

/**
 * Interface for Items which lie on the map below the debris.
 * The pickup function from the Interface Item is called when the player walks over an item.
 */

@FunctionalInterface
public interface Item {
    
    public abstract String pickup(Player player);

}

package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.texture.Drawable;

/**
 * Contract for all living entities that can be rendered on the map.
 */
public interface Wildlife extends Drawable {

    /**
     * Marks this entity as dead and triggers any cleanup logic.
     */
    void kill();

    /**
     * Indicates whether the entity should be removed from the map.
     *
     * @return true if it is safe to remove the entity
     */
    boolean isRemovable();

    /**
     * Checks whether the entity is dead.
     *
     * @return true if the entity is dead
     */
    boolean isDead();
}

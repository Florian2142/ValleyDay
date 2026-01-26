package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.texture.Drawable;


/**
 * Interface Chicken
 * Behavior contract for chicken entities.
 * scurry(): react to player presence.
 * scurryAway(): move away from the player.
 */
public interface Chicken extends Drawable{
    
    // Run away from the player. -> SCARED
    public void scurryAway(float playerX, float playerY); 
    
    // Trigger scared behavior when the player is nearby.
    public void scurry(float playerX, float playerY);

    // Per-frame update for movement and state.
    public void tick(float frameTime, GameMap map);

    // Current position.
    public float getX();
    public float getY();

    // Mark the chicken as shocked.
    public void setShocked();
}

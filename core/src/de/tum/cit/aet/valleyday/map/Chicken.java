package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.texture.Drawable;


/******
 * Interface Chicken 
 * Implements the scurry function and scurryAway fuction for each chicken.
 * The scurry Function tells the chicken where the player stands, sets the isScared fuction to "true".
 * The scurryAway function enables the chicken to run away.
 * 
 * */
public interface Chicken extends Drawable{
    
    public void scurryAway(float playerX, float playerY); 
    
    public void scurry(float playerX, float playerY);

    public void tick(float frameTime, GameMap map);

    public float getX();
    public float getY();

    public void setShocked(); // sets the chicken in a schocked mode
}

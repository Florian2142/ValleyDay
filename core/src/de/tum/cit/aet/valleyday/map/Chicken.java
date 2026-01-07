package de.tum.cit.aet.valleyday.map;

/******
 * Interface Chicken 
 * Implements the scurry function and scurryAway fuction for each chicken.
 * The scurry Function tells the chicken where the player stands, sets the isScared fuction to "true".
 * The scurryAway function enables the chicken to run away.
 * 
 * */
public interface Chicken{
    
    public void scurryAway(float playerX, float playerY); 
    
    public void scurry(float playerX, float playerY);

    public void tick(float frameTime, GameMap map);

    public float getX();
    public float getY();
}

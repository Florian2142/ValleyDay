package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Animations;



/**
 * Creates a Chicken Class which is ALWAYS hungry (nonsatisfiable) and tries to eat CROP
 */
public class Chicken extends Entity {

    private boolean moving = false;

    private float moveTimer = 0f;
    private float timeToNextMove = 2.0f; // Chicken thinks every 2 seconds

    private float yVelocity = 0;
    private float xVelocity = 0;

    
    public Chicken(World world, float x, float y) {
        super(world, x, y);
    }



    /******
     * 
     * TESTING CURRENTLY NOT FINISHED -> We will BUILD a A* Algorithm
     * 
     * @param frameTime time which elapses
     * @param map 
     * @return nothing
     */

    public void tick(float frameTime, GameMap map) {

        this.moveTimer += frameTime;

        int currX = Math.round(getX()); // retrieve the currX always, reduces the function calls
        int currY = Math.round(getY()); // same for Y


        if (timeToNextMove <= 0) {

            // lets take a random number TESTING
            int randomDir = MathUtils.random(0 , 4);

            // SEt the current velocity to 0
            xVelocity = 0;
            yVelocity = 0;

            moving = true;

            switch (randomDir) {
                case 0: // UP
                    currDirection = Direction.UP;
                    yVelocity++;
                    break;
                case 1: // DOWN ,
                    currDirection = Direction.DOWN;
                    yVelocity--;
                    break;
                case 2: // LEFT 
                    currDirection = Direction.LEFT;
                    xVelocity--;
                    break;
                case 3: // RIGHT
                    currDirection = Direction.RIGHT;
                    xVelocity++;
                    break;
                default: 
                    // do nothing
                    moving = false;
                    break;
            
            }       

            // WE NEED TO RESET THE TIMER for next Brainmove
            timeToNextMove = 2.0f;

            
        }


        // decrement the time in order for brainpower restorage -> Move requires loads of energy
        timeToNextMove -= frameTime;



        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }



    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the frame of the walk down animation that corresponds to the current time.
    
        // if the player is not harvesting he can move
        if (isMoving()) {
            return  Animations.CHICKEN_WALKING.getKeyFrame(this.moveTimer, true);
        }
        else {
            return  Animations.CHICKEN_NOT_WALKING.getKeyFrame(this.moveTimer, true);    
        }
    };





    public boolean isMoving() {
        return moving;
    }


    
    
    
}

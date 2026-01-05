package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Animations;
import de.tum.cit.aet.valleyday.map.Entity.Direction;
import de.tum.cit.aet.valleyday.pathfinding.*;

import java.util.*;



/**
 * Creates a Chicken Class which is ALWAYS hungry (nonsatisfiable) and tries to eat CROP
 */
public class Chicken extends Entity {

    private boolean moving = false;
    private boolean isEating = false;

    private float moveTimer = 0f;
    private float eatTimer = 0f;
    private float timeToNextMove = 2.0f; // Chicken thinks every 2 seconds

    private float yVelocity = 0;
    private float xVelocity = 0;
    private boolean isScurrying;
    private float scurryTimer;
    private float playerX;
    private float playerY;

    /** GPS to next Crop */
    private List<GridNode> highwayToHeaven = new LinkedList<>();

    private GridNode start;
    private GridNode goal;
    private GridNode nextMove;

    private int offsetX;
    private int offsetY;

    private Crop goalCrop;


    
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

            System.out.println("Well this works potenically");


            if (highwayToHeaven == null || highwayToHeaven.size() == 0) {

                System.out.println("Currently its null");

                goalCrop = map.randomCrop();

                if (goalCrop != null) {

                    start = new GridNode(currX, currY, 0, 0, null);
                    goal  = new GridNode((int) goalCrop.getX(), (int) goalCrop.getY(), 0, 0, null);

                    highwayToHeaven = Gps.findPath(start, goal, map);

                    if (highwayToHeaven != null) {
                        System.out.println("Path found! Steps: " + highwayToHeaven.size());
                    }
                }
                else {
                    highwayToHeaven = null;
                }

                

                
            }

            if (highwayToHeaven == null || highwayToHeaven.size() == 0) {
                // lets take a random number TESTING
                int randomDir = MathUtils.random(0 , 4);

                // Set the current velocity to 0
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
                }

            // WE NEED TO RESET THE TIMER for next Brainmove
            timeToNextMove = 2.0f;

            
        }

        /** The smart movement of the chicken */

        if (highwayToHeaven != null && highwayToHeaven.size() > 0) {

                nextMove = highwayToHeaven.get(0);

                System.out.println("The current TileX which is better: " + nextMove.getX());
                System.out.println("The current TileX which is better: " + nextMove.getY());

                offsetX = nextMove.getX();
                offsetY = nextMove.getY();

                xVelocity = offsetX - currX;
                yVelocity = offsetY - currY;

                if      (xVelocity > 0) currDirection = Direction.RIGHT;
                else if (xVelocity < 0) currDirection = Direction.LEFT;
                else if (yVelocity > 0) currDirection = Direction.UP;
                else if (yVelocity < 0) currDirection = Direction.DOWN;

                // set moving to true as always
                moving = true;

                if (currX == offsetX && currY == offsetY) {

                        highwayToHeaven.remove(0);
                    }           
            }
        
        

        // Make the chicken eat the crop
        if (map.getCrop(currX, currY) != null) {
            eatTimer = 30f;
            this.isEating = true;
        }
        map.eatCrop(currX, currY);

        if (isScurrying && scurryTimer >= 0) {
            scurryAway(playerX, playerY);
        }
        else {
            isScurrying = false;
        }

        // decrement the time in order for brainpower restorage -> Move requires loads of energy
        timeToNextMove -= frameTime;
        eatTimer--;


        if (eatTimer <= 0) {isEating = false;} // reset the eating animation
        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }

    public void scurryAway(float playerX, float playerY) {
        
        float sprint = 10f;
        this.xVelocity = (this.getX() - playerX) * sprint;
        this.yVelocity = (this.getY() - playerY) * sprint;
    }
    public void scurry(float playerX, float playerY) {
        this.isScurrying = true;
        this.scurryTimer = 0.5f;

        this.playerX = playerX;
        this.playerY = playerY;
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the frame of the walk down animation that corresponds to the current time.
    
        // if the player is not harvesting he can move
        if (isEating()) {
            return Animations.CHICKEN_EATING.getKeyFrame(this.eatTimer, false);

        }
        else if (isMoving()) {
            return  Animations.CHICKEN_WALKING.getKeyFrame(this.moveTimer, true);
        }
        else {
            return  Animations.CHICKEN_NOT_WALKING.getKeyFrame(this.moveTimer, true);    
        }
    };





    public boolean isMoving() {
        return moving;
    }



    public boolean isEating() {
        return isEating;
    }



    
    
    
}
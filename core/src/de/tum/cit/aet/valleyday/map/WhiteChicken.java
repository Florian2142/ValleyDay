package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Animations;
import de.tum.cit.aet.valleyday.map.Entity.Direction;
import de.tum.cit.aet.valleyday.pathfinding.*;

import java.util.*;



/**
 * The WhiteChicken class follows random movement different to the BrownChiken class.
 * The WhiteChicken class extends the entity super class which represent a living object.
 * It also implements the chicken interface. the interface contains the scurry and scurryAway function 
 * which is implemented for all forms of chicken.
 */
public class WhiteChicken extends Entity implements Chicken{

    // chicken is not moving, is standing.
    private boolean moving = false;
    // chicken is not eating.
    private boolean isEating = false;

    // As soon as the velocity set, the chicken starts to move.
    private float moveTimer = 0f;
    // Chicken start immidietly eating, when being on the crop tile.
    private float eatTimer = 0f;
    private float timeToNextMove = 2.0f; // Chicken thinks every 2 seconds

    // Velocity in the y-Direction.
    private float yVelocity = 0;
    // Velocity in the x-Direction.
    private float xVelocity = 0;
    // contains the value of wether the chicken is scurrying.
    private boolean isScurrying;
    private float scurryTimer;
    private float scurryAnimTimer;
    private float schocked;
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


    /**
     * The WhiteChicken constructor contains the parameters world, the x and y positions.
     * It initializes the parent class World with the super keyword.
     * Same as in the BrownChicken constructor.
     * @param world
     * @param x
     * @param y
     */
    public WhiteChicken(World world, float x, float y) {
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

            // Set the current velocity to 0
            xVelocity = 0;
            yVelocity = 0;

            System.out.println("Well this works potenically");

                // lets take a random number TESTING
                int randomDir = MathUtils.random(0 , 4);

                

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

             

        // Make the chicken eat the crop
        // If the chicken found a crop, the chicken eats it.
        if (map.getCrop(currX, currY) != null) {
            eatTimer = 30f;
            this.isEating = true;
        }
        map.eatCrop(currX, currY);

        /**
         *If the chicken is on the tile that is one next to or before the player, the chicken knows the 
         *x and y postion of the player and isScurrying is true. 
         */
        if (isScurrying && scurryTimer >= 0) {
            scurryAway(playerX, playerY);
        }
        else {
            isScurrying = false;
            scurryAnimTimer = 0f;
        }

        // decrement the time in order for brainpower restorage -> Move requires loads of energy
        timeToNextMove -= frameTime;
        if (eatTimer > 0) {
            eatTimer--;
        }
        if (scurryTimer > 0){
            scurryTimer-=frameTime;
        }
        if (isScurrying) {
            scurryAnimTimer += frameTime;
        }
        if (schocked > 0) {
            schocked -= frameTime;
        }


        if (eatTimer <= 0) {isEating = false;} // reset the eating animation

        if (Math.abs(xVelocity) > 0.1f) {
            if (xVelocity > 0) {
                currDirection = Direction.RIGHT;
            } else {
                currDirection = Direction.LEFT;
            }
        }

        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }

    /**
     * The scurryAway function of the chicken, takes the x and y positions of the player as arguments.
     *When true, the x and y velocity is calculated by the current tile the chicken is standing on 
     *and the postion of the player multiplied by the sprint speed of 10. The chicken moves and sprints away.
     */
    public void scurryAway(float playerX, float playerY) {
        
        float diffX = this.getX() - playerX;
        float diffY = this.getY() - playerY;

        float angle = MathUtils.atan2(diffY, diffX);

        // Constant sprint speed (e.g., 5 meters per second)
        float sprintSpeed = 3f; 
        
        // Set velocity using simple trigonometry
        if (schocked <= 0) {
            this.xVelocity = MathUtils.cos(angle) * sprintSpeed;
            this.yVelocity = MathUtils.sin(angle) * sprintSpeed;
        }
        else {
            this.xVelocity = 0;
            this.yVelocity = 0;
        }
    }
    /**The scurry function takes the position of the player in the x and y direction. 
    // If the player is on tile before, behind, above or below the chicken, isScuyying is set to true and called 
    // in the if statement that checkes for wether isScurrying is true.*/
    public void scurry(float playerX, float playerY) {
        this.isScurrying = true;
        this.scurryTimer = 2f;
        this.scurryAnimTimer = 0f;

        this.playerX = playerX;
        this.playerY = playerY;

        // This stops it from walking back to the old path after running away.
        this.highwayToHeaven = null; 
        this.goalCrop = null;
        
        // Optional: Make it wait a moment before picking a new target after panicking
        this.timeToNextMove = 1.0f;
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the frame of the walk down animation that corresponds to the current time.
    
        // if the player is not harvesting he can move
        if (isEating()) {
            if (currDirection == Direction.LEFT) {
                return Animations.WHITE_CHICKEN_EATING_LEFT.getKeyFrame(this.eatTimer, false);
            }
            return Animations.WHITE_CHICKEN_EATING.getKeyFrame(this.eatTimer, false);

        }
        else if (isScurrying()) {
            if (currDirection == Direction.LEFT) {
                if (scurryAnimTimer <= Animations.WHITE_CHICKEN_SCARED_LEFT.getAnimationDuration()) {
                    return Animations.WHITE_CHICKEN_SCARED_LEFT.getKeyFrame(scurryAnimTimer, false);
                }
                return Animations.WHITE_CHICKEN_WALKING_LEFT.getKeyFrame(scurryAnimTimer, true);
            }
            if (scurryAnimTimer <= Animations.WHITE_CHICKEN_SCARED.getAnimationDuration()) {
                return Animations.WHITE_CHICKEN_SCARED.getKeyFrame(scurryAnimTimer, false);
            }
            return Animations.WHITE_CHICKEN_WALKING.getKeyFrame(scurryAnimTimer, true);
        }
        else if (isMoving()) {
            if (currDirection == Direction.LEFT) {
                return  Animations.WHITE_CHICKEN_WALKING_LEFT.getKeyFrame(this.moveTimer, true);
            }
           
            return  Animations.WHITE_CHICKEN_WALKING.getKeyFrame(this.moveTimer, true);
        
        }
        else {
            if (currDirection == Direction.LEFT) {
                return  Animations.WHITE_CHICKEN_NOT_WALKING_LEFT.getKeyFrame(this.moveTimer, true);   
            }
            return  Animations.WHITE_CHICKEN_NOT_WALKING.getKeyFrame(this.moveTimer, true);    
        }
    };





    public boolean isMoving() {
        return moving;
    }



    public boolean isEating() {
        return isEating;
    }



    public void setMoving(boolean moving) {
        this.moving = moving;
    }



    public void setEating(boolean isEating) {
        this.isEating = isEating;
    }



    public float getMoveTimer() {
        return moveTimer;
    }



    public void setMoveTimer(float moveTimer) {
        this.moveTimer = moveTimer;
    }



    public float getEatTimer() {
        return eatTimer;
    }



    public void setEatTimer(float eatTimer) {
        this.eatTimer = eatTimer;
    }



    public float getTimeToNextMove() {
        return timeToNextMove;
    }



    public void setTimeToNextMove(float timeToNextMove) {
        this.timeToNextMove = timeToNextMove;
    }



    public float getyVelocity() {
        return yVelocity;
    }



    public void setyVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }



    public float getxVelocity() {
        return xVelocity;
    }



    public void setxVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }



    public boolean isScurrying() {
        return isScurrying;
    }



    public void setScurrying(boolean isScurrying) {
        this.isScurrying = isScurrying;
    }



    public float getScurryTimer() {
        return scurryTimer;
    }



    public void setScurryTimer(float scurryTimer) {
        this.scurryTimer = scurryTimer;
    }



    public float getPlayerX() {
        return playerX;
    }



    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }



    public float getPlayerY() {
        return playerY;
    }



    public void setPlayerY(float playerY) {
        this.playerY = playerY;
    }



    public List<GridNode> getHighwayToHeaven() {
        return highwayToHeaven;
    }



    public void setHighwayToHeaven(List<GridNode> highwayToHeaven) {
        this.highwayToHeaven = highwayToHeaven;
    }



    public GridNode getStart() {
        return start;
    }



    public void setStart(GridNode start) {
        this.start = start;
    }



    public GridNode getGoal() {
        return goal;
    }



    public void setGoal(GridNode goal) {
        this.goal = goal;
    }



    public GridNode getNextMove() {
        return nextMove;
    }



    public void setNextMove(GridNode nextMove) {
        this.nextMove = nextMove;
    }



    public int getOffsetX() {
        return offsetX;
    }



    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }



    public int getOffsetY() {
        return offsetY;
    }



    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }



    public Crop getGoalCrop() {
        return goalCrop;
    }



    public void setGoalCrop(Crop goalCrop) {
        this.goalCrop = goalCrop;
    }



    public float getSchocked() {
        return schocked;
    }


    @Override
    public void setShocked() {
        // TODO Auto-generated method stub
        this.schocked = 1f;
    }

    


    
    
    
}

package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Animations;
import de.tum.cit.aet.valleyday.map.Entity.Direction;
import de.tum.cit.aet.valleyday.pathfinding.*;

import java.util.*;



/**
 * The Brown Chicken class implements chickens that are distinct from the white chickens. They don't move randomly. 
 * They use an algorithm that finds the quickest way to to crops to eat it. 
 * The BrownChicken class extends the entity super class which represent a living object.
 * It also implements the chicken interface. the interface contains the scurry and scurryAway function 
 * which is implemented for all forms of chicken.
 */
public class BrownChicken extends Entity implements Chicken{

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
    private float catchBreath;
    private float shocked;
    private float playerX;
    private float playerY;

    private static final float NORMAL_SPEED = 0.6f;

    /** GPS to next Crop */
    private List<GridNode> highwayToHeaven = new LinkedList<>();

    private GridNode start;
    private GridNode goal;
    private GridNode nextMove;

    private int offsetX;
    private int offsetY;

    private Crop goalCrop;

    private GameMap map;

    private boolean bool = false;


    /**
     * The BrownChicken constructor contains the parameters world, the x and y positions.
     * It initializes the parent class World with the super keyword.
     * @param world
     * @param x
     * @param y
     */
    public BrownChicken(World world, float x, float y) {
        super(world, x, y);
    }



    /******
     * 
     * Tick method handles the pathfinding, the movement and the physics inside the world.
     * TESTING CURRENTLY NOT FINISHED -> We will BUILD a A* Algorithm
     * 
     * @param frameTime time which elapses
     * @param map 
     * @return nothing
     */

    public void tick(float frameTime, GameMap map) {

        this.moveTimer += frameTime;

        if (!bool) {
            this.map = map;
            bool = true;
        }
        

        

        int currX = Math.round(getX()); // retrieve the currX always, reduces the function calls
        int currY = Math.round(getY()); // same for Y

        // right before next move
        if (timeToNextMove <= 0) {

            // Set the current velocity to 0
            xVelocity = 0;
            yVelocity = 0;

            System.out.println("Well this works potentially");

            //Checks if the path list (highwayToHeaven) is null or empty.
            if (highwayToHeaven == null || highwayToHeaven.size() == 0) {

                System.out.println("Currently its null");

                // It asks the map for a random crop.
                goalCrop = map.randomCrop();

                if (goalCrop != null) {
                    // Creates a start node and a end note.
                    start = new GridNode(currX, currY, 0, 0, null);
                    goal  = new GridNode((int) goalCrop.getX(), (int) goalCrop.getY(), 0, 0, null);
                    // Searches for the best route based on the GPS Class.
                    highwayToHeaven = Gps.findPath(start, goal, map);

                    if (highwayToHeaven != null) {
                        System.out.println("Path found! Steps: " + highwayToHeaven.size());
                    }
                }
                else {
                    highwayToHeaven = null;
                }

                

                
            }

            if (highwayToHeaven == null || highwayToHeaven.isEmpty()) {
                // lets take a random number TESTING
                int randomDir = MathUtils.random(0 , 4);

                // Set the current velocity to 0
                xVelocity = 0;
                yVelocity = 0;

                moving = true;
                // 
                switch (randomDir) {
                    case 0: // UP
                        currDirection = Direction.UP;
                        yVelocity = NORMAL_SPEED;
                        break;
                    case 1: // DOWN ,
                        currDirection = Direction.DOWN;
                        yVelocity = -NORMAL_SPEED;
                        break;
                    case 2: // LEFT 
                        currDirection = Direction.LEFT;
                        xVelocity = -NORMAL_SPEED;
                        break;
                    case 3: // RIGHT
                        currDirection = Direction.RIGHT;
                        xVelocity = NORMAL_SPEED;
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

        if (highwayToHeaven != null && highwayToHeaven.size() > 0 && isScurrying != true && catchBreath <= 0) {
                // When the chicken has a path, it looks at first node in the List.
                nextMove = highwayToHeaven.get(0);

                System.out.println("The current TileX which is better: " + nextMove.getX());
                System.out.println("The current TileX which is better: " + nextMove.getY());

                offsetX = nextMove.getX();
                offsetY = nextMove.getY();
                
                // The velocity is calculated by taking the difference of the target tile (offset)
                // and the current tile currX.
                xVelocity = (offsetX - currX) * NORMAL_SPEED;
                yVelocity = (offsetY - currY) * NORMAL_SPEED;

                // Based on wether the difference is positive or negative, in the x-Direction, 
                // the Chicken moves to the right or left direction respectively.
                if      (xVelocity > 0) currDirection = Direction.RIGHT;
                else if (xVelocity < 0) currDirection = Direction.LEFT;
                // The same concept applies to the y-Direction.
                else if (yVelocity > 0) currDirection = Direction.UP;
                else if (yVelocity < 0) currDirection = Direction.DOWN;

                // set moving to true as always
                moving = true;

                if (currX == offsetX && currY == offsetY) {

                        highwayToHeaven.remove(0);
                    }           
            }
        
        

        // Make the chicken eat the crop
        // If the chicken found a crop, the chicken eats it.
        if (map.getCrop(currX, currY) != null) {
            eatTimer = 30f;
            this.isEating = true;
        }
        map.eatCrop(currX, currY);

        // If the chicken is on the tile that is one next to or before the player, the chicken knows the 
        // x and y postion of the player and isScurrying is true. 
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
        if (shocked > 0) {
            shocked -= frameTime;
        }
        if (catchBreath >= 0) {
            catchBreath-=frameTime;
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



    // The scurryAway function of the chicken, takes the x and y positions of the player as arguments.
    // When true, the x and y velocity is calculated by the current tile the chicken is standing on 
    // and the postion of the player multiplied by the sprint speed of 10. The chicken moves and sprints away.
    public void scurryAway(float playerX, float playerY) {

        float diffX = this.getX() - playerX;
        float diffY = this.getY() - playerY;

        float angle = MathUtils.atan2(diffY, diffX);

        // Constant sprint speed (e.g., 5 meters per second)
        float sprintSpeed = 3f; 
        
        // Set velocity using simple trigonometry
        if (shocked <= 0) {
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
        /** WE WILL CHANGE THIS VALUES ACCORDING TO DIFFICULTY */

        // Difficulty Settings
        String difficulty = map.getDifficulty();
        
        // deciding how fast the chicken should be
        switch (difficulty) {
            case "TUM":
                this.scurryTimer = 1.185f;
                this.catchBreath = 1.5f;
                break;
            case "Hard":
                this.scurryTimer = 1.5f;
                this.catchBreath = 2f;
                break;
            case "Medium":
            default: 
                this.scurryTimer = 2f;
                this.catchBreath = 2.5f;
                break;
        }
        
        this.scurryAnimTimer = 0f;

        // Note that we must erase former memory of the optimal solution.
        this.highwayToHeaven = null; 
        this.goalCrop = null;

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
                return Animations.BROWN_CHICKEN_EATING_LEFT.getKeyFrame(this.eatTimer, false);
            }
            return Animations.BROWN_CHICKEN_EATING.getKeyFrame(this.eatTimer, false);

        }
        /* Used a quite different approach here, such that the animation time goes aslong as the chicken is really scared */
        else if (isScurrying()) {
            if (currDirection == Direction.LEFT) {
                if (scurryAnimTimer <= Animations.BROWN_CHICKEN_SCARED_LEFT.getAnimationDuration()) {
                    return Animations.BROWN_CHICKEN_SCARED_LEFT.getKeyFrame(scurryAnimTimer, false);
                }
                return Animations.BROWN_CHICKEN_WALKING_LEFT.getKeyFrame(scurryAnimTimer, true);
            }
            if (scurryAnimTimer <= Animations.BROWN_CHICKEN_SCARED.getAnimationDuration()) {
                return Animations.BROWN_CHICKEN_SCARED.getKeyFrame(scurryAnimTimer, false);
            }
            return Animations.BROWN_CHICKEN_WALKING.getKeyFrame(scurryAnimTimer, true);
        }
        else if (isMoving()) {
            if (currDirection == Direction.LEFT) {
                return  Animations.BROWN_CHICKEN_WALKING_LEFT.getKeyFrame(this.moveTimer, true);
            }
            return  Animations.BROWN_CHICKEN_WALKING.getKeyFrame(this.moveTimer, true);
        }
        else {
            if (currDirection == Direction.LEFT) {
                return  Animations.BROWN_CHICKEN_NOT_WALKING_LEFT.getKeyFrame(this.moveTimer, true);
            }
            return  Animations.BROWN_CHICKEN_NOT_WALKING.getKeyFrame(this.moveTimer, true);    
        }
    };




// --- Getters & Setters ---
    public boolean isMoving() { return moving; }
    public void setMoving(boolean moving) { this.moving = moving; }
    public boolean isEating() { return isEating; }
    public void setEating(boolean isEating) { this.isEating = isEating; }
    public float getMoveTimer() { return moveTimer; }
    public void setMoveTimer(float moveTimer) { this.moveTimer = moveTimer; }
    public float getEatTimer() { return eatTimer; }
    public void setEatTimer(float eatTimer) { this.eatTimer = eatTimer; }
    public float getTimeToNextMove() { return timeToNextMove; }
    public void setTimeToNextMove(float timeToNextMove) { this.timeToNextMove = timeToNextMove; }
    public float getyVelocity() { return yVelocity; }
    public void setyVelocity(float yVelocity) { this.yVelocity = yVelocity; }
    public float getxVelocity() { return xVelocity; }
    public void setxVelocity(float xVelocity) { this.xVelocity = xVelocity; }
    public boolean isScurrying() { return isScurrying; }
    public void setScurrying(boolean isScurrying) { this.isScurrying = isScurrying; }
    public float getScurryTimer() { return scurryTimer; }
    public void setScurryTimer(float scurryTimer) { this.scurryTimer = scurryTimer; }
    public float getPlayerX() { return playerX; }
    public void setPlayerX(float playerX) { this.playerX = playerX; }
    public float getPlayerY() { return playerY; }
    public void setPlayerY(float playerY) { this.playerY = playerY; }
    public List<GridNode> getHighwayToHeaven() { return highwayToHeaven; }
    public void setHighwayToHeaven(List<GridNode> highwayToHeaven) { this.highwayToHeaven = highwayToHeaven; }
    public GridNode getStart() { return start; }
    public void setStart(GridNode start) { this.start = start; }
    public GridNode getGoal() { return goal; }
    public void setGoal(GridNode goal) { this.goal = goal; }
    public GridNode getNextMove() { return nextMove; }
    public void setNextMove(GridNode nextMove) { this.nextMove = nextMove; }
    public int getOffsetX() { return offsetX; }
    public void setOffsetX(int offsetX) { this.offsetX = offsetX; }
    public int getOffsetY() { return offsetY; }
    public void setOffsetY(int offsetY) { this.offsetY = offsetY; }
    public Crop getGoalCrop() { return goalCrop; }
    public void setGoalCrop(Crop goalCrop) { this.goalCrop = goalCrop; }
    public float getShocked() { return shocked; }
    public void setShocked() { this.shocked = 1f; }
}
    

    
    
    


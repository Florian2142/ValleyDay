package de.tum.cit.aet.valleyday.map;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.pathfinding.Gps;
import de.tum.cit.aet.valleyday.pathfinding.GridNode;
import de.tum.cit.aet.valleyday.texture.Animations;

public class Spider extends Entity implements Wildlife{

    private boolean moving = false; 
    
    // As soon as the velocity set, the chicken starts to move.
    private float moveTimer = 0f;
    
    private float timeToNextMove = 2.0f; // Chicken thinks every 2 seconds

    // Velocity in the y-Direction.
    private float yVelocity = 0;
    // Velocity in the x-Direction.
    private float xVelocity = 0;
    
    private Player player;
    

    private static final float NORMAL_SPEED = 0.6f;

    /** GPS to next Crop */
    private List<GridNode> highwayToHeaven = new LinkedList<>();

    
    private int offsetX;
    private int offsetY;


    private GameMap map;

    private boolean bool = false;


    /**
     * The Spider constructor contains the parameters world, the x and y positions.
     * It initializes the parent class World with the super keyword.
     * @param world
     * @param x
     * @param y
     */
    public Spider(World world, float x, float y) {
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

        
        // right before next move
        if (timeToNextMove <= 0) {
            calculatePath(map);
            timeToNextMove = 120;
        }
        timeToNextMove--;

        followPath();

        
        if (Math.abs(xVelocity) > 0.1f) {
            if (xVelocity > 0) {
                currDirection = Direction.RIGHT;
            } else {
                currDirection = Direction.LEFT;
            }
}
        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }



    
    private void calculatePath(GameMap map) {

        int x = Math.round(getX());
        int y = Math.round(getY());

        int playerX = Math.round(player.getX());
        int playerY = Math.round(player.getY());

        GridNode start = new GridNode(x, y, playerX, playerY, null);
        GridNode goal = new GridNode(x, y, playerX, playerY, null);

        this.highwayToHeaven = Gps.findPath(start, goal, map);

    }

    private void followPath() {

        if (highwayToHeaven == null || highwayToHeaven.isEmpty()) {

            this.xVelocity = 0;
            this.yVelocity = 0;
            return;
        }
        GridNode nextNode = highwayToHeaven.get(0);
        float diffX = getX() - nextNode.getX();
        float diffY = getY() - nextNode.getY();
        
        float speed = 1;
        this.xVelocity = diffX * speed;
        this.yVelocity = diffY * speed;

        if (Math.abs(diffX) < 0.05f && Math.abs(diffY) < 0.05f) {
            highwayToHeaven.remove(0);
        }
        
    }
        
        

   

    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the frame of the walk down animation that corresponds to the current time.
    
        // if the player is not harvesting he can move
       
            if (currDirection == Direction.LEFT) {
                return  Animations.SPIDER_CHICKEN_WALKING_LEFT.getKeyFrame(this.moveTimer, true);
            }
            return  Animations.SPIDER_CHICKEN_WALKING.getKeyFrame(this.moveTimer, true);   
    }



    @Override
    public void kill() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'kill'");
    };

    
}

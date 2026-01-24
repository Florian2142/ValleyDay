package de.tum.cit.aet.valleyday.map;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.audio.SoundEffect;
import de.tum.cit.aet.valleyday.pathfinding.Gps;
import de.tum.cit.aet.valleyday.pathfinding.GridNode;
import de.tum.cit.aet.valleyday.texture.Animations;

public class Spider extends Entity implements Wildlife{

    private boolean moving = false; 
    
    // As soon as the velocity set, the chicken starts to move.
    private float moveTimer = 0f;
    
    private float timeToNextMove = 2.0f; // Chicken thinks every 2 seconds

    // boolean variables for attacking
    private float attackTime = 0f;
    private boolean hasAttacked = false;
    private boolean isAttacking = false;

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

    private boolean killed = false;
    private boolean reallyKilled = false;

    private float deathTimer = 0f;


    /**
     * The Spider constructor contains the parameters world, the x and y positions.
     * It initializes the parent class World with the super keyword.
     * @param world
     * @param x
     * @param y
     */
    public Spider(World world, float x, float y, Player player) {
        super(world, x, y);
        this.player = player;
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

        // We must load the player one time
        if (this.player == null) {
            this.player = map.getPlayer();
        }

        if (killed) {
            xVelocity = 0;
            yVelocity = 0;

            this.hitbox.setActive(false); // we can set it to false in order to remove unnecessary and weird effects

            deathTimer += frameTime;

            remove(); // call it each time to make ready for map removal

        }
        else if (isAttacking) {
            attackTime += frameTime;
            if (attackTime >= Animations.SPIDER_ATTACK_LEFT.getAnimationDuration()) {
                isAttacking = false;
                attackTime = 0;
            }
            if (attackTime >= 0.2f && !hasAttacked) {
                hasAttacked = true;
                SoundEffect.SPIDERHISS.play();
            }
            if (attackTime >= Animations.SPIDER_ATTACK_LEFT.getAnimationDuration()) {
                isAttacking = false;
                attackTime = 0;
                hasAttacked = false; 
            }
            xVelocity = 0;
            yVelocity = 0;
        }
        else {
                // right before next move
            if (timeToNextMove <= 0) {
                calculatePath(map);
                timeToNextMove = 1;
            }
            timeToNextMove--;

            followPath();

            attack(); // Spider will attack if the player is in reach

            
            if (Math.abs(xVelocity) > 0.1f) {
                if (xVelocity > 0) {
                    currDirection = Direction.RIGHT;
                } else {
                    currDirection = Direction.LEFT;
                }

            }
        
        
        }
        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }

    /**
     * The calculatePath function calculates the path the spider needs to go to reach the postion of the player.
     * It uses Gridnodes. The findPath algorithm is stored in the "highwayToHeaven" List. 
     * The findPath Algorithm takes a start GridNote and GridNote goal, when the spider reaches. the player.
     * @param map
     */
    private void calculatePath(GameMap map) {

        int x = Math.round(getX());
        int y = Math.round(getY());

        int playerX = Math.round(player.getX());
        int playerY = Math.round(player.getY());

        GridNode start = new GridNode(x, y, 0,0, null);
        GridNode goal = new GridNode(playerX, playerY,0,0, null);

        this.highwayToHeaven = Gps.findPath(start, goal, map);

    }

    /** The followPath functions List highwayToHeaven starts with the index 0 in the List.
     * 
    */
    private void followPath() {

        if (highwayToHeaven == null || highwayToHeaven.isEmpty()) {
            System.out.println("FOUND THE PLAYER!");
            System.out.println(("FOUND THE PLAYER!" + this.player));

            this.xVelocity = 0;
            this.yVelocity = 0;
            return;
        }
        GridNode nextNode = highwayToHeaven.get(0);
        
        /**To move towards the player, calculate the distance in x and y direction. */
        float diffX = nextNode.getX() - getX();
        float diffY = nextNode.getY() - getY();

        // The angle is calculated with inverse tangent that takes the value of diffX and diffY.
        float angle = MathUtils.atan2(diffY, diffX);
        
        System.out.println("FOUND THE PLAYER!");
        
        float speed = 1;

        /**When moving, the spider uses the speed and the angle. */
        this.xVelocity =     MathUtils.clamp(MathUtils.cos(angle) * speed, MathUtils.cos(angle) * speed, 3);
        this.yVelocity =     MathUtils.clamp(MathUtils.sin(angle) * speed, MathUtils.sin(angle) * speed, 3);

        if (Math.abs(diffX) < 0.05f && Math.abs(diffY) < 0.05f) {
            highwayToHeaven.remove(0);
        }
        
    }

    private void remove() {

        if (deathTimer > Animations.SPIDER_KILL.getAnimationDuration()) {
                this.reallyKilled = true; // now we must remove it from the map
            }
    }
        
        

   

    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the frame of the walk down animation that corresponds to the current time.
    
        // if the player is not harvesting he can move
            if (killed) {
                return Animations.SPIDER_KILL.getKeyFrame(this.deathTimer, false); 
            }
            else if (isAttacking) {
                if (currDirection == Direction.LEFT) {
                    return Animations.SPIDER_ATTACK_LEFT.getKeyFrame(this.attackTime, false);
                }
                else {
                    return Animations.SPIDER_ATTACK_RIGHT.getKeyFrame(this.attackTime, false);
                }
            }
            else if (currDirection == Direction.LEFT) {
                return  Animations.SPIDER_CHICKEN_WALKING_LEFT.getKeyFrame(this.moveTimer, true);
            }
            else {
                return  Animations.SPIDER_CHICKEN_WALKING.getKeyFrame(this.moveTimer, true); 
            }
              
    }

    /* Function which attacks the player when in reach */
    private void attack() {
        // Asks if the spider is in reach for an attack
        float attackRange = Entity.radius * 2f;
        float attackRangeSq = attackRange * attackRange;
        if ((Math.pow(getX() - player.getX(), 2) + Math.pow(getY() - player.getY(), 2)) < attackRangeSq) {
            isAttacking = true;
            hasAttacked = false;
            attackTime = 0f;
        }
    }



    @Override
    public void kill() {
            this.killed = true;
            
        
    };

    @Override
    public boolean isRemovable() {
        return reallyKilled;
    }

    @Override
    public boolean isDead() {
        return killed;
    }



    public boolean isMoving() {
        return moving;
    }



    public void setMoving(boolean moving) {
        this.moving = moving;
    }



    public float getMoveTimer() {
        return moveTimer;
    }



    public void setMoveTimer(float moveTimer) {
        this.moveTimer = moveTimer;
    }



    public float getTimeToNextMove() {
        return timeToNextMove;
    }



    public void setTimeToNextMove(float timeToNextMove) {
        this.timeToNextMove = timeToNextMove;
    }



    public float getAttackTime() {
        return attackTime;
    }



    public void setAttackTime(float attackTime) {
        this.attackTime = attackTime;
    }



    public boolean isHasAttacked() {
        return hasAttacked;
    }



    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }



    public boolean isAttacking() {
        return isAttacking;
    }



    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
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



    public Player getPlayer() {
        return player;
    }



    public void setPlayer(Player player) {
        this.player = player;
    }



    public static float getNormalSpeed() {
        return NORMAL_SPEED;
    }



    public List<GridNode> getHighwayToHeaven() {
        return highwayToHeaven;
    }



    public void setHighwayToHeaven(List<GridNode> highwayToHeaven) {
        this.highwayToHeaven = highwayToHeaven;
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



    public GameMap getMap() {
        return map;
    }



    public void setMap(GameMap map) {
        this.map = map;
    }



    public boolean isKilled() {
        return killed;
    }



    public void setKilled(boolean killed) {
        this.killed = killed;
    }



    public boolean isReallyKilled() {
        return reallyKilled;
    }



    public void setReallyKilled(boolean reallyKilled) {
        this.reallyKilled = reallyKilled;
    }



    public float getDeathTimer() {
        return deathTimer;
    }



    public void setDeathTimer(float deathTimer) {
        this.deathTimer = deathTimer;
    }

    
    
}

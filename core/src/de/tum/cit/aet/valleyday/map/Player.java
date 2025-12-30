package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.aet.valleyday.texture.Animations;
import de.tum.cit.aet.valleyday.texture.Drawable;
import de.tum.cit.aet.valleyday.audio.SoundEffect;
import de.tum.cit.aet.valleyday.screen.Hud;


/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Player extends Entity implements Drawable {
    
    /** Total time elapsed since the game started. We use this for calculating the player movement and animating it. */
    private float elapsedTime;

    private float harvestTime; /** var for harvesting time to make an Animation of the Harvesting */
    
    

    float MaxStamina = 100f;
    float stamina = 100f;
    boolean isExhausted = false;
    private float sprintCooldown = 0f;
    private final float COOLDOWN_DURATION = 5f; // 5 seconds
    float drainRate = 25f;
    float regenRate = 25f;


    /** Create a memory for the movement of the player*/

    /** offset x,y for the direction he is interacting with */
    private int offsetX;
    private int offsetY;

    /*Is the player standing or moving */
    private boolean moving = false;
    private boolean isHarvesting = false;

    /*Variables to store the harvesting count and tool count implenented in the HUD */
    private int harvestedCrops = 0;
    private int shovelCount = 0;
    private int fertilizerCount = 0;
    private int wateringCanCount = 0;

    /* Methods to update the counts */
    public void addCrops() {
        harvestedCrops++;
    }
    public void addShovel() {
        shovelCount++;
    }
    public void addFertilizer() {
        fertilizerCount++;
    }
    public void addWateringCan() {
        wateringCanCount++;
    }

    

    /* Nice cozy soundeffects */
    private float chopSoundCooldown = 0f;
    private static final float CHOP_SOUND_INTERVAL = 0.25f;
    private float stepSoundCooldown = 0f;
    private static final float STEP_SOUND_INTERVAL = 0.35f;

    /* Variables for Items */
    private boolean hasShovel = false;

    /** vars for the HUD */
    private int    messageCoolDown;

    /** individual MESSAGES */
    private String messageToDisplay;
    private String messageForHarvest;
    private String waterCanPickupMessage;
    private String messageForReviving;

    /** Winning conditions */
    private int currentHarvest;

    private float harvestCooloff = 0;
    private float harvestingAnimationCooloff = 0;

    private final int harvesting = 3; // UPDATE CORRESPONDING TO THE DIFFICULTY

    

    
    public Player(World world, float x, float y) {
        super(world, x, y);
    }
    
    /**
     * This function is based on the logic of keys. The user can press the keys A;W;S;D to move.
     * 
     * This has all the logic for all the player movements and all the actions a player can perform
     * 
     * @param frameTime the time since the last frame.
     */
    public void tick(float frameTime, GameMap map) {

        this.elapsedTime += frameTime; // increment the delta of the time elapsed

        this.harvestTime += frameTime; // increment the harvest time

        int currX = Math.round(getX()); // retrieve the currX always, reduces the function calls
        int currY = Math.round(getY()); // same for Y

        
        // Make the player move in a circle with radius 2 tiles
        // You can change this to make the player move differently, e.g. in response to user input.
        // See Gdx.input.isKeyPressed() for keyboard input
        float yVelocity = 0;
        float xVelocity = 0;

        /** We need to round otherwise the math is off */
        this.offsetX = currX;
        this.offsetY = currY;
        // offset the coordinates given the direction the Player is looking at
        offsetDirection(currDirection);
        
        /**
         * we define a constant speed here
         */

        float speed = 5f;

        if (sprintCooldown > 0) {
            sprintCooldown -= frameTime;
        }
        if (stamina <= 0) {
            isExhausted = true;
            sprintCooldown = 5.0f;
            stamina = 0;
        }
        if (isExhausted && stamina >= MaxStamina * 0.5f) {
            isExhausted = false;
        }
        boolean isSprinting = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && sprintCooldown <= 0 && stamina > 0;

        if (isSprinting) {
            speed = 10f;
            this.moving = true;
            stamina -= drainRate * frameTime;
        }
        //regenerates Stamina when the Shift key is not pressed.
        else if (stamina < MaxStamina) {

            stamina += regenRate * frameTime;
        }
        //keep the value of the stamina between 0 and 100. -> The nice clamping from Stefan
        stamina = MathUtils.clamp(stamina, 0, MaxStamina);

        if (stepSoundCooldown > 0f) {
            stepSoundCooldown -= frameTime;
        }

        // we want to increase the stepping sound or the frequency when he is sprinting
        float stepInterval = isSprinting ? 0.2f : STEP_SOUND_INTERVAL;

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            yVelocity += speed;
            this.currDirection = Direction.UP;
            this.moving = true;
            if (stepSoundCooldown <= 0f) {
                SoundEffect.STEPS_DIRT.play();
                stepSoundCooldown = stepInterval;
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            yVelocity -= speed;
            this.currDirection = Direction.DOWN;
            this.moving = true;
            if (stepSoundCooldown <= 0f) {
                SoundEffect.STEPS_DIRT.play();
                stepSoundCooldown = stepInterval;
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            xVelocity += speed;
            this.currDirection = Direction.RIGHT;
            this.moving = true;
            if (stepSoundCooldown <= 0f) {
                SoundEffect.STEPS_DIRT.play();
                stepSoundCooldown = stepInterval;
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            xVelocity -= speed;
            this.currDirection = Direction.LEFT;
            this.moving = true;
            if (stepSoundCooldown <= 0f) {
                SoundEffect.STEPS_DIRT.play();
                stepSoundCooldown = stepInterval;
            }
        }
        else {
            this.moving = false;
        }

        /**
         * Scans for destructible objects in the given direction 
         * 
         * if destroyable -> Destroys object piece by piece
         * 
         * KEEP key pressed if you want to completely remove the object
         */
        if (chopSoundCooldown > 0f) {
            chopSoundCooldown -= frameTime;
        }

        if (Gdx.input.isKeyPressed(Keys.D)) {
            /*** TESTING REMOVE LATER */
            System.out.println("THE CURRENT X COORDINATE IS: " + currX);
            System.out.println("THE CURRENT X COORDINATE IS: " + offsetX);
            // asks if the tile is a destructable
            if (map.isDestructible(offsetX, offsetY)) {
                int damage = hasShovel ? 2 : 1;
                // destruct the obstacle
                ((Destructible) map.getObstacle(offsetX, offsetY)).destruct(map, damage);
                if (chopSoundCooldown <= 0f) {
                    SoundEffect.BRANCHES.play(); // play the nice sound for killing branches
                    chopSoundCooldown = CHOP_SOUND_INTERVAL;
                }
            }

        }


        /**
         * Function for Harvesting the Crops and interacting with them
         * 
         * Player can Press A on an empty Soil field and plant a new Crop
         * 
         */
        if (Gdx.input.isKeyJustPressed(Keys.A)) {
            /*** TESTING REMOVE LATER */
            System.out.println("THE CURRENT X COORDINATE IS: " + currX);
            System.out.println("THE CURRENT X COORDINATE IS: " + offsetX);
            // check if the current Soil is empty
            if (map.getGround(offsetX, offsetY).getType().equals(TileType.SOIL)) {
                /**
                 * Maybe enhance this method such that we can plant different CropTypes
                 * 
                 * FOR THE MVP we stick with just a simple one!
                 */
                System.out.println("COULD ENTER THE SOIL");
                if (map.plantCrop(offsetX, offsetY, CropType.CORN) != true) {
                    System.out.println("COULD ENTER THE PLANT CROP");
                    // now we have to check if player can harvest the current crop if the SOIL isEmpty() != true
                    Crop currentCrop = map.getCrop(offsetX, offsetY);
                    if (currentCrop != null) {
                        System.out.println("CROP IS ACTUALLY NOT EMPTY");
                        // we must check if the Crop is in state 2 (implying we can harvest this one)
                        if (currentCrop.canHarvest()) {
                            System.out.println("IS THE CROP ACTUALLY HARVESTABLE");
                            map.harvestCrop(offsetX, offsetY); // harvest the crop
                            /** INCREMENTING THE WINNING CONDITION */
                            this.currentHarvest++;

                            messageForHarvest = "You just harvested: " + currentCrop.getClass().getSimpleName() + ". Only " + (harvesting - currentHarvest) + "left!";
                        }
                        else if (currentCrop.isRotten()) {
                            messageForHarvest = "Crop is Rotten, you need to water it!";
                        }
                        else {
                            messageForHarvest = "Crop is not ready for harvesting!";
                        }
                        harvestCooloff = 120;
                        harvestingAnimationCooloff = 30f; // 0.5 Seconds animation for the harvesting
                        isHarvesting = true;
                        this.harvestTime = 0;
                    }
                };
                harvestingAnimationCooloff = 30f; // 0.5 Seconds animation for the harvesting
                isHarvesting = true;
                this.harvestTime = 0;

            }

        }
        


        /**
        * Function for hidden items, exits and any easter egg
        */

        hiddenObject currHiddenObject = map.gethiddenObject(currX, currY);

        if (currHiddenObject != null && (map.getObstacle(currX, currY) == null)) {
            /** ITEM FUNCTION
             * 
             * to pick it up */
            if (currHiddenObject instanceof Item) { // DYNAMIC POLYMORPISM 
                this.messageCoolDown = 240;
                // pickup the Item and return the pickup String message
                this.messageToDisplay = ((Item)currHiddenObject).pickup(this);
            }
            /** Exit function for exiting the 
             * game 
             * MUST FULLFILL ALL THE WINNING CONDITIONS */
            else if (currHiddenObject instanceof Exit) {
                if (isWinning()) {
                    // leave the game
                    map.getGame().goToMenu();
                }
                else {
                    // not yet finished will display the message on screen
                    this.messageCoolDown = 240;
                    this.messageToDisplay = "You have not won yet! You're missing: " + (harvesting - currentHarvest);
                }
            }
            
            
        }

        /**
         * Function for the Exit. If Player has all the required winning functions
         */

        /** Decrement all the cooldowns */
        
        messageCoolDown--;
        harvestCooloff--;
        harvestingAnimationCooloff--;



        if (harvestingAnimationCooloff <= 0) {isHarvesting = false;}

        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }
    
    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the frame of the walk down animation that corresponds to the current time.
        if (isHarvesting()) {
            switch (this.currDirection) {
                    case RIGHT: return  Animations.CHARACTER_HARVEST_RIGHT.getKeyFrame(this.harvestTime, false);
                    case LEFT : return  Animations.CHARACTER_HARVEST_LEFT.getKeyFrame(this.harvestTime, false);
                    case UP   : return  Animations.CHARACTER_HARVEST_UP.getKeyFrame(this.harvestTime, false);
                    default   : return  Animations.CHARACTER_HARVEST_DOWN.getKeyFrame(this.harvestTime, false);
            }
        } 
        
        // if the player is not harvesting he can move
        else if (isMoving()) {
            switch (this.currDirection) {
                    case RIGHT: return  Animations.CHARACTER_WALK_RIGHT.getKeyFrame(this.elapsedTime, true);
                    case LEFT : return  Animations.CHARACTER_WALK_LEFT.getKeyFrame(this.elapsedTime, true);
                    case UP   : return  Animations.CHARACTER_WALK_UP.getKeyFrame(this.elapsedTime, true);
                    default   : return  Animations.CHARACTER_WALK_DOWN.getKeyFrame(this.elapsedTime, true);
            }
        }
        
        else {
            switch (this.currDirection) {
                // These weird things are basically just when the character stands still -> Makes it natural
                    case RIGHT: return  Animations.CHARACTER_WALK_RIGHT_IDLE.getKeyFrame(this.elapsedTime, true);
                    case LEFT : return  Animations.CHARACTER_WALK_LEFT_IDLE.getKeyFrame(this.elapsedTime, true);
                    case UP   : return  Animations.CHARACTER_WALK_UP_IDLE.getKeyFrame(this.elapsedTime, true);
                    default   : return  Animations.CHARACTER_WALK_DOWN_IDLE.getKeyFrame(this.elapsedTime, true);
        }
    }}

    public void equipShovel() {
        this.hasShovel = true;
    }

    /**
     * Function for the HuD to display 
     * @param time
     */

    public int messageCooldown() {
        return this.messageCoolDown;
    }   



    /**
     * Has player all the winning conditions
     * 
     */
    public boolean isWinning() {
        return this.harvesting <= currentHarvest;

    }


    /**
     * Offsets the current direction in order to find the next tile he is looking at
     * 
     * Important for Debris removal, Item Interaction
     * @param currDirection
     */
    private void offsetDirection(Direction currDirection) {

        if (currDirection == Direction.UP) {
                this.offsetY++;
            }
            else if (currDirection == Direction.DOWN) {
                this.offsetY--;
            }
            else if (currDirection == Direction.RIGHT) {
                this.offsetX++;
            }
            else {
               this.offsetX--;
            }

    }


    public float getElapsedTime() {
        return elapsedTime;
    }

    public Body getHitbox() {
        return hitbox;
    }

    public Direction getCurrDirection() {
        return currDirection;
    }

    public boolean isMoving() {
        return moving;
    }

    public float getMaxStamina() {
        return MaxStamina;
    }

    public float getStamina() {
        return stamina;
    }

    public boolean isExhausted() {
        return isExhausted;
    }

    public float getSprintCooldown() {
        return sprintCooldown;
    }

    public float getCOOLDOWN_DURATION() {
        return COOLDOWN_DURATION;
    }

    public float getDrainRate() {
        return drainRate;
    }

    public float getRegenRate() {
        return regenRate;
    }

    public float getChopSoundCooldown() {
        return chopSoundCooldown;
    }

    public static float getChopSoundInterval() {
        return CHOP_SOUND_INTERVAL;
    }

    public float getStepSoundCooldown() {
        return stepSoundCooldown;
    }

    public static float getStepSoundInterval() {
        return STEP_SOUND_INTERVAL;
    }

    public boolean isHasShovel() {
        return hasShovel;
    }

    public int getMessageCoolDown() {
        return messageCoolDown;
    }

    public String getMessageToDisplay() {
        return messageToDisplay;
    }

    public int getCurrentHarvest() {
        return currentHarvest;
    }

    public int getHarvesting() {
        return harvesting;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public String getMessageForHarvest() {
        return messageForHarvest;
    }

    public float getHarvestCooloff() {
        return harvestCooloff;
    }

    public boolean isHarvesting() {
        return isHarvesting;
    }

    public String getWaterCanPickupMessage() {
        return waterCanPickupMessage;
    }

    public String getMessageForReviving() {
        return messageForReviving;
    }

    public float getHarvestingAnimationCooloff() {
        return harvestingAnimationCooloff;
    }

    public int getHarvestedCount() { 
        return harvestedCrops; 
    }
    public int getShovelCount() { 
        return shovelCount; 
    }
    public int getFertilizerCount() { 
        return fertilizerCount; 
    }
    public int getWateringCanCount() {
        return wateringCanCount;
    }
    public boolean hasShovel() {
        return shovelCount > 0;
    }
    public boolean hasFertilizer() {
        return fertilizerCount > 0;
    }
    public boolean hasWateringCan() {
        return wateringCanCount > 0;
    }

    

    
}

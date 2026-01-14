package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Drawable;
import de.tum.cit.aet.valleyday.texture.Textures;
/**
 * Makes the Crop which can be planted by the player
 * 
 * Has different state will growing
 */

public class Crop implements Drawable{



    private boolean planted = false;

    // private final TextureRegion[] debrisState = Textures.DEBRIS_STATES;

    private int currState;
    private TextureRegion[] cropTexture;
    private float timeElapsed;
    private CropType cropType;


    private float timeToMaturity;

    private float x;
    private float y;



    /**
     * Constructs a new  Crop at the given SOIL
     * 
     * @param world the actual world
     * @param x x-axis coordinate
     * @param y y-axis coordinate
     */
    public Crop(CropType croptype, float x, float y) {
        this.cropTexture = croptype.getTextures();
        this.cropType = croptype;
        this.x = x;
        this.y = y;
        this.timeElapsed = 0;
        this.timeToMaturity = croptype.getTimeToMaturity();
    }
    
    public TextureRegion getCurrentAppearance() {
        /** Returns the current state, will update if player holds d and eventually destroy the object 
         * Iterates through the four different stages, at their index in the TextureRegion Array. 
         * Starting at index 0 of the Array, the state will be init type for each cropType in the cropType enum class. 
         * The appearance of the stages is called by their textures into the enum class.
         * Each index of the Array has a different sate defined in the textures class. 
        */
        return cropTexture[currState];
    }

    public boolean isPlanted() {
        return this.planted;
    }


    @Override
    public float getX() {
        return this.x;
    }


    @Override
    public float getY() {
        return this.y;
    }


    /*
    * Plants the Crop
    */
   public void plant() {
    this.planted = true;
   }

    /**
     * Lets the Crop grow for later Harvesting. Must be used within the tick of the player or map.
     */

    public void grow(float deltaTime) {
        timeElapsed += deltaTime; // Increment the method with each tickcall -> We call the method all the time
    
       /** with elapsing time the crop grows 
        * Check if before 60s have passed, if the current state is smaller than 2, which means that the crops are either 
        * in its initial state (index 0) or in its growing state (index 1) in the TextureRegion Array.
        * As long as the index is smaller than 2, we update the currentState (currState).
       */
        if (timeElapsed >= timeToMaturity/4 && currState < 2) {
            currState++;
            timeElapsed = 0;
        /**
        * We continue moving through the Array until being at index 2. 
        * At index 2 the crop is maturing and we update the state again, which means we are now at index 3.
        * At index 3 the crop is rotten and it is the last index of our Array so we don't update anymore. 
        * Otherwise we would get an indexOutOfBound Exception. 
        */
        }
        else if (currState == 2 && timeElapsed >= 60) {
            currState++; // Now its rotten
            timeElapsed = 0;
        }
        
        
    }


    /** Function for the watering Can -> Will revive the rotten Crops */

    public void revive() {
    
        // with elapsing time the crop grows
        // As long as the current state is 0, 1 or 2, when using the watering can, it restets the timer for all crops.
        if (currState <= 2) {
            // resets the rot timer FOR all crops
            timeElapsed = 0;
        }
        // revive the crop, setting it one state back
        else if (currState == 3) {
            currState--; 
            timeElapsed = 0; // reset count -> After 60 Seconds will be rotten again
        }
        
    }

    /** Function for Fertilizer -> Growing Crop instantly by one */
    public void fertilze() {

        // works only if state 0 or 1
        if (currState < 2) {
            currState++; // advance the State if the Player picks up the fertilizer
            timeElapsed = 0;
        }
    }




    /**
     * If player wants to harvest and the crop is actually ready for harvesting we will return true here
     * @return true if current State == 2 (indicating maturity, not like a government bond but as a crop like real economy)
     */

    public boolean canHarvest() {
        if (this.isPlanted() && this.currState == 2) {
            return true;
        }
        return false;
    }

    /**
     * is the current Crop rotten
     * 
     * @return true if is rotten (== state == 3) else false
     */
    public boolean isRotten() {
        if (isPlanted() && this.currState == 3) {
            return true;
        }
        return false;
    }

    public void setPlanted(boolean planted) {
        this.planted = planted;
    }

    public int getCurrState() {
        return currState;
    }

    public void setCurrState(int currState) {
        this.currState = currState;
    }

    public TextureRegion[] getCropTexture() {
        return cropTexture;
    }

    public void setCropTexture(TextureRegion[] cropTexture) {
        this.cropTexture = cropTexture;
    }

    public float getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(float timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    public float getTimeToMaturity() {
        return timeToMaturity;
    }

    public void setTimeToMaturity(float timeToMaturity) {
        this.timeToMaturity = timeToMaturity;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    
    










    // /**
    //  * Destroys the object 
    //  */
    // @Override
    // public void destruct(GameMap gamemap, int damage) {
    //     // if player holds d decrement the lifetime
    //     hit -= damage; // faster if player has shovel
    //     if (hit <= 0) {
    //         currState--;
    //         hit = 12;
    //     }
    //     if (currState <= 0) {
    //         this.destroyBody(gamemap.getWorld());
    //         gamemap.destroyObstacle((int) this.x, (int) this.y);
    //         this.destructed = true;
    //     }
    // }
    
}

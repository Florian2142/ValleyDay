package de.tum.cit.aet.valleyday.texture;

import org.w3c.dom.Text;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {
    
    public static final TextureRegion FLOWERS = SpriteSheet.BASIC_TILES.at(2, 5);
    public static final TextureRegion SOIL    = SpriteSheet.CROPS.at(10, 1); 
    public static final TextureRegion ICE     = SpriteSheet.BASIC_TILES.at(2, 6);
    public static final TextureRegion GRAS    = SpriteSheet.BASIC_TILES.at(2, 1);
    public static final TextureRegion WALL    = SpriteSheet.BASIC_TILES.at(3, 1);
    // STRAIGHT PIECES
    public static final TextureRegion FENCE_HORIZ = SpriteSheet.FARM_THINGS.at(6, 2);
    public static final TextureRegion FENCE_VERT  = SpriteSheet.FARM_THINGS.at(7, 3);

    // CORNERS
    public static final TextureRegion FENCE_BOTTOM_LEFT  = SpriteSheet.FARM_THINGS.at(8, 1);
    public static final TextureRegion FENCE_BOTTOM_RIGHT = SpriteSheet.FARM_THINGS.at(8, 3);
    public static final TextureRegion FENCE_TOP_LEFT     = SpriteSheet.FARM_THINGS.at(6, 1);
    public static final TextureRegion FENCE_TOP_RIGHT    = SpriteSheet.FARM_THINGS.at(6, 3);

    // Cross-stuff
    public static final TextureRegion FENCE_CROSS   = SpriteSheet.FARM_THINGS.at(7, 2);



    public static final TextureRegion CHEST = SpriteSheet.FARM_THINGS.at(5, 5);

    public static final TextureRegion EXIT = SpriteSheet.BASIC_TILES.at(7, 1);


    // testing with specific pixel estimation (bottom-left origin)
    public static final TextureRegion DEBRIS_FULL  = SpriteSheet.BASICS.fromPixelsTopLeft(190, 0, 18, 14);
    public static final TextureRegion DEBRIS_ONE   = SpriteSheet.BASICS.fromPixelsTopLeft(190, 0, 18, 14);
    public static final TextureRegion DEBRIS_TWO   = SpriteSheet.BASICS.fromPixelsTopLeft(173, 0, 14, 14);
    public static final TextureRegion DEBRIS_THREE = SpriteSheet.BASICS.fromPixelsTopLeft(155, 0, 12, 8);
    public static final TextureRegion DEBRIS_FINAL = SpriteSheet.BASICS.fromPixelsTopLeft(155, 0, 12, 8);
   


    // add all Debris Stages within an array for easy access
    public static final TextureRegion[] DEBRIS_STATES = {
        DEBRIS_FINAL, // Index 0 => Will be a destroyed object
        DEBRIS_THREE,
        DEBRIS_TWO,
        DEBRIS_ONE,
        DEBRIS_FULL // the super healthy debris object untouched of human cruelty.
    };

    /**
    * ITEMS 
    */
    // SHOVEL
    public static final TextureRegion SHOVEL= SpriteSheet.HARVEST.at(16, 10);
    // WATERCAN
    public static final TextureRegion WATERING_CAN = SpriteSheet.BASICS.at(2,11);
    // FERTILIZER
    public static final TextureRegion FERTILIZER = SpriteSheet.BASICS.at(2,12);
    
     

    /**
     * CROPS AND HARVESTING
     */
    public static final TextureRegion CORN_INIT = SpriteSheet.CROPS.at(2, 1); 
    public static final TextureRegion CORN_GROWING = SpriteSheet.CROPS.at(4, 1); 
    public static final TextureRegion CORN_MATURING = SpriteSheet.CROPS.at(6, 1); 
    public static final TextureRegion CORN_MATURED = SpriteSheet.CROPS.at(8, 1); 






    // The crop array which will greatly increase scalability and clean code :)
    public static final TextureRegion[] CORN_STAGES = {
        CORN_INIT, // Index 0 => Will be the just planted crop
        CORN_GROWING ,
        CORN_MATURING,
        CORN_MATURED
    };




}

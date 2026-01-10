package de.tum.cit.aet.valleyday.texture;

import org.w3c.dom.Text;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {
    
    public static final TextureRegion FLOWERS               = SpriteSheet.BASIC_TILES.at(2, 5);
    public static final TextureRegion SOIL                  = SpriteSheet.CROPS.at(10, 2, 1, 1); 
    public static final TextureRegion ICE                   = SpriteSheet.BASIC_TILES.at(3, 6);
    public static final TextureRegion GRAS                  = SpriteSheet.BASIC_TILES.at(9, 2);
    public static final TextureRegion WALL                  = SpriteSheet.BASIC_TILES.at(3, 1);
    public static final TextureRegion STONE                 = SpriteSheet.BASIC_TILES.at(2, 6);
    public static final TextureRegion STONES                = SpriteSheet.BASIC_TILES.at(7, 3);
    public static final TextureRegion PATH                  = SpriteSheet.BASIC_TILES.at(3, 1);
    public static final TextureRegion FOUNTAIN              = SpriteSheet.BASIC_TILES.at(4, 8);
    public static final TextureRegion SMALLPLANT            = SpriteSheet.BASIC_TILES.at(3, 5);
    public static final TextureRegion BRIDGE_VERTICAL       = SpriteSheet.BASIC_TILES.at(12, 1);
    public static final TextureRegion BRIDGE_HORIZONTAL     = SpriteSheet.BASIC_TILES.at(12, 2);
    public static final TextureRegion TORCH                 = SpriteSheet.BASIC_TILES.at(7, 5);
    public static final TextureRegion SNOW                  = SpriteSheet.BASIC_TILES.at(8, 8);
    public static final TextureRegion WATER                 = SpriteSheet.FARM_THINGS.at(6, 7);
    public static final TextureRegion SAND                  = SpriteSheet.BASIC_TILES.at(2, 3);

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
    public static final TextureRegion DEBRIS_FULL  = SpriteSheet.DEBRIS.at(1,1);
    public static final TextureRegion DEBRIS_ONE   = SpriteSheet.DEBRIS.at(1,2);;
    public static final TextureRegion DEBRIS_TWO   = SpriteSheet.DEBRIS.at(1,3);;
    public static final TextureRegion DEBRIS_THREE = SpriteSheet.DEBRIS.at(1,5);
    public static final TextureRegion DEBRIS_FINAL = SpriteSheet.DEBRIS.at(1,5);
   


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
    public static final TextureRegion SHOVEL= SpriteSheet.TOOLS.at(1, 1);
    // WATERCAN
    public static final TextureRegion WATERING_CAN = SpriteSheet.TOOLS.at(1,3);
    // FERTILIZER
    public static final TextureRegion FERTILIZER = SpriteSheet.TOOLS.at(1,4);
    
     

    /**
     * CROPS AND HARVESTING
     */
    public static final TextureRegion CORN_INIT = SpriteSheet.CROPS.at(2, 13, 1, 1); 
    public static final TextureRegion CORN_GROWING = SpriteSheet.CROPS.at(4, 13, 1, 1); 
    public static final TextureRegion CORN_MATURING = SpriteSheet.CROPS.at(8, 13, 1, 1); 
    public static final TextureRegion CORN_MATURED = SpriteSheet.CROPS.at(11, 11, 2, 1); 

    public static final TextureRegion MAIS_INIT = SpriteSheet.CROPS.at(2, 11, 1, 1); 
    public static final TextureRegion MAIS_GROWING = SpriteSheet.CROPS.at(4, 11, 1, 1); 
    public static final TextureRegion MAIS_MATURING = SpriteSheet.CROPS.at(8, 11, 1, 1); 
    public static final TextureRegion MAIS_MATURED = SpriteSheet.CROPS.at(10, 11, 1, 1); 

    public static final TextureRegion LEMON_INIT = SpriteSheet.CROPS.at(2, 10, 1, 1); 
    public static final TextureRegion LEMON_GROWING = SpriteSheet.CROPS.at(4, 10, 1, 1); 
    public static final TextureRegion LEMON_MATURING = SpriteSheet.CROPS.at(8, 10, 1, 1); 
    public static final TextureRegion LEMON_MATURED = SpriteSheet.CROPS.at(11, 10, 1, 1); 

    public static final TextureRegion SELLERIE_INIT     = SpriteSheet.CROPS.at(2, 12, 1, 1); 
    public static final TextureRegion SELLERIE_GROWING  = SpriteSheet.CROPS.at(4, 12, 1, 1); 
    public static final TextureRegion SELLERIE_MATURING = SpriteSheet.CROPS.at(8, 12, 1, 1); 
    public static final TextureRegion SELLERIE_MATURED  = SpriteSheet.CROPS.at(11, 12, 1, 1); 





    // The crop array which will greatly increase scalability and clean code :)
    public static final TextureRegion[] CORN_STAGES = {
        CORN_INIT, // Index 0 => Will be the just planted crop
        CORN_GROWING ,
        CORN_MATURING,
        CORN_MATURED
    };

    // The crop array which will greatly increase scalability and clean code :)
    public static final TextureRegion[] MAIS_STAGES = {
        MAIS_INIT, // Index 0 => Will be the just planted crop
        MAIS_GROWING ,
        MAIS_MATURING,
        MAIS_MATURED
    };

    // The crop array which will greatly increase scalability and clean code :)
    public static final TextureRegion[] LEMON_STAGES = {
        LEMON_INIT, // Index 0 => Will be the just planted crop
        LEMON_GROWING ,
        LEMON_MATURING,
        LEMON_MATURED
    };

    // The crop array which will greatly increase scalability and clean code :)
    public static final TextureRegion[] SELLERIE_STAGES = {
        SELLERIE_INIT, // Index 0 => Will be the just planted crop
        SELLERIE_GROWING ,
        SELLERIE_MATURING,
        SELLERIE_MATURED
    };



    // Textures for the HUD
    public static final TextureRegion CLOCK1 = SpriteSheet.CLOCK1.at(1,1, 3 ,3);
    public static final TextureRegion CLOCK2 = SpriteSheet.CLOCK2.at(1,1,3 ,3);
    public static final TextureRegion CLOCK3 = SpriteSheet.CLOCK3.at(1,1,3 ,3);
    public static final TextureRegion CLOCK4 = SpriteSheet.CLOCK4.at(1,1,3 ,3);
    public static final TextureRegion CLOCK5 = SpriteSheet.CLOCK5.at(1,1,3 ,3);

    public static final TextureRegion HEART = SpriteSheet.OBJECTS_SMALL.at(4,1);

    // big objects for style
    public static final TextureRegion TREE = SpriteSheet.OUTSIDE.at(1, 2, 2, 3); 
    public static final TextureRegion HOUSE = SpriteSheet.HARVEST.at(2, 7);





}


package de.tum.cit.aet.valleyday.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {
    
    public static final TextureRegion FLOWERS = SpriteSheet.BASIC_TILES.at(2, 5);
    public static final TextureRegion DIRT    = SpriteSheet.BASIC_TILES.at(7, 3);
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


    // Debris objects
    // public static final TextureRegion DEBRIS_FULL = SpriteSheet.BASICS.at(4, 7);
    // public static final TextureRegion DEBRIS_ONE = SpriteSheet.BASICS.at(4, 7);
    // public static final TextureRegion DEBRIS_TWO = SpriteSheet.BASICS.at(4, 8);
    // public static final TextureRegion DEBRIS_THREE = SpriteSheet.BASICS.at(4, 9);
    // public static final TextureRegion DEBRIS_FINAL = SpriteSheet.BASICS.at(4, 10);
    


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
     public static final TextureRegion SHOVEL= SpriteSheet.ITEMS.atBig(1, 1, 2, 1);
     //public static final TextureRegion SHOVEL = SpriteSheet.ITEMS.atBigFromBottom(3, 1, 3, 1);  // bottomRow=1, spans up 3 tiles

   



    public static final TextureRegion PUMPKIN = SpriteSheet.FARM_THINGS.at(1, 2);
    public static final TextureRegion JACK_O_LANTERN = SpriteSheet.FARM_THINGS.at(1, 3);

    public static final TextureRegion WATERING_CAN = SpriteSheet.FARM_THINGS.at(2, 6);
    public static final TextureRegion TOMATOES = SpriteSheet.FARM_THINGS.at(3, 7);
    public static final TextureRegion BUSH = SpriteSheet.FARM_THINGS.at(3, 8);  // One of the black bushes
    public static final TextureRegion TREE_STUMP = SpriteSheet.FARM_THINGS.at(3, 11);

    // Larger example: Barn roof (assuming 3 wide, 1 tall)
    public static final TextureRegion BARN_ROOF = SpriteSheet.FARM_THINGS.atBig(1, 4, 3, 1);  // heightTiles=3? Wait, no: for horizontal, heightTiles=1 (vert), widthTiles=3 (horiz)

    // Larger example: Greenhouse (assuming 6 wide, 3 tall—adjust after counting pixels)
    public static final TextureRegion GREENHOUSE = SpriteSheet.FARM_THINGS.atBig(3, 1, 6, 3);

    // From second image (basics.png)—adjust row/col based on exact counts; assuming 32x32 grid
    public static final TextureRegion HEART = SpriteSheet.BASICS.at(1, 3);  // Red heart example
    public static final TextureRegion FIRE = SpriteSheet.BASICS.at(2, 1);
    public static final TextureRegion COIN = SpriteSheet.BASICS.at(3, 1);
    public static final TextureRegion ROCK = SpriteSheet.BASICS.at(3, 4);  // Pebble example
    public static final TextureRegion GREEN_BUSH = SpriteSheet.BASICS.at(6, 5);  // Round green bush
    public static final TextureRegion NUMBER_ONE = SpriteSheet.BASICS.at(7, 2);
    public static final TextureRegion ARROW = SpriteSheet.BASICS.at(8, 1);
    public static final TextureRegion ICICLE = SpriteSheet.BASICS.at(8, 4);
        
}

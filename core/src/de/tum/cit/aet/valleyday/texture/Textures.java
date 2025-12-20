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
    public static final TextureRegion ICE    = SpriteSheet.BASIC_TILES.at(2, 6);
    public static final TextureRegion GRAS    = SpriteSheet.BASIC_TILES.at(2, 0);
    public static final TextureRegion WALL    = SpriteSheet.BASIC_TILES.at(3, 0);

    // STRAIGHT PIECES
public static final TextureRegion FENCE_HORIZ = SpriteSheet.BASIC_TILES.at(6, 2);
public static final TextureRegion FENCE_VERT  = SpriteSheet.BASIC_TILES.at(7, 3);

// CORNERS
public static final TextureRegion FENCE_BOTTOM_LEFT  = SpriteSheet.BASIC_TILES.at(8, 1);
public static final TextureRegion FENCE_BOTTOM_RIGHT = SpriteSheet.BASIC_TILES.at(8, 3);
public static final TextureRegion FENCE_TOP_LEFT     = SpriteSheet.BASIC_TILES.at(6, 1);
public static final TextureRegion FENCE_TOP_RIGHT    = SpriteSheet.BASIC_TILES.at(6, 3);

// Cross-stuff
public static final TextureRegion FENCE_CROSS   = SpriteSheet.BASIC_TILES.at(7, 2);



public static final TextureRegion CHEST = SpriteSheet.BASIC_TILES.at(5, 5);
    
}

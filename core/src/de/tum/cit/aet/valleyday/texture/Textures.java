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
    public static final TextureRegion SAND    = SpriteSheet.BASIC_TILES.at(1, 0);
    public static final TextureRegion GRAS    = SpriteSheet.BASIC_TILES.at(2, 0);
    public static final TextureRegion WALL    = SpriteSheet.BASIC_TILES.at(3, 0);

    public static final TextureRegion CHEST = SpriteSheet.BASIC_TILES.at(5, 5);
    
}

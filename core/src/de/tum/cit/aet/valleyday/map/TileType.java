package de.tum.cit.aet.valleyday.map;


import org.w3c.dom.Text;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.aet.valleyday.texture.Textures;
/**
 * ENUM datatype for the TileType
 * 
 * Enables us to use different grounds for the map visualization
 */
public enum TileType {
 
        FLOWERS(true, Textures.FLOWERS),
        SOIL(true, Textures.SOIL),
        GRAS(true, Textures.GRAS),
        LAVA(true, Textures.LAVA),
        SNOW(true, Textures.SNOW),
        START(true, Textures.START),

        PATH_FULL(true, Textures.PATH_FULL),                    // Center path
        PATH_UP(true, Textures.PATH_UP),                        // Top edge
        PATH_DOWN(true, Textures.PATH_DOWN),                    // Bottom edge
        PATH_LEFT(true, Textures.PATH_LEFT),                    // Left edge
        PATH_RIGHT(true, Textures.PATH_RIGHT),                  // Right edge

        PATH_CORNER_TL(true, Textures.PATH_LEFT_CORNER_T),      // Top Left
        PATH_CORNER_TR(true, Textures.PATH_RIGHT_CORNER_T),     // Top Right
        PATH_CORNER_BL(true, Textures.PATH_LEFT_CORNER_B),      // Bottom Left
        PATH_CORNER_BR(true, Textures.PATH_RIGHT_CORNER_B),     // Bottom Right

        SMALL_ROCK(false, Textures.SMALL_ROCK), // Blocks movement
        TINY_ROCK(true, Textures.TINY_ROCK),    // Decoration (Walkable)
        BIG_ROCK(false, Textures.BIG_ROCK),     // Large obstacle

        
        SAND(true, Textures.SAND),
        WATER(true, Textures.WATER),
        PATH(true, Textures.PATH),
        STONE(true, Textures.STONE),
        STONES(true, Textures.STONES),
        SMALLPLANT(true, Textures.SMALLPLANT),

        FLOWER1(false, Textures.FLOWERS1),
        FLOWER2(false, Textures.FLOWERS2),
        FLOWER3(false, Textures.FLOWERS3),

        BRIDGE_VERTICAL(true, Textures.BRIDGE_VERTICAL),
        BRIDGE_HORIZONTAL(true, Textures.BRIDGE_HORIZONTAL);
        


        // We define the variable for differing between passable objects and SOLID stuff
        private final boolean passable;
        private final TextureRegion texture;

        // we define the constructor -> Passing the argument from above. Later it will be easier to define shared behaviour with that
        private TileType(boolean passable, TextureRegion textures) {
            this.passable = passable;
            this.texture = textures;
        }

        public boolean isPassable() {
            return this.passable;
        }
        public TextureRegion getTexture() {
            return this.texture;
        }

}

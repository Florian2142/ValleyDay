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
        ICE(true, Textures.ICE),
        SNOW(true, Textures.SNOW),

        SAND(true, Textures.SAND),
        WATER(true, Textures.WATER),
        PATH(true, Textures.PATH),
        STONE(true, Textures.STONE),
        STONES(true, Textures.STONES),
        SMALLPLANT(true, Textures.SMALLPLANT),

        WALL(false, Textures.WALL),
        CHEST(false, Textures.CHEST),
        TREE(false, Textures.TREE),
        HOUSE(false, Textures.HOUSE),
        FOUNTAIN(false, Textures.FOUNTAIN),
        TORCH(false, Textures.TORCH),

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

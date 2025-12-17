package de.tum.cit.aet.valleyday.map;


import org.w3c.dom.Text;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.aet.valleyday.texture.Textures;
/**
 * ENUM datatype 
 * .
 * 
 */
public enum TileType {
 
        FLOWERS(true, Textures.FLOWERS),
        DIRT(true, Textures.DIRT),
        SAND(true, Textures.SAND),
        GRAS(true, Textures.GRAS),
        WALL(false, Textures.WALL),
        CHEST(false, Textures.CHEST);
        


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

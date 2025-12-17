package de.tum.cit.aet.valleyday.map;

import org.w3c.dom.Text;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.aet.valleyday.texture.Drawable;
import de.tum.cit.aet.valleyday.texture.Textures;

/**
 * Makes a generic Tiles class based on the ENUM datatype to 
 * avoid redundance
 * 
 */

public class Tiles implements Drawable{

    private final int x;
    private final int y;
    private final TileType type;
    private final TextureRegion texture;

    /**
     * 
     * @param x => X-Coordinate
     * @param y => Y-Coordinate
     * @param type => Type, Please refer to the TileType for seeing the various types
     */

    public Tiles(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.texture =  type.getTexture();

    }

    /**
     * Get the current Appearances, which is deduced from Textures.java please refer there if changing anything. 
     * This class is only supposed to safe redundancy
     */

    @Override 
    public TextureRegion getCurrentAppearance() {
        return this.texture;
    }

    @Override
    public float getX() {
        return x;
    }
    
    @Override
    public float getY() {
        return y;
    }

    public TileType getType() {
        return type;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    

}

package de.tum.cit.aet.valleyday.map;




import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.aet.valleyday.texture.Drawable;
import de.tum.cit.aet.valleyday.texture.Textures;



/**
 * Represents a hiddenObject class.
 * 
 * Can according to the Liskov Substitution principle include ALL things which promise the same contract
 * 
 * HENCE -> Shovel, EXIT are all classified as hiddenobjects and share the same behavior
 * 
 */

public abstract class hiddenObject implements Drawable {

    private final int x;
    private final int y;
    private boolean hidden;
    protected TextureRegion texture;
    protected GameMap map; // needs access for the map especially the obstacles to determine if its been hidden or not

    /**
     * 
     * @param x => X-Coordinate
     * @param y => Y-Coordinate
     * @param type => Type, Please refer to the TileType for seeing the various types
     */

    public hiddenObject(int x, int y, GameMap map) {
        this.x = x;
        this.y = y;
        this.hidden = true;
        this.map = map;
    }

    /**
     * Get the current Appearances, which is deduced from Textures.java please refer there if changing anything. 
     * This class is only supposed to safe redundancy
     */

    @Override 
    public TextureRegion getCurrentAppearance() {
        hiddenUpdate();
        if (hidden == false) {
            return getTexture();
        }
        else {
            return null;
        }
    }

    private void hiddenUpdate() {
        if (map.getObstacle((int) getX(), (int) getY()) == null) {
            hidden = false;
        }
        return;
    }


    @Override
    public float getX() {
        return x;
    }
    
    @Override
    public float getY() {
        return y;
    }

    /**
     * Overwrite for each Item to get the individual Texture
     * @return the individual Texture
     */
    public TextureRegion getTexture() {
        return texture;
    }

    public boolean isHidden() {
        return hidden;
    }    
}

package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.aet.valleyday.texture.Textures;

public class Exit extends hiddenObject {

    /**
     * The Exit class extends the hiddenObject class and implements the Item interface.
     * The constructor takes the GameMap as a parameter and the textures variable from the Parent class hiddenObject.
     * @param x
     * @param y
     * @param map
     */
    public Exit(int x, int y, GameMap map) {
        super(x, y, map);
        super.texture = Textures.EXIT;
    }
    /**
     * Opens the Exit if the player reaches the winning quota
     */
    public void openExit() {
        super.texture = Textures.OPEN_EXIT;
    }
    
}

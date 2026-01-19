package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.aet.valleyday.texture.Textures;

public class Exit extends hiddenObject {

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

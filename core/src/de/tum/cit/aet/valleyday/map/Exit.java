package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.tum.cit.aet.valleyday.texture.Textures;

public class Exit extends hiddenObject {

    public Exit(int x, int y, GameMap map) {
        super(x, y, map);
        super.texture = Textures.EXIT;
      
    }
    
    
}

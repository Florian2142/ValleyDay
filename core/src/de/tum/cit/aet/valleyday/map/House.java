package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

import de.tum.cit.aet.valleyday.texture.Textures;
/**
 * 
 * Makes an above proportional house which will only be one time planted in the first map.
 * Can only be used where the player is not walking
 * 
 */
public class House extends Obstacle {

    private TextureRegion currTexture;


    public House(World world, int x, int y) {
        super(world, x, y);
        this.currTexture = Textures.HOUSE;

    }

    @Override
    public TextureRegion getCurrentAppearance() {
        // return the default Fence here
        return currTexture;
    }
}
package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Animations;

/**
 * 
 * Makes a moving Tornado animation
 * Will be stored in the 41 case of the switch
 * 
 */
public class Tornado extends Obstacle {

    private float time = 0f;

    

    public Tornado(World world, float x, float y) {
        super(world, x, y);
    }

    /**
     * getCurrentAppearance returns the Animations in a loop.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        time += Gdx.graphics.getDeltaTime();
        return Animations.TORNADO.getKeyFrame(time,true);
    }
    
}

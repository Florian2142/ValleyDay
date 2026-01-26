package de.tum.cit.aet.valleyday.tornados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.map.Obstacle;
import de.tum.cit.aet.valleyday.texture.Animations;

/**
 * 
 * Makes a moving Tornado animation
 * 
 * 
 */
public class Tornado extends Obstacle {

    private float time = 0f;

    private final Animation<TextureRegion> animation;

    

    public Tornado(World world, float x, float y, Animation<TextureRegion> animation) {
        super(world, x, y);
        this.animation = animation;
    }

    /**
     * getCurrentAppearance returns the Animations in a loop.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        time += Gdx.graphics.getDeltaTime();
        return animation.getKeyFrame(time, true);
    }
    
}

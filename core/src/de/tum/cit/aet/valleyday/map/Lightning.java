package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Animations;

public class Lightning extends Obstacle {

    private float time = 0f;

    public Lightning(World world, float x, float y) {
        super(world, x, y);
        super.destroyBody(world);
        
        //TODO Auto-generated constructor stub
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        time += Gdx.graphics.getDeltaTime();
        return Animations.LIGHTNING.getKeyFrame(time, true);
        
    }
    
}

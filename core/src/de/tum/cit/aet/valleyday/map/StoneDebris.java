package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Textures;
import de.tum.cit.aet.valleyday.audio.SoundEffect;
import de.tum.cit.aet.valleyday.texture.Animations;



public class StoneDebris extends Obstacle implements Destructible {
    

     private boolean destructed = false;

     private float time;

     private boolean isTriggered = false;


    private final TextureRegion debrisState = Textures.STONE_DEBRIS;

   


    /**
     * Constructs a new Debris Item -> id = 1 in MAP 
     * 
     * @param world the actual world
     * @param x x-axis coordinate
     * @param y y-axis coordinate
     */
    public StoneDebris(World world, float x, float y) {
        super(world, x, y);
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        /** Returns the current state, will update if player holds d and eventually destroy the object */
        if (isTriggered) {
            return Animations.EXPLOSION.getKeyFrame(time,false);
        }
            return debrisState;
    }

    @Override
    public boolean isDestructible() {
        return true;
    }

    public void tick(float delta, GameMap map) {
        if (isTriggered) {
            time += delta;
        }
        if (Animations.EXPLOSION.isAnimationFinished(time)) {
            this.destroyBody(map.getWorld());
            map.destroyObstacle((int) this.x, (int) this.y);
            this.destructed = true;
        }
    }


    /**
     * Destroys the object 
     */
    @Override
    public void destruct(GameMap gamemap, int damage) {
        // if player taps d and has dynamite then he destroys the debris
           if (isTriggered || isDestructed()) {
            return;
           }
            this.isTriggered = true;
            time = 0f;

            gamemap.addExplodingDebris(this);

            SoundEffect.EXPLOSION.play();
        }
    

    public boolean isDestructed() {
        return destructed;
    }

    public void setDestructed(boolean destructed) {
        this.destructed = destructed;
    }

}

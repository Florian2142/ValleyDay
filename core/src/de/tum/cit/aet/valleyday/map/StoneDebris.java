package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Textures;
import de.tum.cit.aet.valleyday.audio.SoundEffect;
import de.tum.cit.aet.valleyday.texture.Animations;


/**
 * 
 * Stonedebris is an harder to destroy debris where the player
 * first must find dynamite to destroy -> Will explode really cool!
 */
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


    /**
     * Tick function which takes the time of the map
     * and checks if the animation time is smaller than the time passed since triggered
     * If its smaller will then remove object from the map
     * 
     * @param delta time difference
     * @param map the gamemap
     */
    public void tick(float delta, GameMap map) {
        if (isTriggered) {
            time += delta;
        }
        /** Uses the Explosion animation when player hits D while having Dynamite */
        if (Animations.EXPLOSION.isAnimationFinished(time)) {
            this.destroyBody(map.getWorld());
            map.destroyObstacle((int) this.x, (int) this.y);
            this.destructed = true;
        }
    }


    /**
     * Destroys the object -> Will be triggered by the player
     */
    @Override
    public void destruct(GameMap gamemap, int damage) {
        // if player taps d and has dynamite then he destroys the debris
           if (isTriggered || isDestructed()) {
            return; // if already destroyed just return
           }
            this.isTriggered = true;
            time = 0f;

            gamemap.addExplodingDebris(this); // will add this StoneDebris to the GAMEMAP tick function 

            SoundEffect.EXPLOSION.play(); // plays the sound
        }
    

    public boolean isDestructed() {
        return destructed;
    }

    public void setDestructed(boolean destructed) {
        this.destructed = destructed;
    }

}

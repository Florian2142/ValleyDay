package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Textures;
/**
 * Makes a Debris class which extends an Obstacle 
 * 
 * Implements a Destructible => Can be destroyed
 */

public class Debris extends Obstacle implements Destructible{



    private boolean destructed = false;

    private final int lifeTIME = 4;

    private final TextureRegion[] debrisState = Textures.DEBRIS_STATES;

    private int currState;
    private int hit;


    /**
     * Constructs a new Debris Item -> id = 1 in MAP 
     * 
     * @param world the actual world
     * @param x x-axis coordinate
     * @param y y-axis coordinate
     */
    public Debris(World world, float x, float y) {
        super(world, x, y);
        this.currState = lifeTIME;
        this.hit = 12;
    }

    @Override
    public TextureRegion getCurrentAppearance() {
        /** Returns the current state, will update if player holds d and eventually destroy the object */
        return debrisState[currState];
    }

    @Override
    public boolean isDestructible() {
        return true;
    }


    /**
     * Destroys the object 
     */
    @Override
    public void destruct(GameMap gamemap, int damage) {
        // if player holds d decrement the lifetime
        hit -= damage; // faster if player has shovel
        if (hit <= 0) {
            currState--;
            hit = 12;
        }
        if (currState <= 0) {
            this.destroyBody(gamemap.getWorld());
            gamemap.destroyObstacle((int) this.x, (int) this.y);
            this.destructed = true;
        }
    }
    
}

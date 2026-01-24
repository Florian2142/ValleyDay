package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

import de.tum.cit.aet.valleyday.texture.Textures;


/**
 * 
 * This class make a very large tree from the PokemonTiles. 
 * Must be placed in the bigobstacles array in the GameMap, otherwise would be 
 */
public class BigTree extends Obstacle {

    private TextureRegion currTexture;


    public BigTree(World world, int x, int y) {
        super(world, x, y);
        this.currTexture = Textures.BIGTREE;

    }

    @Override
    public TextureRegion getCurrentAppearance() {
        // return the default Fence here
        return currTexture;
    }
}
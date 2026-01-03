package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.aet.valleyday.texture.Textures;

public class Trees extends Obstacle {

    private TextureRegion currTexture;


    public Trees(World world, int x, int y) {
        super(world, x, y);
        this.currTexture = Textures.TREE;

    }

    @Override
    public TextureRegion getCurrentAppearance() {
        // return the default Fence here
        return currTexture;
    }
}
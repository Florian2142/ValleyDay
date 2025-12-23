package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.aet.valleyday.texture.Textures;

public class Fence extends Obstacle {

    private TextureRegion currTexture;


    public Fence(World world, int x, int y) {
        super(world, x, y);
        this.currTexture = Textures.FENCE_HORIZ;

    }

    @Override
    public TextureRegion getCurrentAppearance() {
        // return the default Fence here
        return currTexture;
    }

    public void updatextureRegion(GameMap map) {

        // cast it to int -> safe, because input must be integers from the map
        int x = (int) super.getX();
        int y = (int) super.getY();

        boolean up    = map.isFence(x, y + 1);
        boolean down  = map.isFence(x, y - 1);
        boolean left  = map.isFence(x - 1, y);
        boolean right = map.isFence(x + 1, y);

        
        if      (!up && !down && left && right) {this.currTexture = Textures.FENCE_HORIZ;}          // horizontal
        else if (up && down && !left && !right) {this.currTexture = Textures.FENCE_VERT;}           // vertical
        else if (!up && down && !left && right) {this.currTexture = Textures.FENCE_TOP_LEFT;}       // top-left
        else if (!up && down && left && !right) {this.currTexture = Textures.FENCE_TOP_RIGHT;}      // top-right
        else if (up && !down && !left && right) {this.currTexture = Textures.FENCE_BOTTOM_LEFT;}    // bottom-left
        else if (up && !down && left && !right) {this.currTexture = Textures.FENCE_BOTTOM_RIGHT;}   // bottom-right
        else                                    {this.currTexture = Textures.FENCE_CROSS;}          // fence-cross
        
    }


    
    
    
}

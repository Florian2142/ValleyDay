package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import de.tum.cit.aet.valleyday.texture.Textures;

/**
 * Different Crop Types which have different growing times 
 * 
 * All crops look different and hence we have a simply TextureRegion[] array
 * 
 * 
 */
public enum CropType {
 
        CORN(Textures.CORN_STAGES, 45f);
      
        


        private final TextureRegion[] textures; // Array of 
        private final Float timeToMaturity; // time to maturity meaning the seconds till harvesting is possible

        // we define the constructor -> Passing the argument from above. Later it will be easier to define shared behaviour with that
        private CropType(TextureRegion[] textures, Float timeToMaturity) {
            this.textures = textures;
            this.timeToMaturity = timeToMaturity;
        }


        /** Returns the stages as a 1D array making it possible to change stages while times elapses*/
        public TextureRegion[] getTextures() {
            return this.textures;
        }

        public float getTimeToMaturity() {
            return this.timeToMaturity;
        }
    

}
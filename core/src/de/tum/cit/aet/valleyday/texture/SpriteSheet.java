package de.tum.cit.aet.valleyday.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Enumerates all spritesheets used in the game and provides helper methods for grabbing texture regions from them.
 * It is assumed that every spritesheet has some standard grid size which can be used for easier coordinate specification.
 * See the assets/texture folder for the actual texture files (plus some more samples which are not enumerated here).
 * Feel free to add your own spritesheets and use them in the game!
 *
 * @see Texture a whole image
 * @see TextureRegion a part of an image
 */
public enum SpriteSheet {
    
    /** The character spritesheet, which has a grid size of 16x32. */
    CHARACTER("character.png", 16, 32),
    /** The basic tiles spritesheet, which has a grid size of 16x16. */
    BASIC_TILES("basictiles.png", 16, 16),
    /** Things about the farm like the fence and gates and other stuff */
    FARM_THINGS("farmthings.png",16 ,16),
    /** Objects like Debris and many more */
    OBJECTS_SMALL("objects.png", 22,22),
    /** Big objects (4x4) */
    OBJECTS_BIG("objects.png", 22, 16),
    /** basics like the branch removal */
    // BASICS("basics.png", 303, 132);
    BASICS("basics2.png", 16,16),

    // BASICS("basics.png", 303, 132);
    ITEMS("Items.png", 16,16),


    // CROPS
    CROPS("crops.png", 32, 32);

    // FARMTHINGS -> DOWNLOADED FROM KENNEY FREE OPEN SOURCE
    //HARVEST("tilemap.png", 16, 16);
    
    



    private final Texture spritesheet;
    private final int width;
    private final int height;
    
    /**
     * Constructor for each variant of this enum.
     * Every SpriteSheet has a corresponding file, width, and height.
     * @param filename the filename of the spritesheet
     * @param width the width of a single grid cell
     * @param height the height of a single grid cell
     */
    SpriteSheet(String filename, int width, int height) {
        this.spritesheet = new Texture(Gdx.files.internal("texture/" + filename));
        this.spritesheet.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);  // more nice pixels
        this.width = width;
        this.height = height;
    }
    
    /**
     * Returns the TextureRegion at the specified row and column (1-based coordinates)
     * according to the grid specified by {@code this.width} and {@code this.height}.
     * This method assumes the size of the texture to be a single grid cell.
     * Keep in mind that since spritesheet textures typically start in the top-left corner,
     * the row index starts at 1 at the top and the column index starts at 1 on the left.
     *
     * @param row the row of the texture to fetch, starting from 1 at the TOP of the spritesheet
     * @param column the column of the texture to fetch, starting from 1 on the LEFT of the spritesheet
     * @return the texture
     */
    public TextureRegion at(int row, int column) {
        return new TextureRegion(
                spritesheet,
                (column - 1) * this.width,
                (row - 1) * this.height,
                this.width,
                this.height
        );
    }

    /**
     * Returns the TextureRegion at the specified row and column (3-BASED-COORDINATES)
     * according to the grid specified by {@code this.width} and {@code this.height}.
     * This method assumes the size of the texture to be a single grid cell.
     * Keep in mind that since spritesheet textures typically start in the top-left corner,
     * the row index starts at 1 at the top and the column index starts at 1 on the left.
     *
     * @param row the row of the texture to fetch, starting from 1 at the TOP of the spritesheet
     * @param column the column of the texture to fetch, starting from 1 on the LEFT of the spritesheet
     * @return the texture
     */
    public TextureRegion atBig(int row, int column, int heightTiles, int widthTiles) {
        return new TextureRegion(
                spritesheet,
                (column - 1) * this.width,
                (row - 1) * this.height,
                this.width * widthTiles,
                this.height * heightTiles
        );
    }


    /**
     * Method which we potencially use because we need to extract the exact pixels for the visuals
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public TextureRegion fromPixelsTopLeft(int x, int yTop, int width, int height) {
        int y = spritesheet.getHeight() - yTop - height;
        return new TextureRegion(spritesheet, x, y, width, height);
}

public TextureRegion atBigFromBottom(int bottomRow, int column, int heightTiles, int widthTiles) {
    int totalRows = spritesheet.getHeight() / height;
    int startRow = totalRows - heightTiles - (bottomRow - 1);  // bottomRow 1 = bottom
    return atBig(startRow, column, heightTiles, widthTiles);
}



}
    


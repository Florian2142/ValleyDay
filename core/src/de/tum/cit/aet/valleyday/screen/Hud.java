package de.tum.cit.aet.valleyday.screen;

import de.tum.cit.aet.valleyday.map.Player;
import de.tum.cit.aet.valleyday.map.Shovel;
import de.tum.cit.aet.valleyday.texture.Drawable;
import de.tum.cit.aet.valleyday.texture.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * A Heads-Up Display (HUD) that displays information on the screen.
 * It uses a separate camera so that it is always fixed on the screen.
 */
public class Hud {

    /**
     * The SpriteBatch used to draw the HUD. This is the same as the one used in the
     * GameScreen.
     */
    private final SpriteBatch spriteBatch;
    /** The font used to draw text on the screen. */
    private final BitmapFont font;
    /** The camera used to render the HUD. */
    private final OrthographicCamera camera;

    /** Hud needs to know the player */
    private Player player;
    private ShapeRenderer shapeRenderer;
    private float x;
    private float y;
    float hudWidth;
    float hudHeight;
    float margin;

    public Hud(SpriteBatch spriteBatch, BitmapFont font, Player player) {

        this.spriteBatch = spriteBatch;
        this.font = font;
        this.camera = new OrthographicCamera();
        this.player = player;
        this.shapeRenderer = new ShapeRenderer();
        //Define global settings 
        hudWidth = 220;
        hudHeight = 150;
        margin = 20;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        x = screenWidth - hudWidth - margin;
        y = screenHeight - hudHeight - margin;
    }

    /**
     * Renders the HUD on the screen.
     * This uses a different OrthographicCamera so that the HUD is always fixed on
     * the screen.
     */
    public void render(float timeRemaining) {

        shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(x, y, hudWidth, hudHeight);
        shapeRenderer.end();
        // Render from the camera's perspective
        spriteBatch.setProjectionMatrix(camera.combined);
        // Start drawing
        spriteBatch.begin();
        // Draw the HUD elements
        // font.draw(spriteBatch, "Press Esc to Pause!", 10, Gdx.graphics.getHeight() -
        // 10);
        // message for the Time left
        font.draw(spriteBatch, "Time left: " + (int) timeRemaining / 60 + ":" + (int) timeRemaining % 60, 10,
                Gdx.graphics.getHeight() - 10);
        // Displayes the harvested crops in the left hand corner.
        font.draw(spriteBatch, "Harvested crops: " + player.getCurrentHarvest() + "/" + player.getHarvesting(), 10,
                Gdx.graphics.getHeight() - 40);
        // Displayes the tools.
        font.draw(spriteBatch, "Tools: ", 10, Gdx.graphics.getHeight() - 80);
        /** Graphics for HUD  */

        draw(spriteBatch, Textures.SHOVEL, 80, 80, 25, Gdx.graphics.getHeight() - 150);
        // Displays the EXIT option.
        // At the start the Exit font color is displayed red.
        // If the the harvesting Quota is reached, font color is changed to green.
        if (player.isWinning()) {
            font.setColor(0, 255, 0, 0.5f);
        } else {
            font.setColor(255, 0, 0, 0.5f);
        }
        font.draw(spriteBatch, "EXIT ", 25, Gdx.graphics.getHeight() - 120);
        font.setColor(255, 255, 255, 0.5f);

        /** Display message for any interactions with hidden items */
        if (player.messageCooldown() > 0) {
            font.draw(spriteBatch, player.getMessageToDisplay(), 10, Gdx.graphics.getHeight() - 800);
        }

        if (player.getHarvestCooloff() > 0) {
            font.draw(spriteBatch, player.getMessageForHarvest(), Gdx.graphics.getWidth() - 650,
                    Gdx.graphics.getHeight() - 800);
        }

        // Finish drawing
        spriteBatch.end();
    }

    /**
     * Resizes the HUD when the screen size changes.
     * This is called when the window is resized.
     * 
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    private static void draw(SpriteBatch spriteBatch, TextureRegion textureRegion, float height, float width, 
        float x, float y) {
        TextureRegion texture = textureRegion;

        // 1. Calculate the size of the logical tile on screen
        // float tilePx = TILE_SIZE_PX * SCALE; // e.g., 64 pixels

        // 2. Calculate the size of the sprite to draw
        float drawWidth = width;
        float drawHeight = height;

        // 3. Calculate Position
        // Base X/Y is the bottom-left corner of the TILE
        float baseX = x;
        float baseY = y;

        //

        float drawX = baseX + (drawWidth) / 2;

        // ALIGN BOTTOM vertically
        // This ensures the object's "feet" sit on the bottom of the tile
        // and the "head" sticks up into the tile above.
        float drawY = baseY;

        spriteBatch.draw(texture, drawX, drawY, drawWidth, drawHeight);
    }

}

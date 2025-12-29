package de.tum.cit.aet.valleyday.screen;

import de.tum.cit.aet.valleyday.map.GameMap;
import de.tum.cit.aet.valleyday.map.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * A Heads-Up Display (HUD) that displays information on the screen.
 * It uses a separate camera so that it is always fixed on the screen.
 */
public class Hud {
    
    /** The SpriteBatch used to draw the HUD. This is the same as the one used in the GameScreen. */
    private final SpriteBatch spriteBatch;
    /** The font used to draw text on the screen. */
    private final BitmapFont font;
    /** The camera used to render the HUD. */
    private final OrthographicCamera camera;

    /** Hud needs to know the player */
    private Player player;
    private ShapeRenderer shapeRenderer;

    public Hud(SpriteBatch spriteBatch, BitmapFont font, Player player) {
        this.spriteBatch = spriteBatch;
        this.font = font;
        this.camera = new OrthographicCamera();
        this.player = player;
        this.shapeRenderer = new ShapeRenderer();
    }
    
    /**
     * Renders the HUD on the screen.
     * This uses a different OrthographicCamera so that the HUD is always fixed on the screen.
     */
    public void render(float timeRemaining) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float hudWidth = 220;
        float hudHeight = 150;
        float margin = 20;
        float x = screenWidth - hudWidth - margin;
        float y = screenHeight - hudHeight - margin;

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
        font.draw(spriteBatch, "Press Esc to Pause!", 10, Gdx.graphics.getHeight() - 10);
        // message for the Time left
        font.draw(spriteBatch, "Time left: " + (int) timeRemaining,Gdx.graphics.getWidth() - 265, Gdx.graphics.getHeight() - 10);
        

        /** Display message for any interactions with hidden items */
        if (player.messageCooldown() > 0) {
            font.draw(spriteBatch, player.getMessageToDisplay(), 10,Gdx.graphics.getHeight() - 800);
        }

        if (player.getHarvestCooloff() > 0) {
            font.draw(spriteBatch, player.getMessageForHarvest(), Gdx.graphics.getWidth() - 650,Gdx.graphics.getHeight() - 800);
        }

  


        // Finish drawing
        spriteBatch.end();
    }
    
    /**
     * Resizes the HUD when the screen size changes.
     * This is called when the window is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    
}

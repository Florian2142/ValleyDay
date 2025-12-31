package de.tum.cit.aet.valleyday.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.aet.valleyday.ValleyDayGame;
import de.tum.cit.aet.valleyday.map.Chicken;
import de.tum.cit.aet.valleyday.map.Flowers;
import de.tum.cit.aet.valleyday.texture.Drawable;

import de.tum.cit.aet.valleyday.map.GameMap;
import de.tum.cit.aet.valleyday.map.Player;
import de.tum.cit.aet.valleyday.map.Tiles;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {
    
    /**
     * The size of a grid cell in pixels.
     * This allows us to think of coordinates in terms of square grid tiles
     * (e.g. x=1, y=1 is the bottom left corner of the map)
     * rather than absolute pixel coordinates.
     */
    public static final int TILE_SIZE_PX = 16;
    
    /**
     * The scale of the game.
     * This is used to make everything in the game look bigger or smaller.
     */
    public static final int SCALE = 4;

    private final ValleyDayGame game;
    private final SpriteBatch spriteBatch;
    private final GameMap map;
    private final Hud hud;
    private final OrthographicCamera mapCamera;


    /** Stuff for the Time runner */
    private float remainingTime;
    private int tick = 60;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(ValleyDayGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        this.map = game.getMap();
        this.hud = new Hud(spriteBatch, game.getSkin().getFont("font"), map.getPlayer());
        // Create and configure the camera for the game view
        this.mapCamera = new OrthographicCamera();
        this.mapCamera.setToOrtho(false);


        /** DUMMY VARIABLES CHANGE LATER TO DIFFICULTY */
        this.remainingTime = 314;
        // Set harvesting quota 
        map.getPlayer().setHarvesting(5); 
    }
    
    /**
     * The render method is called every frame to render the game.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
        
        // Clear the previous frame from the screen, or else the picture smears
        ScreenUtils.clear(Color.BLACK);
        
        // Cap frame time to 250ms to prevent spiral of death
        float frameTime = Math.min(deltaTime, 0.250f);
        
        // Update the map state
        map.tick(frameTime);
        
        // Update the camera
        updateCamera();
        
        // Render the map on the screen
        renderMap();
        
        // Render the HUD on the screen
        /** Every 60 frames goes a second */
        if (tick == 0) {
            remainingTime--;
            tick = 60;
        }

        //After every start, the remaining time is displayed.
        hud.render(this.remainingTime);
        //If time is over, game screen appears.
        if (remainingTime <= 0d) {game.goToMenu();}


        /** tick counter for various activities */
        tick--;
    }
    
    /**
     * Updates the camera to follow the player but only 80% viewport (stated in the task)
     * 
     */
    private void updateCamera() {

        Player player = map.getPlayer();

        // first we need to get the actual players position
        float currentX = player.getX() * TILE_SIZE_PX * SCALE;
        float currentY = player.getY() * TILE_SIZE_PX * SCALE;


        mapCamera.position.x = currentX;
        mapCamera.position.y = currentY;


        mapCamera.update(); // This is necessary to apply the changes
    }
    
    private void renderMap() {
        // This configures the spriteBatch to use the camera's perspective when rendering
        spriteBatch.setProjectionMatrix(mapCamera.combined);
        
        // Start drawing
        spriteBatch.begin();
        
        // Render everything in the map here, in order from lowest to highest (later things appear on top)
        // You may want to add a method to GameMap to return all the drawables in the correct order

        
        // Loop through every coordinate -> Builts the map from the Groundup

        for (int y = map.getWidth(); y >= 0; y--) {
            for (int x = 0; x <= map.getWidth(); x++) {

                // 1. Draw Ground (Layer 0)
                Drawable floor = map.getGround(x, y);
                if (floor != null) {
                    draw(spriteBatch, floor);
                }

                

                // 3. Draw the nice Items or Objects like shovel, exit etc. (Layer 3)
                Drawable item = map.gethiddenObject(x, y);
                if (item != null && item.getCurrentAppearance() != null) {
                    draw(spriteBatch, item);
                }


            }
            }

            for (int y = map.getWidth(); y >= 0; y--) {
                for (int x = 0; x <= map.getWidth(); x++) {

                Drawable soil = map.getSoil(x, y);
                if (soil != null && soil.getCurrentAppearance() != null) {
                    draw(spriteBatch, soil);
                }
            }}

                
            for (int y = map.getWidth(); y >= 0; y--) {
                for (int x = 0; x <= map.getWidth(); x++) {

                // 2. Draw Obstacles (Layer 2) - Drawn ON TOP of ground
                Drawable obstacles = map.getObstacle(x, y);
                if (obstacles != null) {
                    draw(spriteBatch, obstacles);
                }


            
                // 4. Draw the Crops
                Drawable crop = map.getCrop(x, y);
                if (crop != null && crop.getCurrentAppearance() != null) {
                    draw(spriteBatch, crop);
                }
            }
        }
            
        

            /** Draw the chicken */
            for (Chicken chicken : map.getActiveChickens()) {
                if (chicken != null) {
                    draw(spriteBatch, chicken);
                }
                
            }


            draw(spriteBatch, map.getChest());
            draw(spriteBatch, map.getPlayer());
        
        // Finish drawing, i.e. send the drawn items to the graphics card
        spriteBatch.end();
    }
    
    /**
     * Draws this object on the screen.
     * The texture will be scaled by the game scale and the tile size.
     * This should only be called between spriteBatch.begin() and spriteBatch.end(), e.g. in the renderMap() method.
     * @param spriteBatch The SpriteBatch to draw with.
     */
    private static void draw(SpriteBatch spriteBatch, Drawable drawable) {
        TextureRegion texture = drawable.getCurrentAppearance();
    
        // 1. Calculate the size of the logical tile on screen
        float tilePx = TILE_SIZE_PX * SCALE; // e.g., 64 pixels

        // 2. Calculate the size of the sprite to draw
        float drawWidth = texture.getRegionWidth() * SCALE;
        float drawHeight = texture.getRegionHeight() * SCALE;

        // 3. Calculate Position
        // Base X/Y is the bottom-left corner of the TILE
        float baseX = drawable.getX() * tilePx;
        float baseY = drawable.getY() * tilePx;

    
        // 

        float drawX = baseX + (tilePx - drawWidth) / 2;

        // ALIGN BOTTOM vertically
        // This ensures the object's "feet" sit on the bottom of the tile
        // and the "head" sticks up into the tile above.
        float drawY = baseY; 

        spriteBatch.draw(texture, drawX, drawY, drawWidth, drawHeight);
    }
    
    /**
     * Called when the window is resized.
     * This is where the camera is updated to match the new window size.
     * @param width The new window width.
     * @param height The new window height.
     */
    @Override
    public void resize(int width, int height) {
        mapCamera.setToOrtho(false);
        hud.resize(width, height);
    }

    // Unused methods from the Screen interface
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}

package de.tum.cit.aet.valleyday.screen;

import de.tum.cit.aet.valleyday.map.Crop;
import de.tum.cit.aet.valleyday.map.Player;
import de.tum.cit.aet.valleyday.map.Shovel;
import de.tum.cit.aet.valleyday.texture.Drawable;
import de.tum.cit.aet.valleyday.texture.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;



/** Import the libraries for 2D Tables */
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


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
    /** Stage for the 2D Table to draw HUD */
    private final Stage stage;

    /** Hud needs to know the player */
    private Player player;
    private ShapeRenderer shapeRenderer;
    private float x;
    private float y;
    float hudWidth;
    float hudHeight;
    float margin;

   
    /**
     * Global variables for the Table HUD
     * 
     */
    private Label timeLabel;
    private Label cropsLabel;
    private Label exitLabel;

    /** Images for the HUD */

    Image shovelIcon  = new Image(Textures.SHOVEL);
    Image wateringCan = new Image(Textures.WATERING_CAN);
    Image fertilizer  = new Image(Textures.FERTILIZER);
    Image clock       = new Image(Textures.CLOCK);
    Image crop        = new Image(Textures.CORN_MATURING);

    // Cooloff for Displaying messages
    int fertilizerCooloff;
    int wateringCanCooloff;



    public Hud(SpriteBatch spriteBatch, BitmapFont font, Player player) {

        this.spriteBatch = spriteBatch;
        this.font = font;
        this.camera = new OrthographicCamera();
        this.stage = new Stage(new ScreenViewport(), spriteBatch);
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

        

        /**
         * Used the libGDX table2D libary for easy alignment and to avoid Magic Numbers.
         * 
         * We implemented a Root table and then added smaller tables which made the HUD nicely line up.
         * 
         * 
         * Generally we build it static with nested Tables and partitions.
         */

        // General Lable Style from libGDX
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE);


        Table rootTable = new Table();
        Table toolsTable = new Table(); // Nested tools table
        rootTable.setFillParent(true);
        rootTable.add(toolsTable).expand().top().left();
        toolsTable.pad(margin);
        stage.addActor(rootTable);

       
        // Make the Time Column
        timeLabel = new Label("", labelStyle);
        toolsTable.add(clock).size(30, 30).padRight(10);
        toolsTable.add(timeLabel).left(); 
        toolsTable.row();                

        // Make the Crops Column
        cropsLabel = new Label("", labelStyle);
        toolsTable.add(crop).size(30, 30).padRight(10);
        toolsTable.add(cropsLabel).left().padTop(5);
        toolsTable.row();                


        // Make extra Table only for the Icons
        Table toolsGrid = new Table();
        
        toolsGrid.add(fertilizer).size(30, 30).padLeft(5);
        toolsGrid.add(wateringCan).size(30, 30).padLeft(5);
        toolsGrid.add(shovelIcon).size(30, 30);
        

        // We need to add this one the the inner table
        toolsTable.add(toolsGrid).colspan(2).left().padTop(10);
        toolsTable.row();


        exitLabel = new Label("EXIT", labelStyle);
        toolsTable.add(exitLabel).colspan(2).center().padTop(20);

        /** CURRENTLY ONLY TESTING CODE -> NICE BACKGROUND WILL FOLLOW */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.5f, 0.3f, 0.1f, 0.8f)); 
        pixmap.fill();

        // Create a Texture from that pixel
        Texture solidTexture = new Texture(pixmap);

        // Create a Drawable that the Table can use
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(solidTexture));

        // Clean up the pixmap 
        pixmap.dispose();

        // Set the background of the table
        toolsTable.setBackground(backgroundDrawable);
        
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

        int minutes = (int) timeRemaining / 60;
        int seconds = (int) timeRemaining % 60;

        timeLabel.setText(String.format("%d:%02d", minutes, seconds));

        cropsLabel.setText("" + player.getCurrentHarvest() + "/" + player.getHarvesting());


        if (player.isWinning()) {
            exitLabel.setColor(0, 255, 0, 0.5f);
        } else {
            exitLabel.setColor(255, 0, 0, 0.5f);
        }

        if (player.hasShovel()) {
            shovelIcon.setVisible(true);
        }
        else {
            shovelIcon.setVisible(false);
        }
        
        String message = player.getMessageToDisplay();
        if (message != null && message.startsWith("Fertilizer")) {
            fertilizerCooloff = 120;
        }
        else if (message != null && message.startsWith("Watering")) {
            wateringCanCooloff = 120;
        }

        if (fertilizerCooloff >= 0) {
            fertilizer.setVisible(true);
        }
        else {
            fertilizer.setVisible(false);
        }
        if (wateringCanCooloff >= 0) {
            wateringCan.setVisible(true);
        }
        else {
            wateringCan.setVisible(false);
        }



        fertilizerCooloff--;
        wateringCanCooloff--;


        // Draw the HUD elements
        // font.draw(spriteBatch, "Press Esc to Pause!", 10, Gdx.graphics.getHeight() -
        // 10);
        // message for the Time left
        // font.draw(spriteBatch, "Time left: " + (int) timeRemaining / 60 + ":" + (int) timeRemaining % 60, 10,
        //         Gdx.graphics.getHeight() - 10);
        // // Displayes the harvested crops in the left hand corner.
        // font.draw(spriteBatch, "Harvested crops: " + player.getCurrentHarvest() + "/" + player.getHarvesting(), 10,
        //         Gdx.graphics.getHeight() - 40);
        // // Displayes the tools.
        // font.draw(spriteBatch, "Tools: ", 10, Gdx.graphics.getHeight() - 80);
        // /** Graphics for HUD  */

        // draw(spriteBatch, Textures.SHOVEL, 80, 80, 25, Gdx.graphics.getHeight() - 150);
        // // Displays the EXIT option.
        // // At the start the Exit font color is displayed red.
        // // If the the harvesting Quota is reached, font color is changed to green.
        // if (player.isWinning()) {
        //     font.setColor(0, 255, 0, 0.5f);
        // } else {
        //     font.setColor(255, 0, 0, 0.5f);
        // }
        // font.draw(spriteBatch, "EXIT ", 25, Gdx.graphics.getHeight() - 120);
        // font.setColor(255, 255, 255, 0.5f);

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


        // Adding the Stage Act from the libGDX
        stage.act(timeRemaining); // For animation stuff
        // Draws the HUD
        stage.draw();   

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

        stage.getViewport().update(width, height, true);
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

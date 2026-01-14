package de.tum.cit.aet.valleyday.screen;

import de.tum.cit.aet.valleyday.map.Crop;
import de.tum.cit.aet.valleyday.map.Player;
import de.tum.cit.aet.valleyday.map.Shovel;
import de.tum.cit.aet.valleyday.texture.Drawable;
import de.tum.cit.aet.valleyday.texture.Textures;
import de.tum.cit.aet.valleyday.screen.*;

import org.w3c.dom.Text;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture3D;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
/** Import the libraries for 2D Tables */
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.tum.cit.aet.valleyday.audio.SoundEffect;


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
    private GameScreen gameScreen;
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
    private Label scoreLabel;
    private Label exitLabel;
    private Label essentialsLabel;

    /** Images for the HUD */

    Image shovelIcon  = new Image(Textures.SHOVEL);
    Image wateringCan = new Image(Textures.WATERING_CAN);
    Image fertilizer  = new Image(Textures.FERTILIZER);
    Image clock       = new Image(Textures.CLOCK1);
    Image coin       = new Image(Textures.COIN);
    Image crop        = new Image(Textures.CORN_MATURING);

    Image crop1    = new Image(Textures.CORN_MATURING);


    Image heart1 = new Image(Textures.HEART); 
    Image heart2 = new Image(Textures.HEART);
    Image heart3 = new Image(Textures.HEART);
    
    
    com.badlogic.gdx.scenes.scene2d.utils.Drawable cornDrawable     = new TextureRegionDrawable(Textures.CORN_MATURING);
    com.badlogic.gdx.scenes.scene2d.utils.Drawable maisDrawable     = new TextureRegionDrawable(Textures.MAIS_MATURING);
    com.badlogic.gdx.scenes.scene2d.utils.Drawable lemonDrawable    = new TextureRegionDrawable(Textures.LEMON_MATURING);
    com.badlogic.gdx.scenes.scene2d.utils.Drawable sellerieDrawable = new TextureRegionDrawable(Textures.SELLERIE_MATURING);

    /** For the clock ticking */
    com.badlogic.gdx.scenes.scene2d.utils.Drawable clockHigh = (com.badlogic.gdx.scenes.scene2d.utils.Drawable) new TextureRegionDrawable(Textures.CLOCK1);
    com.badlogic.gdx.scenes.scene2d.utils.Drawable clock1    = (com.badlogic.gdx.scenes.scene2d.utils.Drawable) new TextureRegionDrawable(Textures.CLOCK2);
    com.badlogic.gdx.scenes.scene2d.utils.Drawable clock2    = (com.badlogic.gdx.scenes.scene2d.utils.Drawable) new TextureRegionDrawable(Textures.CLOCK3);
    com.badlogic.gdx.scenes.scene2d.utils.Drawable clock3    = (com.badlogic.gdx.scenes.scene2d.utils.Drawable) new TextureRegionDrawable(Textures.CLOCK4);
    com.badlogic.gdx.scenes.scene2d.utils.Drawable clockLow  = (com.badlogic.gdx.scenes.scene2d.utils.Drawable) new TextureRegionDrawable(Textures.CLOCK5);

    // Cooloff for Displaying messages
    int fertilizerCooloff;
    int wateringCanCooloff;
    int clockTicking = 240;


    /** To access the HUD via Gamescreen for Pausing and resuming option adding global Variables */
    private Table pauseTable;
    private Table exitTable;
    private Table gameOverTable;

    private Skin skin = new Skin(Gdx.files.internal("skin/craftacular/craftacular-ui.json"));


    public Hud(SpriteBatch spriteBatch, BitmapFont font, Player player, GameScreen gameScreen) {

        this.spriteBatch = spriteBatch;
        this.font = font;
        this.camera = new OrthographicCamera();
        this.stage = new Stage(new ScreenViewport(), spriteBatch);
        this.player = player;
        this.gameScreen = gameScreen;
        this.shapeRenderer = new ShapeRenderer();
        //Define global settings 
        hudWidth = 250;
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


        Table rootTable   = new Table();
        Table borderTable = new Table(); // Only for nice Border of the table
        Table toolsTable  = new Table(); // Nested tools table

        // toolsTable.setDebug(true); // Uncomment for nice Debugging
      
        rootTable.setFillParent(true);
        rootTable.add(borderTable).expand().top().left(); // Also restrict the size to avoid weird oversize
        rootTable.padTop(margin);
        rootTable.padLeft(margin);

        float marginBorder = 5f;

        borderTable.padLeft(marginBorder);
        borderTable.padRight(marginBorder);
        borderTable.padTop(marginBorder);
        borderTable.padBottom(marginBorder);

        borderTable.add(toolsTable);
        //borderTable.padLeft(margin); // align the table nicely

        toolsTable.pad(margin);
   
        stage.addActor(rootTable);

       
        // Make the Time Column
        timeLabel = new Label("", labelStyle);
        toolsTable.add(clock).size(30, 30).padRight(20);
        toolsTable.add(timeLabel).left().size(35, 25); 
        toolsTable.row();                

        // Make the Crops Column
        cropsLabel = new Label("", labelStyle);
        toolsTable.add(crop).size(30, 30).padRight(10);
        toolsTable.add(cropsLabel).left().padTop(5);
        toolsTable.row();                

        // Make the Time Column
        scoreLabel = new Label(": 0", labelStyle);
        toolsTable.add(coin).size(30, 30).padRight(10);
        toolsTable.add(scoreLabel).left().size(60,30).padTop(5);
        toolsTable.row(); 

        // Make extra Table only for the Icons
        Table toolsGrid = new Table();
        
        toolsGrid.add(fertilizer).size(30, 30).padLeft(10);
        toolsGrid.add(wateringCan).size(30, 30).padLeft(10);
        toolsGrid.add(shovelIcon).size(30, 30).padLeft(10);
        

        // We need to add this one the the inner table
        toolsTable.add(toolsGrid).colspan(2).left().padTop(10);
        toolsTable.row();

        Table exitShowTable = new Table();


        exitLabel = new Label("EXIT", labelStyle);
        exitShowTable.add(exitLabel).colspan(3).center().padTop(10).padLeft(10);

        toolsTable.add(exitShowTable).center().colspan(2);

        // Texture branchTexture = new Texture(Gdx.files.internal("assets/texture/Wood/885.jpg"));

        // TextureRegionDrawable branchDrawable = new TextureRegionDrawable(new TextureRegion(branchTexture));

        // borderTable.setBackground(branchDrawable);

        

        /** We reuse the same logic to make a Black Border of the Table */
        Pixmap blackBorder = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        blackBorder.setColor(new Color(0.26f, 0.16f, 0.06f, 1f)); 
        blackBorder.fill();
        // Create a Texture from that pixel
        Texture blackTexture = new Texture(blackBorder);
        // Create a Drawable that the Table can use
        TextureRegionDrawable blackBorders = new TextureRegionDrawable(new TextureRegion(blackTexture));
        // Clean up the pixmap 
        blackBorder.dispose();
        // Set the background of the table
        borderTable.setBackground(blackBorders);

        /** CURRENTLY ONLY TESTING CODE -> NICE BACKGROUND WILL FOLLOW */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.82f, 0.57f, 0.20f, 1f)); 
        // comments for filling
        pixmap.fill();
        // Create a Texture from that pixel
        Texture solidTexture = new Texture(pixmap);
        // Create a Drawable that the Table can use
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(solidTexture));
        // Clean up the pixmap 
        pixmap.dispose();
        // Set the background of the table
        toolsTable.setBackground(backgroundDrawable);


/**
         * Switches and displays the different Crop types && health bar
         */
        Table essentialsTable = new Table();
        Table borderTwoTable = new Table(); 
        Table displayTable = new Table();   

        stage.addActor(essentialsTable);
        essentialsTable.setFillParent(true);
        
        // Position top-right
        essentialsTable.add(borderTwoTable).expand().top().right(); 
        essentialsTable.padTop(margin);
        essentialsTable.padRight(margin);

        // Style the outer border
        borderTwoTable.setBackground(blackBorders); 
        borderTwoTable.pad(marginBorder);
        borderTwoTable.add(displayTable);

        // Style the inner content
        displayTable.setBackground(backgroundDrawable); 
        displayTable.pad(10); 

     
        Table cropRow = new Table();
        essentialsLabel = new Label("Yields PTS: 1", labelStyle); 
        
        cropRow.add(crop1).size(30, 30).padRight(10); 
        cropRow.add(essentialsLabel).left();
        
        displayTable.add(cropRow).left().row(); 
    
        Image separator = new Image(blackBorders); 
        displayTable.add(separator).growX().height(2).padTop(5).padBottom(5).row();

    
        Table healthRow = new Table();
        
        
        healthRow.add(heart1).size(25, 25).padRight(5);
        healthRow.add(heart2).size(25, 25).padRight(5);
        healthRow.add(heart3).size(25, 25);
        
        displayTable.add(healthRow).left();





        /**
         * 
         * Make the Pause Table for pausing and resuming options
         * 
         */

        this.pauseTable = new Table();

        pauseTable.setFillParent(true); // Make the table fill the stage
        stage.addActor(pauseTable); // Add the table to the stage

        // Add a label as a title
        pauseTable.add(new Label("Welcome warrior, take a Rest!", this.skin)).padBottom(40).row();

        // Create and add a button to go to the game screen
        TextButton resumeButtom     = new TextButton("Resume the game.", this.skin);
        TextButton settingsButton   = new TextButton("Settings.", this.skin);
        TextButton scaryButton      = new TextButton("Become a lost warrior!", this.skin);


        pauseTable.add(resumeButtom).width(425).row();
        pauseTable.row();
        pauseTable.add(settingsButton).width(425).row();
        pauseTable.row();
        pauseTable.add(scaryButton).width(425).row();
        
        pauseTable.setVisible(false); // Set the pauseTable to False by Default

        /**
         * We have to define several EventListener like HTML
         */

        resumeButtom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.resume();
            }
        });

        scaryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.scared();
            }
        });



        /**
         * 
         * Make EXIT and WINNING BUTTONS
         * => Will be displayed if the Player fullfills the Crop quota
         */

        this.exitTable = new Table();

        exitTable.setFillParent(true); // Make the table fill the stage
        stage.addActor(exitTable); // Add the table to the stage

        // Add a label as a title
        exitTable.add(new Label("YOU WON THE GAME, what next", this.skin)).padBottom(40).row();

        // Create and add a button to go to the game screen
        TextButton winningButtom       = new TextButton("Keep Farming!", this.skin);
        TextButton advanceButton       = new TextButton("Advance to the next level!", this.skin);
        TextButton continueButton      = new TextButton("Enjoy the Harvest and make Beer!", this.skin);


        exitTable.add(winningButtom).width(650).row();
        exitTable.row();
        exitTable.add(advanceButton).width(650).row();
        exitTable.row();
        exitTable.add(continueButton).width(650).row();
        exitTable.row();
       
        exitTable.setVisible(false); // Set the pauseTable to False by Default

        // Create gameOver table
        this.gameOverTable = new Table();
        gameOverTable.setFillParent(true);
        stage.addActor(gameOverTable);

        // Create Label which displays that player is an idiot
        gameOverTable.add(new Label("YOU LOST!!", this.skin)).padBottom(40).row();

        // Create button which lets player go to the menuScreen
        TextButton losingButton = new TextButton("GET LOST !!!!", this.skin);

        gameOverTable.add(losingButton).width(650).row();
        gameOverTable.setVisible(false);
    

        losingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.gameOver();
                
            }
        });

        /**
         * We have to define several EventListener like HTML
         */

        winningButtom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                continueFarming();
                gameScreen.resume();
                
            }
        });

        advanceButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.getGame().nextLevel(); // intialize next level
                
            }
        });

        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // We make the nice anonymous class !:)
                Dialog dialog = new Dialog("Already leaving, are you scared?", skin) {

                @Override
                protected void result(Object answerOfUser) {
                    boolean exit = (Boolean) answerOfUser;
                    if (exit) {
                        gameScreen.scared(); // Close the game if true
                        } 
                    else {
                        // If false, the dialog just closes automatically
                        System.out.println("The warrior stays!"); 
                        }
                    }
                };

                dialog.text("Are you sure you want to quit?");



                dialog.button("Yes", true);  // Sends 'true' to result()
                dialog.button("No", false);  // Sends 'false' to result()


                dialog.show(stage);
                
                
            }
        });

        exitTable.setVisible(false); // By default set to false



        // /** Debugging -> uncomment if needed */
        // toolsTable.setDebug(true); // Uncomment for nice Debugging
        // exitTable.setDebug(true); // Uncomment for nice Debugging
        // pauseTable.setDebug(true); // Uncomment for nice Debugging
    }

    /**
     * Renders the HUD on the screen.
     * This uses a different OrthographicCamera so that the HUD is always fixed on
     * the screen.
     */
    public void render(float timeRemaining) {

        //shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
//
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(0, 0, 0, 0.0f);
        //shapeRenderer.rect(x, y, hudWidth, hudHeight);
        //shapeRenderer.end();
        // Render from the camera's perspective
        spriteBatch.setProjectionMatrix(camera.combined);
        // Start drawing
        spriteBatch.begin();


        // Draw the HUD elements
        font.draw(spriteBatch, "Esc to Pause!",  Gdx.graphics.getWidth() - 250, 30);

















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

        int currentOption = player.getOption();

        // Switches through the available crops which bring different points
        if (currentOption == 0) {
            crop1.setDrawable(cornDrawable);
            essentialsLabel.setText("Yields PTS: 1");
        } 
        else if (currentOption == 1) {
            crop1.setDrawable(maisDrawable);
            essentialsLabel.setText("Yields PTS: 1");
        }
        else if (currentOption == 2) {
            crop1.setDrawable(lemonDrawable);
            essentialsLabel.setText("Yields PTS: 2");
        }
        else if (currentOption == 3) {
            crop1.setDrawable(sellerieDrawable);
            essentialsLabel.setText("Yields PTS: 3");
        }

        int currentHealth = player.getHealth();

        
        if (currentHealth >= 3) {
            heart1.setVisible(true);
            heart2.setVisible(true);
            heart3.setVisible(true);
        } 
        else if (currentHealth == 2) {
            heart1.setVisible(true);
            heart2.setVisible(true);
            heart3.setVisible(false);
        } 
        else if (currentHealth == 1) {
            heart1.setVisible(true);
            heart2.setVisible(false);
            heart3.setVisible(false);
        } 
        else {
            // Dead (0 or less)
            heart1.setVisible(false);
            heart2.setVisible(false);
            heart3.setVisible(false);
        }


        /** For score just set the TEXT of the scoreLabel */
        scoreLabel.setText(": " + gameScreen.getGame().getScore());

        /** Need to reset only after time passes */
        if (clockTicking <= 0) {
            clockTicking = 120; 
        }

        if (clockTicking <= 0) {
            
            clock.setDrawable(clockLow);
        } 
        else if (clockTicking <= 30) {
            
            clock.setDrawable(clock3);
        } 
        else if (clockTicking <= 60) {
            clock.setDrawable(clock2);
        } 
        else if (clockTicking <= 90) {
            
            clock.setDrawable(clock1);
        }
        else {
            clock.setDrawable(clockHigh);
        }


        



        fertilizerCooloff--;
        wateringCanCooloff--;
        clockTicking--;
        

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

        x = width - hudWidth - margin;
        y = height - hudHeight - margin;
    }

    
    /**
     * Will be called by the GameScreen when the user presses ESC -> i.e. Pauses the Game
     * 
     * Also triggers the inputProcessor for the interactive pauseButtons 
     * 
     * @param isPaused
     */
    public void setPaused(boolean isPaused) {
        this.pauseTable.setVisible(isPaused);
        if (isPaused) {
            Gdx.input.setInputProcessor(stage);
        }
        else {
            Gdx.input.setInputProcessor(null);
        } 
    }


    public void showVictoryMenu() {
        this.exitTable.setVisible(true);
        Gdx.input.setInputProcessor(stage);
        SoundEffect.NEXTLEVEL.play();
    }

    public void continueFarming() {
        this.exitTable.setVisible(false);
        Gdx.input.setInputProcessor(null);
    }

    public void showLosingMenu() {
        this.gameOverTable.setVisible(true);
        Gdx.input.setInputProcessor(stage);
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Stage getStage() {
        return stage;
    }

    public Player getPlayer() {
        return player;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHudWidth() {
        return hudWidth;
    }

    public float getHudHeight() {
        return hudHeight;
    }

    public float getMargin() {
        return margin;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public Label getCropsLabel() {
        return cropsLabel;
    }

    public Label getExitLabel() {
        return exitLabel;
    }

    public Image getShovelIcon() {
        return shovelIcon;
    }

    public Image getWateringCan() {
        return wateringCan;
    }

    public Image getFertilizer() {
        return fertilizer;
    }

    public Image getClock() {
        return clock;
    }

    public Image getCrop() {
        return crop;
    }

    public com.badlogic.gdx.scenes.scene2d.utils.Drawable getClockHigh() {
        return clockHigh;
    }

    public com.badlogic.gdx.scenes.scene2d.utils.Drawable getClock1() {
        return clock1;
    }

    public com.badlogic.gdx.scenes.scene2d.utils.Drawable getClock2() {
        return clock2;
    }

    public com.badlogic.gdx.scenes.scene2d.utils.Drawable getClock3() {
        return clock3;
    }

    public com.badlogic.gdx.scenes.scene2d.utils.Drawable getClockLow() {
        return clockLow;
    }

    public int getFertilizerCooloff() {
        return fertilizerCooloff;
    }

    public int getWateringCanCooloff() {
        return wateringCanCooloff;
    }

    public int getClockTicking() {
        return clockTicking;
    }

    public Table getPauseTable() {
        return pauseTable;
    }

    public Table getExitTable() {
        return exitTable;
    }

    public Table getGameOverTable() {
        return gameOverTable;
    }

    public Skin getSkin() {
        return skin;
    }


    
}



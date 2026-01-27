package de.tum.cit.aet.valleyday.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.tum.cit.aet.valleyday.ValleyDayGame;
import de.tum.cit.aet.valleyday.audio.MusicTrack;

/**
 * 
 * The level intro screen displays the Pictures for the Story if the player chooses to start the story
 * Its implementing its own screen and will be created by the ValleyDayGame
 * We always have to insert an array of pictures, which will be sequentially played on the Screen
 * It acts as a invisible Table which becomes visible once the Screen gets created
 * Once the player clicks the button the real game will be started
 * 
 * 
 */
public class LevelIntroScreen implements Screen {

    private final ValleyDayGame game;
    private final SpriteBatch spriteBatch;
    private final Stage stage;
    private final Texture[] frames;
    private final Image image;
    private final TextButton continueButton;
    private final float frameDurationSeconds;
    private float frameTimer;
    private int frameIndex;

    /**
     * Creates a new LevelIntroScreen which takes the whole screenViewport and displayes images across the viewport
     * 
     * @param game the current game
     * @param framePaths the filepaths like .png and so on
     * @param frameDurationSeconds seconds for the diashow
     * @param mapIndex the current frame
     */
    public LevelIntroScreen(ValleyDayGame game, String[] framePaths, float frameDurationSeconds, int mapIndex) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        this.stage = new Stage(new ScreenViewport(), spriteBatch); // takes the whole damn screen as the stage
        MusicTrack.stopAll(); // stops borring music
        
        
        MusicTrack.INTRO.play(); // plays the super epic music
        
        if (mapIndex == 0) {
            
            MusicTrack.EPICVOICE.play(); // plays the super ultra epic voice
        }
        

        /**
         * Makes a new frames array and puts new Textures for each
         */
        this.frames = new Texture[framePaths.length];
        for (int i = 0; i < framePaths.length; i++) {
            frames[i] = new Texture(Gdx.files.internal(framePaths[i]));
        }
        this.frameDurationSeconds = frameDurationSeconds;

        this.image = new Image(new TextureRegionDrawable(new TextureRegion(frames[0]))); // makes a NEW libGDX image for the first one
        this.image.setFillParent(true); // sets it as stage (javaFX) meaning the whole screen as the stage is parent
        stage.addActor(image);

        
        this.continueButton = new TextButton("Begin Level", game.getSkin());

        /**
         * Adds a button for starting the game once the animation is finished
         */
        Table buttonTable = new Table(); // use a simple table
        buttonTable.setFillParent(true);
        buttonTable.bottom();
        buttonTable.add(continueButton).width(320).padBottom(30); // add the button to the table
        stage.addActor(buttonTable);

        this.continueButton.setVisible(frames.length <= 1); 
        
        continueButton.addListener(new ChangeListener() {
            /** Starts the game from the intro screen. */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.startGame(); // starts the game
            }
        });
    }

    /*
    * sets the InputProcessor -> meaning makes the button reactive
    */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders each image as a diashow -> Will increment the index and change the image
     * The delta of the frametime will decide when to change to the next picture
     * 
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        /**
         * If player touches anything while starting will skip the intro and instantly start the game
         */
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.startGame();
            return;
        }

        /**
         * Loops through the images and makes the intro
         */
        if (frameIndex < frames.length - 1) {
            frameTimer += delta;
            if (frameTimer >= frameDurationSeconds) { // if the frameTimer is smaller than the defined time will jump to the next picture
                frameTimer = 0f; // sets timer always to zero as we want around 6 seconds for each frame
                frameIndex++;
                image.setDrawable(new TextureRegionDrawable(new TextureRegion(frames[frameIndex]))); // sets the new picture
                if (frameIndex == frames.length - 1) {
                    continueButton.setVisible(true); // once the frames are ended we display the button for startign the awesome game
                }
            }
        }

        stage.act(delta);
        stage.draw();
    }

    /**
     * resizes the screen as per documentation of libGDX
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /** No-op for intro pause. */
    @Override
    public void pause() {
    }

    /** No-op for intro resume. */
    @Override
    public void resume() {
    }

    /** Clears input processor when hidden. */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * LibGDX disposal, cleaning storage
     */
    @Override
    public void dispose() {
        stage.dispose();
        for (Texture texture : frames) {
            if (texture != null) {
                texture.dispose();
            }
        }
    }
}

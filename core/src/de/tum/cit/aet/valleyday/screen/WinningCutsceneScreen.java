package de.tum.cit.aet.valleyday.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.tum.cit.aet.valleyday.ValleyDayGame;
import de.tum.cit.aet.valleyday.audio.MusicTrack;

/* Class WinningCutsceneScreen implements the screen interface. 
 * The constructor sets the the entire Screen as the ViewPort via stage.
*/
public class WinningCutsceneScreen implements Screen {

    private static final float FRAME_DURATION = 6.0f;

    private final ValleyDayGame game;
    private final SpriteBatch spriteBatch;
    private final Stage stage;
    private final Texture[] frames;
    private final Image image;
    private final TextButton toMenuButton;
    private float frameTimer;
    private int frameIndex;


    /**
     * Initializes the WinningCutSceneScreen and takes the ValleyDayGame is a parameter.
     * The stage is set to the ScreenViewPort meaning the stage is the entire screen.
     * @param game
     */
    public WinningCutsceneScreen(ValleyDayGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        this.stage = new Stage(new ScreenViewport(), spriteBatch);



        /* When winningCutSceneScreen is playing, every image in the Array "frames" is accessed and shown in sequence.
        * The "EPICAUDIO" and "WINNING" tracks are playing. 
        */
        this.frames = new Texture[] {
            new Texture(Gdx.files.internal("cutscenes/WinScreen/0.png")),
            new Texture(Gdx.files.internal("cutscenes/WinScreen/1.png")),
            new Texture(Gdx.files.internal("cutscenes/WinScreen/2.png")),
            new Texture(Gdx.files.internal("cutscenes/WinScreen/3.png")),
            new Texture(Gdx.files.internal("cutscenes/WinScreen/4.png")),
            new Texture(Gdx.files.internal("cutscenes/WinScreen/5.png")),
            new Texture(Gdx.files.internal("cutscenes/WinScreen/Credits.png"))
        };

        MusicTrack.stopAll();
        MusicTrack.EPICOUTRO.play();
        MusicTrack.WINNING.play();

        this.image = new Image(new TextureRegionDrawable(new TextureRegion(frames[0])));
        this.image.setFillParent(true); // setFillParent is true and the image is displayed on the entire screen.
        stage.addActor(image);

        // Creates a Button the lets you return to the menu, it is not visible until the last images in the frames Array is shown. 
        this.toMenuButton = new TextButton("Back to Menu", game.getSkin());
        this.toMenuButton.setVisible(false);

        // A new Table is created and the toMenuButton is added inside the Table. 
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.bottom();
        buttonTable.add(toMenuButton).width(300).padBottom(30);
        stage.addActor(buttonTable);

        // The eventListener adds functionality to the toMenuButton and when pressed, you return to the menu screen.
        toMenuButton.addListener(new ChangeListener() {
            /** Returns to the main menu after the cutscene. */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu();
            }
        });
    }

    /** Enables input for the cutscene screen. */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /** Advances cutscene frames and renders the stage. */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        // The Array is itarated trough in the render function. 
        // On the last image, the goToMenu Button is set to true and is displyed as a result. 
        if (frameIndex < frames.length - 1) {
            frameTimer += delta;
            if (frameTimer >= FRAME_DURATION) {
                frameTimer = 0f;
                frameIndex++;
                image.setDrawable(new TextureRegionDrawable(new TextureRegion(frames[frameIndex])));
                if (frameIndex == frames.length - 1) {
                    toMenuButton.setVisible(true);
                }
            }
        }

        stage.act(delta);
        stage.draw();
    }

    /** Updates the stage viewport on resize. */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /** No-op for cutscene pause. */
    @Override
    public void pause() {
    }

    /** No-op for cutscene resume. */
    @Override
    public void resume() {
    }

    /** Clears input when this screen is hidden. */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /** Disposes of textures and stage resources. */
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

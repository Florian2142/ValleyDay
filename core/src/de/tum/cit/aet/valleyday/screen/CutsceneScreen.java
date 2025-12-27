package de.tum.cit.aet.valleyday.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.aet.valleyday.ValleyDayGame;

/**
 * THIS class is specifically NOT for the standard game its about making a working easter egg here.
 * 
 * It would be interesting to make an actual Cutscene 
 * 
 * 
 * WE WILL FIGURE THIS ONE OUT AFTER IMPLEMENTING THE MVP
 * 
 * 
 * I IMPORTED THIS CODE FROM GEMINI.COM 
 * 
 */

public class CutsceneScreen implements Screen {

    private final ValleyDayGame game;
    private final SpriteBatch batch;
    private Animation<TextureRegion> videoAnimation;
    private float stateTime;

    public CutsceneScreen(ValleyDayGame game) {
        this.game = game;
        this.batch = game.getSpriteBatch();
        
        // Load the frames but right now the MP4 video 
        TextureRegion[] frames = new TextureRegion[100]; // However many frames you have
        for(int i = 0; i < 100; i++) {
            /// FRAME BY FRAME.
        }
        
        // Create animation (0.1f = 10 frames per second)
        videoAnimation = new Animation<>(0.1f, frames);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Black background
        stateTime += delta;

        TextureRegion currentFrame = videoAnimation.getKeyFrame(stateTime, false);
        
        batch.begin();
        // Draw full screen
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Check if animation is finished
        if (videoAnimation.isAnimationFinished(stateTime)) {
            // Cutscene over, go back to game!
            game.goToGame(); 
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {
        // Dispose textures here if you loaded them manually
    }
}
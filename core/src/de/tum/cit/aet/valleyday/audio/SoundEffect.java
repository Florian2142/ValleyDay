package de.tum.cit.aet.valleyday.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Short sound effects (SFX). Prefer Sound over Music for one-shot effects.
 */
public enum SoundEffect {

    BRANCHES("branchesShort.wav", 0.6f),
    STEPS_DIRT("walk-on-dirt-3-291983.mp3", 0.15f),
    
    GAMEOVER("pixel-death-66829.mp3", 0.5f),
    
    NEXTLEVEL("cute-level-up-3-189853.mp3", 0.75f);
   

    private final Sound sound;
    private final float volume;

    SoundEffect(String fileName, float volume) {
        this.sound = Gdx.audio.newSound(Gdx.files.internal("audio/" + fileName));
        this.volume = volume;
    }

    public void play() {
        sound.play(volume);
    }
}

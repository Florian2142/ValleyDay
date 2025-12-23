package de.tum.cit.aet.valleyday.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Short sound effects (SFX). Prefer Sound over Music for one-shot effects.
 */
public enum SoundEffect {

    BRANCHES("branchesShort.wav", 0.6f),
    STEPS_DIRT("steps2.wav", 0.6f);

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

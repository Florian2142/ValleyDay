package de.tum.cit.aet.valleyday.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Short sound effects (SFX). Prefer Sound over Music for one-shot effects.
 */
public enum SoundEffect {

    BRANCHES("branchesShort.wav", 0.6f),
    STEPS_DIRT("walk-on-dirt-3-291983.mp3", 0.15f),
    SWORD_SLICE("sword-slice-2-393845.mp3", 0.25f),
    GAMEOVER("pixel-death-66829.mp3", 0.5f),
    CROP_PICKUP("CropPickup.wav", 0.3f),
    CROP_PLANTING("walk-on-dirt-1-291981.mp3", 0.7f),
    EQUIP("equip-sound-272428.mp3", 0.7f),
    NEXTLEVEL("cute-level-up-3-189853.mp3", 0.75f),
    DEBRISREMOVAL("branch-drag-329004.mp3", 0.25f);

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

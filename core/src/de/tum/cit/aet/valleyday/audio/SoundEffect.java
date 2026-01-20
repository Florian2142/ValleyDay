package de.tum.cit.aet.valleyday.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Short sound effects (SFX). Prefer Sound over Music for one-shot effects.
 */
public enum SoundEffect {

    BRANCHES("branchesShort.wav", 0.6f),
    STEPS_DIRT("walk-on-dirt-3-291983.mp3", 0.15f),
    EQUIP("equip-sound-272428.mp3", 0.33f),
    GAMEOVER("pixel-death-66829.mp3", 0.5f),
    CROP_PICKUP("CropPickup.wav", 0.33f),
    CROP_PLANTING("steps2.wav", 0.33f),
    DEBRISREMOVAL("confirm-tap-394001.mp3", 0.33f),
    SWORD_SLICE("sword-slice-2-393845.mp3", 0.33f),
    EXPLOSION("explosion-8-bit-14-314686.mp3", 1f),
    SLASH("sword-blade-slicing-flesh-352708.mp3", 0.7f),
    SPIDERHISS("SpiderHiss.wav", 0.25f),
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

package de.tum.cit.aet.valleyday.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * This enum is used to manage the music tracks in the game.
 * Currently, only one track is used, but this could be extended to include multiple tracks.
 * Using an enum for this purpose is a good practice, as it allows for easy management of the music tracks
 * and prevents the same track from being loaded into memory multiple times.
 * See the assets/audio folder for the actual music files.
 * Feel free to add your own music tracks and use them in the game!
 */
public enum MusicTrack {
    
    BACKGROUND("background.mp3", 0.1f),
    EASTER_EGG("level-ten-8-bit-pixel-warriors-chapter-one-415692.mp3", 0.33f),
    GAME("game-music-loop-6-144641.mp3", 0.15f),
    MAP2("minecraft-run-music-394978 (1).mp3", 0.15f),
    MAP3("winter-pixel-422896.mp3", 0.15f),
    MAP4("pixel-245147 (2).mp3", 0.15f);

    
    /** The music file owned by this variant. */
    private final Music music;
    
    MusicTrack(String fileName, float volume) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + fileName));
        this.music.setLooping(true);
        this.music.setVolume(volume);
    }
    
    /**
     * Play this music track.
     * This will not stop other music from playing
     */
    public void play() {
        this.music.play();
    }

    public void stop() {
        this.music.stop();
    }

    /** 
     * Checks if the music is currently playing.
     * @return true if playing, false otherwise.
     */
    public boolean isPlaying() {
        return this.music.isPlaying();
    }
}

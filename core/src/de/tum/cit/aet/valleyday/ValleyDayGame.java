package de.tum.cit.aet.valleyday;

import java.io.File;
import java.util.logging.FileHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.tum.cit.aet.valleyday.audio.MusicTrack;
import de.tum.cit.aet.valleyday.map.GameMap;
import de.tum.cit.aet.valleyday.map.mapInputExcepetion;
import de.tum.cit.aet.valleyday.screen.GameScreen;
import de.tum.cit.aet.valleyday.screen.MenuScreen;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;

/**
 * The ValleyDayGame class represents the core of the Valley Day game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class ValleyDayGame extends Game {

    /**
     * Sprite Batch for rendering game elements.
     * This eats a lot of memory, so we only want one of these.
     */
    private SpriteBatch spriteBatch;

    /** The game's UI skin. This is used to style the game's UI elements. */
    private Skin skin;
    
    /**
     * The file chooser for loading map files from the user's computer.
     * This will give you access to a {@link com.badlogic.gdx.files.FileHandle} object,
     * which you can use to read the contents of the map file as a String, and then parse it into a {@link GameMap}.
     */
    private final NativeFileChooser fileChooser;

    private FileHandle pendingMapFile;

    
    
    /**
     * The map. This is where all the game objects are stored.
     * This is owned by {@link ValleyDayGame} and not by {@link GameScreen}
     * because the map should not be destroyed if we temporarily switch to another screen.
     */
    private GameMap map;

    // Make a global variable
    private String difficulty;

    /**
     * Constructor for ValleyDayGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public ValleyDayGame(NativeFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    } 

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     * During the class constructor, libGDX is not fully initialized yet.
     * Therefore this method serves as a second constructor for the game,
     * and we can use libGDX resources here.
     */
    @Override
    public void create() {

        /**
         * Note: We cannot initiate the spriteBatch and load textures because the graphics card connection hasn't been established yet.
         * 
         */
        this.spriteBatch = new SpriteBatch(); // Create SpriteBatch for rendering
        this.skin = new Skin(Gdx.files.internal("skin/craftacular/craftacular-ui.json")); // Load UI skin

        MusicTrack.BACKGROUND.play(); // Play some background music
        goToMenu(); // Navigate to the menu screen

        
    }

        // THe actualy map is already handled in the NativeFileChooser in the ChosenFile

        // -> So we make our own maps here!!! 

        /**
         * 1.0: Via the Desktopmanager we already give the finsihed object of the Filechooser and pass it into the game
         * 2.0: Once we have the Filechooser in the game we can just use it in order to select the maps we want
         * 3.0: For that we first have to choose the setting and set it to the path of the maps
         * 4.0: Then we leverage the onFileChosen which makes a pop-up of the maps
         * 5.0: We create a new GameMap and pass the selected file for the map creation
         *
         * Possible Improvements: Do not choose the file via the explorer but instead ingame choosing
         * 
         * 
        */

    public void selectMap() {
        NativeFileChooserConfiguration config = new NativeFileChooserConfiguration();

        config.directory = Gdx.files.local("itp2526itp2526projectwork-trycatchreturn35\\desktop\\src");
        
        // pick the right file
        this.fileChooser.chooseFile(config, new NativeFileChooserCallback() {

        @Override
        public void onFileChosen(FileHandle file) {
            // return the map we have chosen by the user

            System.out.println("File selected: " + file.name());

            ValleyDayGame.this.pendingMapFile = file;

            System.out.println("Map loaded! Please select difficulty next.");

        }

                @Override
            public void onCancellation() {
                System.out.println("User cancelled.");
            }
        
            @Override
            public void onError(Exception exception) {
                System.err.println("Error picking file:");
                exception.printStackTrace(); // This will tell us the REAL reason if it fails again
            }
        
    });

       
    }

    public void startGame() {
        // Check if we have everything we need
        if (this.pendingMapFile == null) {
            System.err.println("Cannot start: No map selected!");
            return;
        }

        if (this.difficulty == null) {
            System.err.println("Cannot start: No difficulty selected! Default will be medium due to your Indecisiveness.");
            this.difficulty = "Medium";
        }

        try {
            // Create the map now that we have all ingredients
            this.map = new GameMap(this, this.pendingMapFile, this.difficulty);

            // Switch to the game screen
            goToGame();

        } catch (Exception e) {
            System.err.println("Failed to create map: " + e.getMessage());
            e.printStackTrace();
        }
}

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
    }

    /** Returns the skin for UI elements. */
    public Skin getSkin() {
        return skin;
    }

    /** Returns the main SpriteBatch for rendering. */
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
    
    /** Returns the current map, if there is one. */
    public GameMap getMap() {
        return map;
    }
    
    /**
     * Switches to the given screen and disposes of the previous screen.
     * @param screen the new screen
     */
    @Override
    public void setScreen(Screen screen) {
        Screen previousScreen = super.screen;
        super.setScreen(screen);
        if (previousScreen != null) {
            previousScreen.dispose();
        }
    }

    /** Cleans up resources when the game is disposed. */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }


    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    
}

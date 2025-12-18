package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.aet.valleyday.ValleyDayGame;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the game map.
 * Holds all the objects and entities in the game.
 */
public class GameMap {
    
    // A static block is executed once when the class is referenced for the first time.
    static {
        // Initialize the Box2D physics engine.
        com.badlogic.gdx.physics.box2d.Box2D.init();
    }
    
    // Box2D physics simulation parameters (you can experiment with these if you want, but they work well as they are)
    /**
     * The time step for the physics simulation.
     * This is the amount of time that the physics simulation advances by in each frame.
     * It is set to 1/refreshRate, where refreshRate is the refresh rate of the monitor, e.g., 1/60 for 60 Hz.
     */
    private static final float TIME_STEP = 1f / Gdx.graphics.getDisplayMode().refreshRate;
    /** The number of velocity iterations for the physics simulation. */
    private static final int VELOCITY_ITERATIONS = 6;
    /** The number of position iterations for the physics simulation. */
    private static final int POSITION_ITERATIONS = 2;
    /**
     * The accumulated time since the last physics step.
     * We use this to keep the physics simulation at a constant rate even if the frame rate is variable.
     */
    private float physicsTime = 0;
    
    /** The game, in case the map needs to access it. */
    private final ValleyDayGame game;
    /** The Box2D world for physics simulation. */
    private final World world;
    
    // Game objects
    private final Player player;
    
    private final Chest chest;
    
    private final Tiles[][] tiles;

    // private final int mapSize;

    public GameMap(ValleyDayGame game, FileHandle map) throws mapInputExcepetion{
        this.game = game;
        this.world = new World(Vector2.Zero, true);
        // Create a player with initial position (1, 3)
        this.player = new Player(this.world, 0, 0); // -> Reset player to (1, 1) as our starting point
        // Create a chest in the middle of the map
        this.chest = new Chest(world, 3, 3);

        /*
        * Here we follow the recommended procedure of the project description which is
        * 1.0 Store the whole file content in one very big string
        * 2.0 Split the string at each \n = newline
        * 3.0 Split each line by "="
        * 3.1 Make sure to split each empty line or comment ("#")
        * 
        */
        String mapString = map.readString(); // 1.0

        String[] newlines = mapString.split("\n"); // 2.0

        // make new HashMap -> Algorithmic fast lookup O(1) -> Ammortized average case 
        HashMap<String, String> tileVectorTypes = new HashMap<>();

        String curLine;
        String[] currTile;
   

        /**
         * 
         * Here we basically stream and read the map input 
         * 
         * IMPROVEMENTS: REGEX for identifying faulty input
         */

        for (int i = 0; i < newlines.length; i++) {
            // get current Line
            curLine = newlines[i];
            
            if (curLine.startsWith("#") || curLine == null) {
                continue;
            }

            try {
                // try putting each tile into
                currTile = newlines[i].split("=");
                tileVectorTypes.put(currTile[0], currTile[1]); 
                System.out.println(newlines[i]);

            } catch (Exception e) {
                throw new mapInputExcepetion("Map contained faulty input. Input must follow: int,int=int (Comments, i.e. Hashtags are allowed)");
            }


           
        }

        // Create flowers in a 7x7 grid
        this.tiles = new Tiles[7][7];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                this.tiles[i][j] = new Tiles(i, j, TileType.DIRT);
            }
        }

        // Take the map as input and store the given types with a big switch statement
        // this.mapSize = (int ) Math.sqrt(tileVectorTypes.size());

        // tiles = new Tiles[mapSize][mapSize];

        // int row;
        // int col;

        // for (String entry : tileVectorTypes.keySet()) {
        //     row = Integer.valueOf(entry.split(",")[0]);
        //     col = Integer.valueOf(entry.split(",")[1]);

            
        //     tiles[row][col] = new Tiles(row, col, TileType.DIRT);
        //     System.out.println(tiles[row][col]);
        // }
        
    }
    
    /**
     * Updates the game state. This is called once per frame.
     * Every dynamic object in the game should update its state here.
     * @param frameTime the time that has passed since the last update
     */
    public void tick(float frameTime) {
        this.player.tick(frameTime);
        doPhysicsStep(frameTime);
    }
    
    /**
     * Performs as many physics steps as necessary to catch up to the given frame time.
     * This will update the Box2D world by the given time step.
     * @param frameTime Time since last frame in seconds
     */
    private void doPhysicsStep(float frameTime) {
        this.physicsTime += frameTime;
        while (this.physicsTime >= TIME_STEP) {
            this.world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            this.physicsTime -= TIME_STEP;
        }
    }
    
    /** Returns the player on the map. */
    public Player getPlayer() {
        return player;
    }
    
    /** Returns the chest on the map. */
    public Chest getChest() {
        return chest;
    }
    
    /** Returns the flowers on the map. */
    public List<Tiles> getTiles() {
        return Arrays.stream(tiles).flatMap(Arrays::stream).toList();
    }
}

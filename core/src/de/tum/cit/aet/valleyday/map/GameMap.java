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

        HashMap<String,String> fillTiles = new HashMap<>();

        String curLine;
        String[] currTile;

        int maxX = 0;
        int maxY = 0;
   

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
            

            int row = 0;
            int col = 0;

            try {
                // try putting each tile into
                currTile = newlines[i].split("=");

                row = Integer.valueOf(currTile[0].split(",")[0].trim());
                col = Integer.valueOf(currTile[0].split(",")[1].trim());

                fillTiles.put(currTile[0], currTile[1]);

                if (row > maxX) {maxX = row;}
                if (col > maxY) {maxY = col;}
                

            } catch (Exception e) {
                throw new mapInputExcepetion("Map contained faulty input. Input must follow: int,int=int (Comments, i.e. Hashtags are allowed)");
            }


           
        }

        // Create flowers in a 7x7 grid
        // this.tiles = new Tiles[7][7];
        // for (int i = 0; i < tiles.length; i++) {
        //     for (int j = 0; j < tiles[i].length; j++) {
        //         this.tiles[i][j] = new Tiles(i, j, TileType.DIRT);
        //     }
        // }

        // Take the map as input and store the given types with a big switch statement


        int row;
        int col;

        String[] splitt;

        
        // first fill every single tile by default 
        tiles = new Tiles[maxX + 1][maxY + 1];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {               
                tiles[i][j] = new Tiles(i, j, TileType.DIRT);
            }
        }

        for (String tile : fillTiles.keySet()) {

            splitt = tile.split(",");

            row = Integer.valueOf(splitt[0].trim());
            col = Integer.valueOf(splitt[1].trim());

            System.out.println(tile);

            switch (Integer.valueOf(fillTiles.get(tile))) {
                case 1:
                    tiles[row][col] = new Tiles(row, col, TileType.SAND);
                    break;
                case 2:
                    tiles[row][col] = new Tiles(row, col, TileType.GRAS);
                default:
                    tiles[row][col] = new Tiles(row, col, TileType.DIRT);
                    break;
            }

        }
        
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

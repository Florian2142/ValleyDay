package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.tum.cit.aet.valleyday.ValleyDayGame;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.random.*;

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

    /** Difficulty of the game */
    private int difficulty;


    /***
     * Now following all the vars for the harvesting, croping and difficuly
     */

    private int currentHarvest;
    
    private final int harvesting;

    /**
     * The map is based on the layered approach. We built piece by piece.
     * We start with the base tiles and then the hiddenObjects (distributed randomly beneath the obstacles)
     * And last but not least the destructible and indestructible objects (interface functionality)
     */
    private final Tiles[][] tiles;
    private final hiddenObject[][] hiddenObjects;
    private final Obstacle[][] obstacles;
 
    private int width = 0;
    private int height = 0;

    // we have to define the starting point of the player based on the gate
    private int startX = 0;
    private int startY = 0;



    public GameMap(ValleyDayGame game, FileHandle map) throws mapInputExcepetion{
        this.game = game;
        this.world = new World(Vector2.Zero, true);
        // Create a player with initial position (1, 3)

        
        this.harvesting = 3; // UPDATE TO DIFFICULTY
        
        // Create a chest in the middle of the map
        this.chest = new Chest(world, 3, 3);

        /** Set difficulty, UPDATE LATER FOR REAL DIFFICULTY */
        this.difficulty = 0;
        /*
        * Here we follow the recommended procedure of the project description which is
        * 1.0 Store the whole file content in one very big string
        * 2.0 Split the string at each \n = newline
        * 3.0 Split each line by "="
        * 3.1 Make sure to split each empty line or comment ("#")
        * 
        * 
        * Otherwise the Whole Logic is built in an layered Approach. Hence:
        * 1: Built the basic map -> Just default flowers
        * 2: Read the File/Properties input and built the objects on top of it.
        * 
        */
        String mapString = map.readString(); // 1.0

        String[] newlines = mapString.split("\n"); // 2.0

        HashMap<String,String> fillTiles = new HashMap<>();

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
            curLine = newlines[i].trim(); // -> We always trim to avoid stupid input
            
            if (curLine.contains("#") || curLine.isEmpty()) {
                continue;
            }
            

            int row = 0;
            int col = 0;

            try {
                // try putting each tile into
                currTile = newlines[i].trim().split("=");

                row = Integer.valueOf(currTile[0].split(",")[0].trim());
                col = Integer.valueOf(currTile[0].split(",")[1].trim());

                fillTiles.put(currTile[0].trim(), currTile[1].trim());

                if (row > width) {width = row;}
                if (col > height) {height = col;}
                

            } catch (Exception e) {
                throw new mapInputExcepetion("Map contained faulty input. Input must follow: int,int=int (Comments, i.e. Hashtags are allowed)");
            }

        }

        int r;
        int c;

        List<Obstacle> debrList = new ArrayList<>(); // List for storing all the Debris
        boolean exitExists = false; // BOOLEAN FLAG (DON'T remove!)

        String[] splitt;

        /**
         * This is a layered Map building approach. 
         * 
         * We first built the default map and then on top of that the other things
         * 
         */

        
        // 1.0 Build the basic tiles which we draw over everything 
        tiles = new Tiles[width + 1][height + 1];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {               
                tiles[i][j] = new Tiles(i, j, TileType.FLOWERS);
            }
        }

        // 2.0 Build/Define the obstacles array which places the objects on the base map

        this.obstacles = new Obstacle[width + 1][height + 1]; /** Initialize the Obstacle */
        this.hiddenObjects = new hiddenObject[width + 1][height + 1];         /** Initialize the hiddenObjects */

        for (String tile : fillTiles.keySet()) {

            splitt = tile.split(",");

            r = Integer.valueOf(splitt[0].trim());
            c = Integer.valueOf(splitt[1].trim());

            switch (Integer.valueOf(fillTiles.get(tile))) {
                // Fence
                case 0:
                    obstacles[r][c] = new Fence(world, r, c);
                    break;
                case 1: // Sand -> Overwrites GROUND Layer
                    obstacles[r][c] = new Debris(world, r, c);

                    debrList.add(obstacles[r][c]); // add the Debris for later randomization

                    break;
                case 2: // Grass -> Overwrites GROUND Layer
                    this.startX = r;
                    this.startY = c;
                    System.out.println("start x: " + startX);
                    System.out.println("start y: " + startY);
                    
                    tiles[r][c] = new Tiles(r, c, TileType.GRAS);
                    break;
                case 3: // PUTS Shovel on the Map and puts DEBRIS on top
                    hiddenObjects[r][c] = new Shovel(r, c, this);
                    obstacles[r][c] = new Debris(world, r, c);
                    break;
                case 4: // EXIT
                    hiddenObjects[r][c] = new Exit(r, c, this);
                    obstacles[r][c]     = new Debris(world, r, c);// put debris again, guaranteeing its debris
                    exitExists = true;
                    break;

                default: // Dirt -> Already set by default
                    break;
            }

        }

        /** IF exit does not exists already make it */
        if (!exitExists) {
            Random rand = new Random(42); // make new random and set seed (just because its a farm Game ;)
            int random = rand.nextInt(debrList.size()); // pick random number from all DEBRIS

            Obstacle currDebris = debrList.get(random); // get the Debris where Exit must be put

            int x = (int) currDebris.getX();
            int y = (int) currDebris.getY();

            this.hiddenObjects[x][y] = new Exit(x, y, this); // put the new Exit at random Location

            System.out.println("Print X: " + x);
            System.out.println("Print Y: " + y);
        }


        /* Update the obstacles -> visualization updating */

        for (int row = 0; row < width + 1; row++) {
            for (int col = 0; col < height + 1; col++) {
                if (obstacles[row][col] instanceof Fence) {
                    ((Fence) obstacles[row][col]).updatextureRegion(this);
                }
            }
        }

        this.player = new Player(this.world, startX, startY); // Set player first after defining the starting point -> i.e. Entrance
    }
    
    /**
     * Updates the game state. This is called once per frame.
     * Every dynamic object in the game should update its state here.
     * @param frameTime the time that has passed since the last update
     */
    public void tick(float frameTime) {
        this.player.tick(frameTime, this);
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
    

    /** Destroys a tile if the object is a Destrutible */
    public void destroyObstacle(int x, int y) {
        if (x >= 0 && x <= width && y >= 0 && y <= height) {
            this.obstacles[x][y] = null;
        }
    }

    /** Removes Item when player has picked it up */
    public void removeItem(int x, int y) {
        if (x >= 0 && x <= width && y >= 0 && y <= height) {
            this.hiddenObjects[x][y] = null; // remove the item from the map
        }
    }

    /**
     * 
     * 
     * @param x x-axis point 
     * @param y y-axis point
     * @return  the ground at x,y to get the basic map tiles for drawing
     */

    public Tiles getGround(int x, int y) {
        if (x >= 0 && x <= width && y >= 0 && y <= height) {
            return tiles[x][y];
            }
        return null;
    }

    public hiddenObject gethiddenObject(int x, int y) {
            if (x >= 0 && x <= width && y >= 0 && y <= height) {
                return hiddenObjects[x][y];
                }
            return null;
        }



    /**
     * 
     * @param x x-axis point 
     * @param y y-axis point
     * @return  the ground at x,y to get the basic map tiles for drawing
     */
    public Obstacle getObstacle(int x, int y) {
        if (x >= 0 && x <= width && y >= 0 && y <= height) {
            return obstacles[x][y];
            }
        return null;
    }

    /**
     * Important for the orientation and direction of the Fence
     * 
     * Returns true if the neighbor or the tile is a fence
     * 
     * @param x
     * @param y
     * @return true if tile is instanceof Fence
     */
    public boolean isFence(int x, int y) {
        if (x < 0 || y < 0 || x > width || y > height) return false;
        return ((obstacles[x][y]) instanceof Fence);
    }

    /**
     * Important for the player when removing destructable objects
     * 
     * @param x
     * @param y
     * @return true if the object is destrutible
     */
    public boolean isDestructible(int x, int y) {
        if (x < 0 || y < 0 || x > width || y > height) return false;
        return ((obstacles[x][y]) instanceof Destructible);
    }



    /** Returns the player on the map. */
    public Player getPlayer() {
        return player;
    }
    
    /** Returns the chest on the map. */
    public Chest getChest() {
        return chest;
    }
    
    /** Returns the Tiles -> Default or ground of the map. */
    public Tiles[][] getTiles() {
        return this.tiles;
    }

    /** Return the Obstacles -> Destructible and not kaputtable */

    public Obstacle[][] getObstacles() {
        return obstacles;
    }

    public static float getTimeStep() {
        return TIME_STEP;
    }

    public static int getVelocityIterations() {
        return VELOCITY_ITERATIONS;
    }

    public static int getPositionIterations() {
        return POSITION_ITERATIONS;
    }

    public float getPhysicsTime() {
        return physicsTime;
    }

    public ValleyDayGame getGame() {
        return game;
    }

    public World getWorld() {
        return world;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}

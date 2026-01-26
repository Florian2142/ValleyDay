package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
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
    private String difficulty;


    /***
     * Now following all the vars for the harvesting, croping and difficuly
     */

    private int currentHarvest;
    
    private final int harvesting;

    private boolean soilFound = false; // make a boolean flag in case no Soil was actually found, then we MUST loop through every single tile to fit 
    private boolean noDebris = false; // the same here because the game will never start if we have no EXIT

    /**
     * The map is based on the layered approach. We built piece by piece.
     * We start with the base tiles and then the hiddenObjects (distributed randomly beneath the obstacles)
     * And last but not least the destructible and indestructible objects (interface functionality)
     */
    private final Tiles[][] tiles;                      // Basic Tiles -> The foundation
    private final hiddenObject[][] hiddenObjects;       // Hidden Objects 
    private final Obstacle[][] obstacles;               // Obstacles like Fence and Debris
    private final Crop[][] crops;                       // All the harvesting and crops needed for winning

    private final Tiles[][] decoration;                 // Nice decorations
    
    /** Soils  */
    private final Tiles[][] soils;
    /** Decoration */
    private final Obstacle[][] bigUnpassableObjects;     // For decoration 
    /** For chicken Checks */
    private final boolean[][] chickenThere;
    /** Debris list for randomizing the EXIT */
    private List<Obstacle> debrList;
    /** Even though the map is layer based we add a map containing the active Crops for better managing of growth */
    private final List<Crop> activeCrops = new ArrayList<>();

    /** We add a new Chicken Array */
    private final List<Chicken> activeChickens = new ArrayList<>();

    /** We add an explosion list for StoneDebris */
    private final List<StoneDebris> explodingDebris = new ArrayList<>();

    private final List<Wildlife> activeWildlife = new ArrayList<>();
 
    private int width = 0;
    private int height = 0;

    // we have to define the starting point of the player based on the gate
    private int startX = 1;
    private int startY = 1;

    // EXIT
    private Exit currentExit;



    /**
     * Main constructor for GameMap.
     * Reads the map file, builds all layers, spawns entities, and sets defaults.
     * This is the big function where most logic happens -> so its long on purpose.
     *
     * @param game main game instance for global access
     * @param map map properties file
     * @param difficulty difficulty string from menu
     * @throws mapInputExcepetion if the input format is invalid
     */
    public GameMap(ValleyDayGame game, FileHandle map, String difficulty) throws mapInputExcepetion{
        this.game = game;
        this.world = new World(Vector2.Zero, true);
        // Create a player with initial position (1, 3)

        this.difficulty = difficulty;

        
        this.harvesting = 3; // UPDATE TO DIFFICULTY
        
        // Create a chest in the middle of the map
        this.chest = new Chest(world, 3, 3);

        /** Set difficulty, UPDATE LATER FOR REAL DIFFICULTY */
        this.difficulty = difficulty;
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

        this.debrList = new ArrayList<>(); // List for storing all the Debris
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
                tiles[i][j] = new Tiles(i, j, TileType.GRAS);
            }
        }

        // 2.0 Build/Define tthe other layers NON-Default

        this.obstacles              = new Obstacle[width + 1][height + 1];                /** Initialize the Obstacle */
        this.hiddenObjects          = new hiddenObject[width + 1][height + 1];            /** Initialize the hiddenObjects */
        this.crops                  = new Crop[width + 1][height + 1];                    /** Intialize the Crops */
        this.soils                  = new Tiles[width + 1][height + 1];
        this.decoration             = new Tiles[width + 1][height + 1];
        this.bigUnpassableObjects   = new Obstacle[width + 1][height + 1];
        this.chickenThere           = new boolean[width + 1][height + 1];



        /**
         * Takes the string input from the map.properties key set and builds the map
         * We define the base cases hence 0 to 7 via the Artemis project description
         * Furthermore we have several more additional cases needed for more visuals
         * 
         * Each switch case is commented
         * 
         */
        for (String tile : fillTiles.keySet()) {

            splitt = tile.split(",");

            r = Integer.valueOf(splitt[0].trim());
            c = Integer.valueOf(splitt[1].trim());

            switch (Integer.valueOf(fillTiles.get(tile))) {


            case 0: // Indestructible FENCE -> Needs to be updated Later via the updateTexture() function
                obstacles[r][c] = new Fence(world, r, c);
                break;

            case 1: // Destructible DEBRIS  -> Overwrites GROUND Layer
                obstacles[r][c] = new Debris(world, r, c);
                this.debrList.add(obstacles[r][c]); // add the Debris to a list for later randomization
                break;

            case 2: // The Entrance
                // Marks the entrance of the player
                tiles[r][c] = new Tiles(r, c, TileType.START);
                this.startX = r;
                this.startY = c;
                break;

            case 3: // THE CHICKEN WILL SPAWN HERE
            double random = Math.random();
            /**
             * We make a random function between White and Brown chicken
             */
                if (random <= 0.5d) {
                    this.activeChickens.add(new WhiteChicken(world, r, c));
                } else {
                    this.activeChickens.add(new BrownChicken(world, r, c));
                }
                chickenThere[r][c] = true;
                break;
         

            case 4: // EXIT (hidden under debris)
                Exit exit =  new Exit(r, c, this);
                hiddenObjects[r][c] = exit;
                this.currentExit = exit;
                obstacles[r][c]     = new Debris(world, r, c);
                exitExists = true;
                break;

            case 5: // Fertilizer (hidden under debris)
                hiddenObjects[r][c] = new Fertilizer(r, c, this);
                obstacles[r][c] = new Debris(world, r, c);
                break;

            case 6: // Watering Can (hidden under debris)
                hiddenObjects[r][c] = new WateringCan(r, c, this);
                obstacles[r][c] = new Debris(world, r, c);
                break;

            case 7: // Shovel (VISIBLE / not under debris)
                hiddenObjects[r][c] = new Shovel(r, c, this);
                obstacles[r][c] = new Debris(world, r, c);
                break;

            case 8: // Soil (separate soil layer)
                soils[r][c] = new Tiles(r, c, TileType.SOIL);
                this.soilFound = true;
                break;
            case 9: // 
                soils[r][c] = new Tiles(r, c, TileType.LAVA);
                
                break;

            case 10:
                hiddenObjects[r][c] = new Dynamite(r, c, this);
                obstacles[r][c] = new Debris(world, r, c);
                break;

            case 12: // Force white chicken
                this.activeWildlife.add(new Spider(world, r, c, this.getPlayer()));
                chickenThere[r][c] = true;
                break;
            

            case 13: // TREE (big unpassable object)
                this.bigUnpassableObjects[r][c] = new Trees(world, r, c);
                break;

        
            case 21: // PATH (walkable ground)
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH);
                break;

            case 23: // STONES (walkable ground)
                this.hiddenObjects[r][c] = new Elixir(r, c, this);
                this.obstacles[r][c] = new StoneDebris(world, r, c);
                break;

            case 24: // FOUNTAIN (blocking structure)
                this.obstacles[r][c] = new StoneDebris(world, r, c);
                // If fountain is a multi-tile/big sprite, use bigUnpassableObjects instead
                break;

            case 25: // SMALLPLANT (walkable decoration)
                this.hiddenObjects[r][c] = new Clock(r, c, this);
                this.obstacles[r][c] = new StoneDebris(world, r, c);
                break;

            case 26: // BRIDGE_VERTICAL (walkable)
                this.bigUnpassableObjects[r][c] = new BigTree(world, r , c);
                break;

            case 27: // BRIDGE_HORIZONTAL (walkable)
                this.bigUnpassableObjects[r][c] = new House(world, r + 1 , c + 1);
                break;

            case 28: // TORCH (blocking decoration)
                this.decoration[r][c] = new Tiles(r, c, TileType.FLOWER1);
                // or obstacles[r][c] = new Torch(world, r, c); if Torch is an object
                break;
            case 29: // TORCH (blocking decoration)
                this.decoration[r][c] = new Tiles(r, c, TileType.FLOWER2);
                // or obstacles[r][c] = new Torch(world, r, c); if Torch is an object
                break;
            case 30: // PATH_FULL (Center)
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH_FULL);
                break;
            case 31: // PATH_UP
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH_UP);
                break;
            case 32: // PATH_DOWN
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH_DOWN);
                break;
            case 33: // PATH_LEFT
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH_LEFT);
                break;
            case 34: // PATH_RIGHT
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH_RIGHT);
                break;
            case 35: // CORNER TOP-LEFT
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH_CORNER_TL);
                break;
            case 36: // CORNER TOP-RIGHT
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH_CORNER_TR);
                break;
            case 37: // CORNER BOTTOM-LEFT
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH_CORNER_BL);
                break;
            case 38: // CORNER BOTTOM-RIGHT
                this.tiles[r][c] = new Tiles(r, c, TileType.PATH_CORNER_BR);
                break;
            case 39: // Tornado
                this.bigUnpassableObjects[r][c] = new Tornado(world, r, c);
                break;
            case 40: // SMALL_ROCK (Obstacle)
                this.decoration[r][c] = new Tiles(r, c, TileType.FLOWER3);
                break;
          


            default: // Dirt -> Already set by default
                break;
}

        }

        


        if (!soilFound) {
            addRandomSoil();
            
        }
        if (this.debrList.isEmpty()) {
            noDebris = true;
        }

        if (noDebris) {
            addRandomDebris();
        }

        /** IF exit does not exists already make it */
        /** Very Very cumbersome function, please just put it*/
        if (!exitExists) {
            int x = -1;
            int y = -1;

            if (!this.debrList.isEmpty()) {
                Random rand = new Random(42); // make new random and set seed (just because its a farm Game ;)
                int random = rand.nextInt(this.debrList.size()); // pick random number from all DEBRIS

                Obstacle currDebris = this.debrList.get(random); // get the Debris where Exit must be put
                x = (int) currDebris.getX();
                y = (int) currDebris.getY();
                } 
            // If the list unfortunately is empty we have to randomize it and loop through everything in order to place the actual exit
            else {
                for (int row = 0; row < width + 1; row++) {
                    for (int col = 0; col < height + 1; col++) {
                        if (isWalkable(row, col)
                            && hiddenObjects[row][col] == null
                            && decoration[row][col] == null
                            && bigUnpassableObjects[row][col] == null
                            && !chickenThere[row][col]) {
                            x = row;
                            y = col;
                            break;
                        }
                    }
                    if (x != -1) {
                        break;
                    }
                }
                if (x == -1) {
                    x = startX;
                    y = startY;
                }
            }

            Exit exit = new Exit(x, y, this);

            this.hiddenObjects[x][y] = exit; // put the new Exit at random Location
            this.currentExit = exit; // assigns the exit to the global variable
            if (this.obstacles[x][y] == null && !(x == startX && y == startY)) {
                // we also put a Debris above the EXIT as wanted
                Obstacle exitDebris = new Debris(world, x, y);
                this.obstacles[x][y] = exitDebris;
                this.debrList.add(exitDebris);
            }
        }

        /* UPDATES THE FENCE, making it with edges and everythign */

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
     * This also drives physics so it must run every render.
     *
     * @param frameTime the time that has passed since the last update
     */
    public void tick(float frameTime) {
        // for each tick in the map we update the time implying growth of the crops
        for (Crop crop: activeCrops) {
            crop.grow(frameTime);
        }
        for (Chicken chicken: activeChickens) {
            chicken.tick(frameTime, this);
        }
        // The tick is updated for the spider.
        for (Wildlife wildlife : activeWildlife) {
            if (wildlife instanceof Spider) {
                ((Spider) wildlife).tick(frameTime, this);
            }
        }
        

        for (int i = 0;  i < explodingDebris.size(); i++) {
            StoneDebris stoneDebris = explodingDebris.get(i);
            stoneDebris.tick(frameTime, this);

            if (stoneDebris.isDestructed()) {
                explodingDebris.remove(stoneDebris);
                i--;
            }
        }
        
        this.player.tick(frameTime, this);
        doPhysicsStep(frameTime);


        
    }
    
    /**
     * Performs as many physics steps as necessary to catch up to the given frame time.
     * This will update the Box2D world by the given time step.
     * We do fixed steps -> otherwise Box2D goes crazy.
     *
     * @param frameTime Time since last frame in seconds
     */
    private void doPhysicsStep(float frameTime) {
        this.physicsTime += frameTime;
        while (this.physicsTime >= TIME_STEP) {
            this.world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            this.physicsTime -= TIME_STEP;
        }
    }
    

    /**
     * Destroys a tile if the object is a Destrutible.
     * This just clears the obstacle layer, render will do the rest.
     */
    public void destroyObstacle(int x, int y) {
        if (inBound(x, y)) {
            this.obstacles[x][y] = null;
        }
    }

    /**
     * Removes Item when player has picked it up.
     * We delete the hiddenObject so it cant be picked again.
     */
    public void removeItem(int x, int y) {
        if (inBound(x, y)) {
            this.hiddenObjects[x][y] = null; // remove the item from the map
        }
    }

    /**
     * Plants a new crop if on the tile there is no obstacle, its Soil and not already a crop planted.
     * Also registers it in active list so it will grow later.
     *
     * @return true if planting worked
     */
    public boolean plantCrop(int x, int y, CropType cropType) {
        if (inBound(x, y) && (getObstacle(x, y) == null) && getCrop(x, y) == null && gethiddenObject(x, y) == null) {
            Crop newCrop = new Crop(cropType, x, y);
            newCrop.plant();
            
            crops[x][y] = newCrop; // add new crop to the map

            this.activeCrops.add(newCrop); // add crop to the active Crops for better managing of growth
            return true;
        }
        return false;
    }

    /**
     * Harvest the inquired Crop if its inbound and not null.
     * Removes from map and from active list.
     */
    public Crop harvestCrop(int x, int y) {
        if (inBound(x, y)) {
            Crop newCrop = getCrop(x, y);
            if (newCrop != null) {
                // remove the crop from the map -> Implying harvesting
                crops[x][y] = null;
                // Also remove the crop from the active Crops -> Not active anymore
                int index = activeCrops.indexOf(newCrop);
                

                return activeCrops.remove(index);
            }
        }
        return null;
    }

    /**
     * Revive the crop if a Watering Can was picked up.
     * This resets timers and can un-rot a crop.
     */
    public boolean reviveCrop() {
        if (this.activeCrops.size() == 0) {
            return false;
        }
        // else call the revive on every crop
        for (Crop crop : this.activeCrops) {
            if (crop != null) {
                crop.revive(); // revives crop
            }
        }
        return true;
    } 

    /**
     * Fertilizes the Active Crops -> Growing instantly by one Stage.
     * This is called once when fertilizer is picked up.
     */
    public boolean fertilizing() {
        if (this.activeCrops.size() == 0) {
            return false;
        }
        // else call the revive on every crop
        for (Crop crop : this.activeCrops) {
            if (crop != null) {
                crop.fertilze(); // fertilizes the crop
            }
        }
        return true;
    } 

    /**
     * Returns base ground tile for drawing.
     * This is the lowest layer and always exists -> default ground.
     *
     * @param x x-axis point
     * @param y y-axis point
     * @return the ground at x,y
     */
    public Tiles getGround(int x, int y) {
        if (inBound(x, y)) {
            return tiles[x][y];
            }
        return null;
    }

    /**
     * Alias for getGround to make render code more clear.
     */
    public Tiles getBackground(int x, int y) {
        return tiles[x][y];
    }

    /**
     * Returns soil layer, used for planting.
     */
    public Tiles getSoil(int x, int y) {
        if (inBound(x, y)) {
            return soils[x][y];
            }
        return null;
    }

    /**
     * Returns hidden object layer (items, exit, etc).
     */
    public hiddenObject gethiddenObject(int x, int y) {
            if (inBound(x, y)) {
                return hiddenObjects[x][y];
                }
            return null;
        }

    /**
     * Returns obstacle layer (debris, fence, stones).
     *
     * @param x x-axis point
     * @param y y-axis point
     * @return obstacle at tile
     */
    public Obstacle getObstacle(int x, int y) {
        if (inBound(x, y)) {
            return obstacles[x][y];
            }
        return null;
    }

    /**
     * Returns big objects layer (trees, house, big tree).
     * These block movement and are drawn above player.
     */
    public Obstacle getBigObject(int x, int y) {
        if (inBound(x, y)) {
            return bigUnpassableObjects[x][y];
            }
        return null;
    }

    /**
     * Returns crop on a tile.
     * We do not filter rotten here so caller can decide.
     *
     * @param x x-axis point
     * @param y y-axis point
     * @return crop or null
     */
    public Crop getCrop(int x, int y) {
        if (inBound(x, y)) {
            Crop crop = crops[x][y];
            if (crop != null) {
                return crop;
            }
            return null;
        }
        return null;
    }

    /**
     * Returns decoration layer (flowers etc).
     */
    public Tiles getDecoration(int x, int y) {
        if (inBound(x, y)) {
            return decoration[x][y];
        }
        return null;
    }

    

    /**
     * Lets chickens eat a crop on a tile and removes it from active list.
     * If no crop -> nothing happens.
     */
    public void eatCrop(int x, int y) {
        if (inBound(x, y)) {
            Crop newCrop = getCrop(x, y);
            if (newCrop != null) {
                // remove the crop from the map -> Implying harvesting
                crops[x][y] = null;
                // Also remove the crop from the active Crops -> Not active anymore
                int index = activeCrops.indexOf(newCrop);
                activeCrops.remove(index);
            }
        }
    }
    /**
     * Returns a random active crop for the chickens to find food.
     * This functions gets called by each Brown Chicken.
     * It is the necessity for the A*Search Algorithm.
     *
     * @return random active Crop or null if none
     */

    public Crop randomCrop() {
        if (activeCrops.size() != 0) {
            Random rand = new Random();
            int randNum = rand.nextInt(activeCrops.size());

            Crop crop = activeCrops.get(randNum);
            
            if (crop != null && !crop.isRotten()) {
                return crop;
            }
            return null;
        }
        else {
            return null;
        }
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
        if (inBound(x, y)) {
            return ((obstacles[x][y]) instanceof Fence);
        }
        return false;
    }

    /**
     * Important for the player when removing destructable objects
     * 
     * @param x
     * @param y
     * @return true if the object is destrutible
     */
    public boolean isDestructible(int x, int y) {
        if (inBound(x, y)) {
        return ((obstacles[x][y]) instanceof Destructible);
        }
        return false;
    }

    /**
     * Method to ask if the tiles are walkable 
     * 
     * IMPORTANT, DO NOT CHANGE -> Pathfinding
     * 
     * 
     * @param x the x position of the tile
     * @param y the y position of the tile
     * @return true if the tiles are walkable
     */
    public boolean isWalkable(int x, int y) {
        if (!inBound(x, y)) {
            return false;
        }
        
        if (getObstacle(x, y) != null) {
            return false;
        }

        if (getBigObject(x, y) != null) {
            return false;
        }

        // Else its supposed to be walkable
        return true;

    }


    /**
     * Make safety checks for inbound to avoid NullPointerExceptions.
     * We call this a lot so it must stay fast.
     */
    public boolean inBound(int x, int y) {
        if (x >= 0 && x <= width && y >= 0 && y <= height) {
            return true;
        }
        return false;
    }

    /**
     * Method for adding exploding debris.
     * Keeps list unique so we dont tick same debris twice.
     */
    public void addExplodingDebris(StoneDebris debris) {
        if (!explodingDebris.contains(debris)) {
            explodingDebris.add(debris);
        }
    }

    /***
     * If the map has no 8 which we defined as Soils we add random Soil.
     * The functions simply loops through free tiles and adds soils.
     */
    public void addRandomSoil() {
        int soilsNeeded = 3; // If no soil tyles have been found then
            Random rand = new Random();

            int attempts = 0;
            int maxAttempts = 100;

            /** WE MUST add a safety counter because it really no soilTile is free then well no winning is possible -> Not our problem */
            while (soilsNeeded > 0 && attempts < maxAttempts) {
                attempts++; // tries
                // Pick a random spot on the map
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);

      
                if (isWalkable(x, y) 
                    && hiddenObjects[x][y] == null 
                    && decoration[x][y] == null 
                    && bigUnpassableObjects[x][y] == null
                    && chickenThere[x][y] == false
                    && obstacles[x][y] == null) {
                    
                    // Place the soil
                    soils[x][y] = new Tiles(x, y, TileType.SOIL);
                    soilsNeeded--;
                }
            }
    }

    /***
     * If the map has no debris we add random Debris.
     * This is needed so Exit can be hidden under debris.
     */
    public void addRandomDebris() {
        int soilsNeeded = 3; // If no soil tyles have been found then
            Random rand = new Random();

            int attempts = 0;
            int maxAttempts = 100;

            /** WE MUST add a safety counter because it really no soilTile is free then well no winning is possible -> Not our problem */
            while (soilsNeeded > 0 && attempts < maxAttempts) {
                attempts++; // tries
                // Pick a random spot on the map
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);

      
                if (isWalkable(x, y) 
                    && hiddenObjects[x][y] == null 
                    && decoration[x][y] == null 
                    && bigUnpassableObjects[x][y] == null
                    && chickenThere[x][y] == false
                    && obstacles[x][y] == null) {
                    
                    // Place the soil
                    Obstacle newDebris = new Debris(world, x, y);
                    obstacles[x][y] = newDebris;
                    soilsNeeded--;

                    debrList.add(newDebris);
                }
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

    public String getDifficulty() {
        return difficulty;
    }

    public int getCurrentHarvest() {
        return currentHarvest;
    }

    public int getHarvesting() {
        return harvesting;
    }

    public hiddenObject[][] getHiddenObjects() {
        return hiddenObjects;
    }

    public Crop[][] getCrops() {
        return crops;
    }

    public List<Crop> getActiveCrops() {
        return activeCrops;
    }

    public List<Chicken> getActiveChickens() {
        return activeChickens;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public List<Wildlife> getActiveWildlife() {
        return activeWildlife;
    }

    public Exit getExit() {
        return currentExit;
    }

}


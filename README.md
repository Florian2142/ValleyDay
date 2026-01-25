# Valley Day

Valley Day is a top-down farming adventure built with LibGDX. You clear debris, plant crops, manage time and stamina, and avoid wildlife while trying to hit a harvest quota before time runs out. The game supports a campaign (multiple maps in sequence) and a free play mode with manual map selection. Note: You must start the STORY game in order to use the Advance Button.

## Project layout

- `core/` Game logic and rendering shared across platforms
  - `core/src/de/tum/cit/aet/valleyday/` main game class
  - `core/src/de/tum/cit/aet/valleyday/screen/` screens (menu, game, HUD, cutscenes)
  - `core/src/de/tum/cit/aet/valleyday/map/` map loading, entities, items, crops
  - `core/src/de/tum/cit/aet/valleyday/pathfinding/` pathfinding helpers (Implements an A*-Search AI Algorithm)
  - `core/src/de/tum/cit/aet/valleyday/audio/` music and sound effect enums
  - `core/src/de/tum/cit/aet/valleyday/texture/` texture loading, animations, drawables
- `desktop/` Desktop launcher and platform configuration
- `assets/` textures, audio, UI skin, cutscenes
- `maps/` map `.properties` files used by campaign and custom selection

## Architecture and class hierarchy

```
ValleyDayGame (extends Game)
  -> Screen
     -> MenuScreen
     -> GameScreen (creates Hud)
     -> LevelIntroScreen
     -> WinningCutsceneScreen
  -> GameMap (tile layers + entities + items)
     -> Entity (dynamic objects)
        -> Player
        -> Chicken -> BrownChicken / WhiteChicken
        -> Wildlife -> Spider
     -> hiddenObject (map items / hidden objects)
        -> Exit
        -> Item -> Shovel / Fertilizer / WateringCan / Dynamite / Elixir / Clock
     -> Obstacle
        -> Fence
        -> Debris (Destructible)
        -> StoneDebris (Destructible)
        -> Trees / House / BigTree
     -> Crop + CropType
     -> Tiles + TileType

Textures, Tiles, animations, and audio are centralized in `texture/` and `audio/` so gameplay classes only reference enums/constants.
```

## How to run

Java 17 is required.

Windows:
```
.\gradlew.bat desktop:run
```

macOS/Linux:
```
./gradlew desktop:run
```

## Controls

- Arrow keys: move -> You have only UP, DOWN, LEFT and RIGHT movements, please note its based on x- & yVelocity
- Shift: sprint (stamina based) -> You can only sprint for a few seconds before exhausted and recover
- A: plant or harvest (on soil tiles)
- D: chop destructible objects (shovel/dynamite required for some)
- R: cycle crop type 
- S: shoo/attack nearby chickens and wildlife 
- Esc: pause/resume

## Game mechanics (beyond the minimum)

- Harvest quota: each level requires harvesting a number of crops before the exit opens.
- Time limit: you lose when the timer reaches zero. 
- Health: touching chickens or being attacked by spiders reduces health; 0 health triggers game over.
- Difficulty: Depending on the difficulty you choose the Harvest quote, time and health will be set.
NOTE: There is Button "Compliance which makes the task fullfillment exactly as stated -> Health 1, Time: Enough
- Crop system:
  - Four crop types (CORN, MAIS, LEMON, SELLERIE) with different growth times and scores.
  - Crops mature, can be harvested, and can rot if ignored (After the final stage that takes 60 seconds).
  - Rotten crops can be revived when finding and picking up the watering can
- Items:
  - Shovel: increases debris damage.
  - Dynamite: allows destroying stone debris. NOTE: WITH DYNAMITE (ITEM) you can blow the stones on the map
  - Fertilizer: instantly advances crop growth by one stage.
  - Watering can: revives rotting crops.
  - Elixir: increases health. -> Only beneath StoneDebris
  - Clock: adds time. -> Only beneath StoneDebris
- AI:
  - Brown chickens use pathfinding to seek crops -> Based on a A*-search Algorithm it finds the shortest path heuristically.
  - Chickens can be scared off; spiders attack when close.
- Campaign mode (BUTTON "Start the warrior Story" in the MAIN MENU):
  - A five-map sequence with intro cutscenes and a final win cutscene -> JUST CLICK THE NICE BUTTON :D.
  - Difficulty settings adjust harvest quota, timer, and player health.(Warning, dont choose "TUM" ;)
- Random Maps: 
  - You can start any random maps you may desire, you dont have to define soil or debris 
  - If nothing is determined via java.properties debris and soil will be randomly distributed (There must be free tiles)
  - You must choose a map before starting. Then you can click the button "Start Random Map".

## Map format (custom maps)

Maps are `.properties` files with lines in the format `x,y=value`. The loader builds layered tiles and objects from these values. Key values:

- `0` fence (indestructible)
- `1` debris (destructible)
- `2` start tile (player spawn)
- `3` chicken spawn
- `4` exit (hidden under debris)
- `5` fertilizer (hidden)
- `6` watering can (hidden)
- `7` shovel (hidden)
- `8` soil (plantable)
- `9` lava
- `10` dynamite (hidden)
- `12` spider spawn
- `13` tree (blocking)
- `21` path (walkable)
- `23` elixir (hidden under stone)
- `25` clock (hidden under stone)
- `26` big tree (blocking)
- `27` house (blocking)
- `28` flower1
- `29` flower2
- `30-38` path shapes (corners/edges)
- `40` flower3

## Notes

- Campaign maps are in `maps/` and are loaded in order: `map1.properties` to `mapEG.properties`.
- Intro cutscenes and the final winning cutscene are stored under `assets/cutscenes/`.

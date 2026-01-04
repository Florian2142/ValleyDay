package de.tum.cit.aet.valleyday.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all animation constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Animations {
    
    /**
     * The animation for the character walking down.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_DOWN = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(1, 1),
            SpriteSheet.CHARACTER.at(1, 2),
            SpriteSheet.CHARACTER.at(1, 3),
            SpriteSheet.CHARACTER.at(1, 4)
    );

    public static final Animation<TextureRegion> CHARACTER_WALK_UP = new Animation<>(0.1f, 
        SpriteSheet.CHARACTER.at(3,1),
        SpriteSheet.CHARACTER.at(3,2),
        SpriteSheet.CHARACTER.at(3,3),
        SpriteSheet.CHARACTER.at(3,4)

    );

    public static final Animation<TextureRegion> CHARACTER_WALK_LEFT = new Animation<>(0.1f,
        SpriteSheet.CHARACTER.at(4,1),
        SpriteSheet.CHARACTER.at(4,2),
        SpriteSheet.CHARACTER.at(4,3),
        SpriteSheet.CHARACTER.at(4,4)
    );

    public static final Animation<TextureRegion> CHARACTER_WALK_RIGHT = new Animation<>(0.1f,
        SpriteSheet.CHARACTER.at(2,1),
        SpriteSheet.CHARACTER.at(2,2),
        SpriteSheet.CHARACTER.at(2,3),
        SpriteSheet.CHARACTER.at(2,4)
    );
    

    /**
     * The Animation if the Player is pressing now key -> i.e. Standing
     * 
     * => Will be called in the Players getApperance()
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_DOWN_IDLE = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(1, 1)   
    );
    public static final Animation<TextureRegion> CHARACTER_WALK_UP_IDLE = new Animation<>(0.1f, 
        SpriteSheet.CHARACTER.at(3,1)

    );
    public static final Animation<TextureRegion> CHARACTER_WALK_LEFT_IDLE = new Animation<>(0.1f,
        SpriteSheet.CHARACTER.at(4,1)
    );
    public static final Animation<TextureRegion> CHARACTER_WALK_RIGHT_IDLE = new Animation<>(0.1f,
        SpriteSheet.CHARACTER.at(2,1)
    );

    /** The Chicken Animation for Movement */
    public static final Animation<TextureRegion> CHICKEN_WALKING = new Animation<>(0.1f,
        SpriteSheet.FARM_THINGS.at(1,5),
        SpriteSheet.FARM_THINGS.at(1,6),
        SpriteSheet.FARM_THINGS.at(1,7),
        SpriteSheet.FARM_THINGS.at(1,8)
    );

    /** The Animation for the standing Chicken */

    public static final Animation<TextureRegion> CHICKEN_NOT_WALKING = new Animation<>(0.1f,
            SpriteSheet.FARM_THINGS.at(1,5)
    );

    /** Animation for eating Chicken */
    public static final Animation<TextureRegion> CHICKEN_EATING = new Animation<>(0.1f,
        SpriteSheet.FARM_THINGS.at(2,5),
        SpriteSheet.FARM_THINGS.at(2,6),
        SpriteSheet.FARM_THINGS.at(2,5)
        
    );



    /** Harvesting Animations */

    /** DOWN CASE */
    public static final Animation<TextureRegion> CHARACTER_HARVEST_DOWN = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(1, 6),
            SpriteSheet.CHARACTER.at(1, 7)
            //SpriteSheet.CHARACTER.at(1, 8)
            
    );
    
    /** UP CASE */
    public static final Animation<TextureRegion> CHARACTER_HARVEST_UP = new Animation<>(0.1f,
        SpriteSheet.CHARACTER.at(3,6),
        SpriteSheet.CHARACTER.at(3,7)
        //SpriteSheet.CHARACTER.at(3,8)
    );


    /** LEFT CASE */
    public static final Animation<TextureRegion> CHARACTER_HARVEST_LEFT = new Animation<>(0.1f,
        SpriteSheet.CHARACTER.at(4,6),
        SpriteSheet.CHARACTER.at(4,7)
        //SpriteSheet.CHARACTER.at(4,8)
    );

    /** RIGHT CASE*/
    public static final Animation<TextureRegion> CHARACTER_HARVEST_RIGHT = new Animation<>(0.1f,
        SpriteSheet.CHARACTER.at(2,6),
        SpriteSheet.CHARACTER.at(2,7)
        //SpriteSheet.CHARACTER.at(2,8)
    );

}



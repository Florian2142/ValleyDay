package de.tum.cit.aet.valleyday.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.aet.valleyday.texture.Animations;
import de.tum.cit.aet.valleyday.texture.Drawable;

/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Player implements Drawable {
    
    /** Total time elapsed since the game started. We use this for calculating the player movement and animating it. */
    private float elapsedTime;
    
    /** The Box2D hitbox of the player, used for position and collision detection. */
    private final Body hitbox;

    /** Create a memory for the movement of the player*/

    /*
    *   Make a private ENUM TYPE
    */
   private enum Direction{UP, DOWN, LEFT, RIGHT}


    private Direction currDirection = Direction.DOWN;

    /*Is the player standing or moving */
    private boolean moving = false;



    
    public Player(World world, float x, float y) {
        this.hitbox = createHitbox(world, x, y);
    }
    
    /**
     * Creates a Box2D body for the player.
     * This is what the physics engine uses to move the player around and detect collisions with other bodies.
     * @param world The Box2D world to add the body to.
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @return The created body.
     */
    private Body createHitbox(World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set the initial position of the body.
        bodyDef.position.set(startX, startY);
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        // Now we need to give the body a shape so the physics engine knows how to collide with it.
        // We'll use a circle shape for the player.
        CircleShape circle = new CircleShape();
        // Give the circle a radius of 0.3 tiles (the player is 0.6 tiles wide).
        circle.setRadius(0.3f);
        // Attach the shape to the body as a fixture.
        // Bodies can have multiple fixtures, but we only need one for the player.
        body.createFixture(circle, 1.0f);
        // We're done with the shape, so we should dispose of it to free up memory.
        circle.dispose();
        // Set the player as the user data of the body so we can look up the player from the body later.
        body.setUserData(this);
        return body;
    }
    
    /**
     * This function is based on the logic of keys. The user can press the keys A;W;S;D to move.
     * 
     * @param frameTime the time since the last frame.
     */
    public void tick(float frameTime) {
        this.elapsedTime += frameTime;
        // Make the player move in a circle with radius 2 tiles
        // You can change this to make the player move differently, e.g. in response to user input.
        // See Gdx.input.isKeyPressed() for keyboard input
        float yVelocity = 0;
        float xVelocity = 0;

        /**
         * we define a constant speed here
         */

        float speed = 5f;
        float MaxStamina = 100f;
        float stamina = 100f;
        float drainRate = 25f;
        float regenRate = 25f;

        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && stamina > 0) {
            speed += 5;
            this.moving = true;
            stamina -= drainRate * frameTime;
        }
        //regenerates Stamina when the Shift key is not pressed.
        else if (!Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT) && (stamina < MaxStamina)) {
            stamina += regenRate * frameTime;
        }
        //keep the value of the stamina between 0 and 100.
        stamina = MathUtils.clamp(stamina, 0, MaxStamina);

        if (Gdx.input.isKeyPressed(Keys.W)) {
            yVelocity += speed;
            this.currDirection = Direction.UP;
            this.moving = true;
        }
        else if (Gdx.input.isKeyPressed(Keys.S)) {
            yVelocity -= speed;
            this.currDirection = Direction.DOWN;
            this.moving = true;
        }
        else if (Gdx.input.isKeyPressed(Keys.D)) {
            xVelocity += speed;
            this.currDirection = Direction.RIGHT;
            this.moving = true;
        }
        else if (Gdx.input.isKeyPressed(Keys.A)) {
            xVelocity -= speed;
            this.currDirection = Direction.LEFT;
            this.moving = true;
        }
        else {
            this.moving = false;
        }

        
        
        this.hitbox.setLinearVelocity(xVelocity, yVelocity);
    }
    
    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the frame of the walk down animation that corresponds to the current time.
        if (isMoving()) {
            switch (this.currDirection) {
                    case RIGHT: return  Animations.CHARACTER_WALK_RIGHT.getKeyFrame(this.elapsedTime, true);
                    case LEFT : return  Animations.CHARACTER_WALK_LEFT.getKeyFrame(this.elapsedTime, true);
                    case UP   : return  Animations.CHARACTER_WALK_UP.getKeyFrame(this.elapsedTime, true);
                    default   : return  Animations.CHARACTER_WALK_DOWN.getKeyFrame(this.elapsedTime, true);
            }
        }
        else {
            switch (this.currDirection) {
                // These weird things are basically just when the character stands still -> Makes it natural
                    case RIGHT: return  Animations.CHARACTER_WALK_RIGHT_IDLE.getKeyFrame(this.elapsedTime, true);
                    case LEFT : return  Animations.CHARACTER_WALK_LEFT_IDLE.getKeyFrame(this.elapsedTime, true);
                    case UP   : return  Animations.CHARACTER_WALK_UP_IDLE.getKeyFrame(this.elapsedTime, true);
                    default   : return  Animations.CHARACTER_WALK_DOWN_IDLE.getKeyFrame(this.elapsedTime, true);
        }
      
    }}
    
    @Override
    public float getX() {
        // The x-coordinate of the player is the x-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().x;
    }
    
    @Override
    public float getY() {
        // The y-coordinate of the player is the y-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().y;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public Body getHitbox() {
        return hitbox;
    }

    public Direction getCurrDirection() {
        return currDirection;
    }

    public boolean isMoving() {
        return moving;
    }

    
}

package de.tum.cit.aet.valleyday.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.aet.valleyday.ValleyDayGame;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;


    private String[] difficulty = new String[]{"Easy", "Medium", "Hard", "TUM"};

    private int current = 0;

    private String currentDifficulty;




    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(ValleyDayGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage


        // Create and add a button to go to the game screen
    
        // Add a label as a title
        table.add(new Label("Welcome to TryCatchReturn35's Project", game.getSkin(), "title")).padBottom(80).row();

        // Create and add a button to go to the game screen
        TextButton goToCampagneButton   = new TextButton("Experience the marvelous warrior Story", game.getSkin());
        TextButton goToGameButton       = new TextButton("Start with random Map", game.getSkin());
        TextButton mapButtom            = new TextButton("Choose the Map.", game.getSkin());
        TextButton settingsButton       = new TextButton("Choose Difficulty.", game.getSkin());
        TextButton scaryButton          = new TextButton("Become a lost warrior!", game.getSkin());

        table.add(goToCampagneButton).width(750).row();
        table.row();
        table.add(goToGameButton).width(750).row();
        table.add(mapButtom).width(750).row();
        table.add(settingsButton).width(750).row();
        table.add(scaryButton).width(750).row();


        goToCampagneButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.startCampaign();
            }
        });


        mapButtom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectMap();

                mapButtom.setText("Map choosen! Cou can start the game.");
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                
                current++;

                if (current >= difficulty.length) {
                    current = 0;
                }

                settingsButton.setText("Difficulty: " + difficulty[current]);

                game.setDifficulty(difficulty[current]);

                
            }
        });

        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               game.startGame();            }
        });

        scaryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Leaving the game

                // We make the nice anonymous class !:)
                Dialog dialog = new Dialog("Already leaving, are you scared?", game.getSkin()) {

                @Override
                protected void result(Object answerOfUser) {
                    boolean exit = (Boolean) answerOfUser;
                    if (exit) {
                        Gdx.app.exit(); // Close the game if true
                        } 
                    else {
                        // If false, the dialog just closes automatically
                        System.out.println("The warrior stays!"); 
                        }
                    }
                };

                dialog.text("Are you sure you want to quit?");



                dialog.button("Yes", true);  // Sends 'true' to result()
                dialog.button("No", false);  // Sends 'false' to result()


                dialog.show(stage);
                
            }
        });

        
    }
    
    /**
     * The render method is called every frame to render the menu screen.
     * It clears the screen and draws the stage.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.250f); // Cap frame time to 250ms to prevent spiral of death        ScreenUtils.clear(Color.BLACK);
        ScreenUtils.clear(Color.BLACK);
        stage.act(frameTime); // Update the stage
        stage.draw(); // Draw the stage
    }
    
    /**
     * Resize the stage when the screen is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    public Stage getStage() {
        return stage;
    }

    public String[] getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String[] difficulty) {
        this.difficulty = difficulty;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }


    

    
}

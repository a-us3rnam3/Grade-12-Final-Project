package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * A screen that extends screen adapter to show a screen when you lose
 */
public class Lose extends ScreenAdapter{
    
    //Initializing variables
    MyGdxGame game;
    private Stage stage;
    private Table table;
    private Skin uiSkin;
    private TextButton backButton;
    private TextButton quitButton;
    private Label tutorialText;
    private FileHandle file;

    /**
     * Creates the lose screen using MyGdxGame as a parameter
     * @param game contains shared resources and provides function to switch screens
     */
    public Lose(MyGdxGame game) {
        this.game = game;
    }

    /**
     * called when the screen constructor is called
     * <p> same function as initialize function
     */
    @Override
    public void show() {
        stage = new Stage();
        table = new Table();
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        backButton = new TextButton("RESTART",uiSkin);
        quitButton = new TextButton("QUIT",uiSkin);
        tutorialText = new Label("YOU'VE BEEN CAUGHT BY THE TEACHER! Should've been more sneaky", uiSkin);

        Gdx.input.setInputProcessor(stage); //sets what will recieve all of the touch inputs
        stage.addActor(table);

        backButton.addListener(new ChangeListener() {//see's if the button's state changes
            @Override
            public void changed(ChangeEvent event, Actor actor) { //reset saved game data file and bring the player to the start menu
                game.resetSaved(file, game);
                game.setScreen(new Start(game));
            }
        });
        quitButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) { //quits the application
                Gdx.app.exit();
            }     
        });

        table.add(tutorialText).height(200).width(400).colspan(2); //adds tutorial text, and modifies the cell that tutorialText is within
        tutorialText.setWrap(true);
        tutorialText.setWidth(400);
        tutorialText.setAlignment(0);
        table.row();
        table.add(backButton).pad(20f);
        table.add(quitButton).pad(20f);
        table.setFillParent(true);
    }

    @Override 
    public void render(float delta) {
        ScreenUtils.clear(0.25f, .25f, 0.25f, 1); //fills in the stage with a grey colour
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();//disposes of the stage to avoid wasting resources
    }
    
}

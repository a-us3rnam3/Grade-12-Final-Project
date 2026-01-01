package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * a screen showing the options when you win
 */
public class Win extends ScreenAdapter{
    
    MyGdxGame game;
    // Test gameScreen = new Test(game);
    private Stage stage;
    private Table table;
    private Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
    private TextButton backButton = new TextButton("RESTART",uiSkin);
    private TextButton quitButton = new TextButton("QUIT",uiSkin);
    private Label tutorialText = new Label("YOU'VE WON! \n You escaped", uiSkin);
    private FileHandle file;

    /**
     * Creates the win screen using MyGdxGame as a parameter
     * @param game contains shared resources and provides function to switch screens
     */
    public Win(MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        table = new Table();
    }

    /**
     * called when the screen is ready to be seen
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); //sets what will recieve all of the touch inputs
        stage.addActor(table);

        backButton.addListener(new ChangeListener() {//see's if the button's state changes
            @Override
            public void changed(ChangeEvent event, Actor actor) { //if it did
                game.resetSaved(file, game);
                game.setScreen(new Start(game)); //go to the menu
            }
        });
        quitButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }     
        });

        table.add(tutorialText).height(200).width(400).colspan(2); //adds tutorial text, and modifies the cell that tutorialText is within
        tutorialText.setColor(Color.BLACK);
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
        ScreenUtils.clear(1f, 1f, 1f, 1); //fills in the stage with a grey colour
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();//disposes of the stage to avoid wasting resources
    }
    
}

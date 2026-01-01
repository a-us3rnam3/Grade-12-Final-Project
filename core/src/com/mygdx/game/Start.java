package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class Start extends ScreenAdapter{
    
    MyGdxGame game;
    private Stage stage;
    private Table table;
    private Skin uiSkin;
    private TextButton newGameButton;
    private TextButton saveGameButton;
    private TextButton quitButton;
    private TextButton tutorialButton;
    private Label titleText;

    /**
     * Constructor for start screen
     * <p> Initialize a new start screen that allows player to select screens they want to navigate to
     * @param game contains shared resources and provides functions to switch screens 
     */
    public Start(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        table = new Table();
        try{
            uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
            
        }catch (Exception e){
            game.setScreen(new ErrorScreen(game));
            return;
        }
        newGameButton = new TextButton("START NEW GAME",uiSkin);
        saveGameButton = new TextButton("START SAVED GAME",uiSkin);
        quitButton = new TextButton("QUIT",uiSkin);
        tutorialButton = new TextButton("TUTORIAL",uiSkin);
        titleText = new Label("ESCAPE", uiSkin);
        
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);

        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SubteamSelect(game));
            } 
        });

        saveGameButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PauseScreen(game, new Game(game, false)));
            }
            
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        tutorialButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Tutorial(game));
            }
        });

        table.add(titleText).colspan(2).height(70).width(150);
        titleText.setAlignment(0);
        titleText.setFontScale(2);
        table.row();
        table.add(newGameButton).pad(20f).height(110).width(160);
        table.add(saveGameButton).pad(20f).height(110).width(160);
        table.row();
        table.add(quitButton).pad(20f).colspan(2);
        table.row();
        table.add(tutorialButton).colspan(2);
        table.setFillParent(true);
        
    }

    @Override 
    public void render(float delta) {
        ScreenUtils.clear(0.25f, .25f, 0.25f, 1);
        stage.draw();

        // if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        //     game.setScreen(new Vent(game));
        // }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }
    
}

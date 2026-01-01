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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class SubteamSelect extends ScreenAdapter{
    
    MyGdxGame game;
    private Stage stage;
    private Table table;
    private Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
    private TextButton designButton = new TextButton("DESIGN",uiSkin);
    private TextButton manufacturingButton = new TextButton("MANUFACTURING",uiSkin);
    private TextButton programmingButton = new TextButton("PROGRAMMING",uiSkin);
    private TextButton backButton = new TextButton("BACK",uiSkin);
    private Label instructionText = new Label("PICK YOUR SUBTEAM", uiSkin);

    private FileHandle file;
    private String text;
    private String[] wordsArray;
    private Array<String> settings;

    /**Creates another screen to allow for choosing a character
     * @param game inputs the starter MyGdxGame to allow for basic functionality
     */
    public SubteamSelect(MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        table = new Table();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);

        designButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                editSubteam("DESIGN");
                game.setScreen(new Game(game, true));
            } 
        });

        manufacturingButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                editSubteam("MANUFACTURING");
                game.setScreen(new Game(game, true));
            }
            
        });

        programmingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                editSubteam("PROGRAMMING");
                game.setScreen(new Game(game, true));
            }
        });
 
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Start(game));
            }
        });

        table.add(instructionText).colspan(3).height(70).width(150);
        instructionText.setAlignment(0);
        instructionText.setFontScale(2);
        table.row();
        table.add(designButton).pad(20f).height(110).width(160);
        table.add(manufacturingButton).pad(20f).height(110).width(160);
        table.add(programmingButton).pad(20f).height(110).width(160);
        table.row();
        table.add(backButton).colspan(3);
        table.setFillParent(true);
        
        settings = new Array<String>();

        game.setupNewSettings(file, text, wordsArray, settings, game);
    }

    
    @Override 
    public void render(float delta) {
        ScreenUtils.clear(0.25f, .25f, 0.25f, 1);
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }

    private void editSubteam(String Subteam) {
        try{
            file = Gdx.files.local("NewSetting.txt");
            file.writeString(settings.get(0) + "\n" + settings.get(1) + "\n" + settings.get(2) + "\n" + settings.get(3) + "\n" + settings.get(4) + "\n" + settings.get(5) + "\n" + settings.get(6) + "\n" + settings.get(7) + "\n" + settings.get(8) + "\n" + settings.get(9) + "\n" + settings.get(10) + "\n" + Subteam, false);   
        } catch(Exception e) {
            game.setScreen(new ErrorScreen(game));
            e.printStackTrace();
        }   
    }
    
}

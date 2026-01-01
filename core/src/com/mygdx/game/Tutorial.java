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

/**
 * a screen showing the tutorial
 */
public class Tutorial extends ScreenAdapter{
    
    MyGdxGame game;
    // Test gameScreen = new Test(game);
    private Stage stage;
    private Table table;
    private Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
    private TextButton backButton = new TextButton("BACK",uiSkin);
    private Label tutorialText = new Label("To play this game, you use the W A S D keys to move up, down, left, and right respectively. You use the left mouse button to interact with objects. Your goal, as the title implies, is to escape. \n \n If you find yourself outside of your team's designated room, you will be told to get back to work, and lose the game. Have fun! \n \n Once you pass each minigame, click the object again for hints and next steps. The game will save your data when you exit by pressing ESC, then quit. There are 3 minigames, the one with wires in the background wants you to click on the rectangles on the left side with your mouse and click on the coresponding rectangle on the other side. The reaction game needs you to click when the screen changes colour. And the vent unscrewing needs you to press the screws repeatedly", uiSkin);

    public Tutorial(MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        table = new Table();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        // table.setDebug(true);
        stage.addActor(table);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Start(game));
            }
        });

        table.add(tutorialText).height(300).width(600);
        tutorialText.setWrap(true);
        tutorialText.setWidth(400);
        // tutorialText.setAlignment(0);
        // tutorialText.setFontScale(1.2f);
        table.row();
        table.add(backButton).pad(0.20f).colspan(2);
        table.setFillParent(true);
        
        
        
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
    
}

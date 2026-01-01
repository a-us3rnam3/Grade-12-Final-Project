package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * A screen that extends screen adapter to show a screen when you pause the game by pressig ESCAPE key
 */
public class PauseScreen extends ScreenAdapter{
    
    //Initializing variables
    MyGdxGame game;
    ScreenAdapter lastScreen;

    OrthographicCamera cam;

    Rectangle resumeButton;
    Rectangle menuButton;

    private Stage textStage;
    private Table resumeTable;
    private Table menuTable;
    private Skin textSkin;
    private Label resumeLabel;
    private Label menuLabel;

    private Vector2 mousePos;
    
    /**
     * Create a pause screen when it is called
     * @param game  contains shared resources and methods
     * @param lastScreen    contains the last screen the game was on
     */
    public PauseScreen (MyGdxGame game, ScreenAdapter lastScreen) {
        this.game = game;    
        this.lastScreen = lastScreen;
    }

    @Override 
    public void show() {
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 1000, 1000 * (game.getWorldHeight() / game.getWorldWidth()));
        cam.position.set(cam.viewportWidth*0.5f, cam.viewportHeight*0.5f, 0);
        cam.update();

        //Fake "buttons"
        resumeButton = new Rectangle((cam.viewportWidth*0.5f) - 100, (cam.viewportHeight*0.6f) - 75/2, 200, 75);
        menuButton = new Rectangle((cam.viewportWidth*0.5f) - 100, (cam.viewportHeight*0.4f) - 75/2, 200, 75);
        
        //Initialize all the text elements that would be displaying
        textStage = new Stage();
        resumeTable = new Table();
        menuTable = new Table();
        textSkin = new Skin(Gdx.files.internal("uiskin.json"));
        resumeLabel = new Label(null, textSkin);
        menuLabel = new Label(null, textSkin);

        // resumeTable.setDebug(true);
        // menuTable.setDebug(true);
        resumeTable.add(resumeLabel).width(65);
        menuTable.add(menuLabel).width(50);
        resumeTable.setSize(100, 50);
        menuTable.setSize(100, 50);
        resumeTable.setPosition(resumeButton.getX(), resumeButton.getY()-50);
        menuTable.setPosition(menuButton.getX(), menuButton.getY()-25);
    }

    @Override
    public void render(float delta) {
        input();
        game.batch.setProjectionMatrix(cam.combined);
        game.shapeRenderer.setProjectionMatrix(cam.combined);
        ScreenUtils.clear(255, 255, 255, 1);

        //Draws the "buttons"
        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setColor(Color.GRAY);
        game.shapeRenderer.rect(resumeButton.getX(), resumeButton.getY(), resumeButton.getWidth(), resumeButton.getHeight());
        game.shapeRenderer.rect(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight());
        game.shapeRenderer.end();

        //Add the table the stage and change the text of the label
        textStage.addActor(resumeTable);
        textStage.addActor(menuTable);
        textStage.draw();
        resumeLabel.setText("Resume");
        menuLabel.setText("Menu");

    }

    public void input() {
        //if a mouse left clicked, it checks if it is one of the buttons 
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            mousePos = game.vector3Converter(game.mousePositionInWorld(cam));
            if (resumeButton.contains(mousePos)) {
                game.setScreen(lastScreen);
            } else if (menuButton.contains(mousePos)) {
                game.setScreen(new Start(game));
            }
        }
    }
}

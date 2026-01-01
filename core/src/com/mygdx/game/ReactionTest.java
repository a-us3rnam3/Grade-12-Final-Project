package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * A screen that extends screen adapter to show a screen when you interact with Mr.C
 */
public class ReactionTest extends ScreenAdapter {
    
    //Intializing variables
    MyGdxGame game;

    private OrthographicCamera cam;

    private String state;

    private Vector3 mousePos;
    private Vector2 mousePos2d;

    private float countDown;
    private long reaction;

    private long startTime;
    private long endTime;

    private Stage textStage;
    private Table textTable;
    private Table titleTable;
    private Skin textSkin;
    private Label textLabel;
    private Label titleLabel;

    private Rectangle startRect;

    private FileHandle file;
    private String text;
    private String[] wordsArray;
    private Array<String> settings;


    public ReactionTest(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        //set up the values for the variables
        reaction = 0;
        countDown = (5 + (float)(Math.random() * ((10 - 5) + 1)));
        state = "start";

        //set up the camera
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 1000, 1000 * (game.getWorldHeight() / game.getWorldWidth()));
        cam.position.set(game.getWorldWidth()*0.5f, game.getWorldHeight()*0.5f, 0);
        cam.update();

        mousePos2d = new Vector2();

        //Initializing values for the test element in the screen
        textStage = new Stage();
        textTable = new Table();
        titleTable = new Table();
        textSkin = new Skin(Gdx.files.internal("uiskin.json"));
        textLabel = new Label(null, textSkin);
        titleLabel = new Label(null, textSkin);

        textLabel.setWrap(true);
        textStage.addActor(textTable);
        textStage.addActor(titleTable);
        textTable.add(textLabel).width(50);
        textTable.setSize(50, 25);
        textTable.setPosition(game.getWorldWidth()*0.5f - textTable.getWidth()/2, game.getWorldHeight()*0.45f - textTable.getHeight()/2);

        // titleTable.setDebug(true);
        titleTable.add(titleLabel).width(350);
        titleTable.setSize(350,100);
        titleTable.setPosition(game.getWorldWidth()*0.5f - titleTable.getWidth()/2, game.getWorldHeight()*0.60f);

        startRect = new Rectangle(game.getWorldWidth()/2 - 80, game.getWorldHeight()*0.45f - textTable.getHeight()/2 - 25, 150, 50);
    
        //setups the settings in a Array
        settings = new Array<String>();
        game.setupSettings(file, text, wordsArray, settings, game);
    }

     @Override 
    public void render(float delta) {
        game.batch.setProjectionMatrix(cam.combined);
        game.shapeRenderer.setProjectionMatrix(cam.combined);

        //Drawing seperate screen depending on the state
        if (state == "start") {
            ScreenUtils.clear(0, 0, 0, 1);
            startInputs();

            game.shapeRenderer.begin(ShapeType.Filled);
            game.shapeRenderer.setColor(Color.BLUE);
            game.shapeRenderer.rect(startRect.x, startRect.y, startRect.width, startRect.height);
            game.shapeRenderer.end();

            titleLabel.setFontScale(0.825f);
            titleLabel.setText("Reaction Test, Click anywhere when the screen turns green");
            textLabel.setText("Start");

        } else if (state == "waiting") {
            Gdx.gl.glClearColor(.3f, .002f, .002f, .01f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            waitingInputs();

            countDown-=.025f;
            if (countDown <= 0) {
                state = "Click";
                startTime = System.currentTimeMillis();
                textStage.clear();
                titleTable.clear();
                resetText(textStage, titleTable, 100, 50, titleLabel, 38);
            }

            game.shapeRenderer.begin(ShapeType.Filled);
            game.shapeRenderer.setColor(Color.WHITE);
            game.shapeRenderer.circle(game.getWorldWidth()*0.5f - 30f, game.getWorldHeight()*0.5f + 30f, 10);
            game.shapeRenderer.circle(game.getWorldWidth()*0.5f, game.getWorldHeight()*0.5f + 30f, 10);
            game.shapeRenderer.circle(game.getWorldWidth()*0.5f + 30f, game.getWorldHeight()*0.5f + 30f, 10);
            game.shapeRenderer.end();

            titleLabel.setText("Wait for Green");
            titleLabel.setFontScale(0.9f);

        } else if (state == "Click") {
            Gdx.gl.glClearColor(.02f, 3f, .5f, .1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            clickInputs();

            game.shapeRenderer.begin(ShapeType.Filled);
            game.shapeRenderer.setColor(Color.WHITE);
            game.shapeRenderer.circle(game.getWorldWidth()*0.5f, game.getWorldHeight()*0.5f + 30f, 10);
            game.shapeRenderer.end();
            
            titleLabel.setText("Click!");
            titleLabel.setFontScale(1);

        } else if (state == "result") {
            resultInput();
            Gdx.gl.glClearColor(.03f, 2f, 2.5f, .1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            titleLabel.setFontScale(1);
            titleLabel.setText("Result: " + reaction + "ms");

        } else if (state == "Early Click") {
            Gdx.gl.glClearColor(.03f, 2f, 2.25f, .1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            earlyClickedInputs();
            
            titleLabel.setFontScale(1);
            titleLabel.setText("Too soon!");
            
        }

        //pause game when ESCAPE key is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game, new Game(game, false)));
        }
        textStage.draw();

        // System.out.println(countDown);
        // System.out.println(settings.toString());
    }

    /*
     * detecting input when state is start 
     */
    private void startInputs() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            mousePos = game.mousePositionInWorld(cam);
            mousePos2d.x = mousePos.x;
            mousePos2d.y = mousePos.y;
            if (startRect.contains(mousePos2d)) {
                state = "waiting";
                textStage.clear();
                titleTable.clear();
                resetText(textStage, titleTable, 100, 50, titleLabel, 100);
            }
        }
    }

    /*
     * detecting input when state is wait 
     */
    private void waitingInputs() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            state = "Early Click";
            textStage.clear();
            titleTable.clear();
            resetText(textStage, titleTable, 75, 50, titleLabel, 75);
        }
    }

    /*
     * detecting input when state is Click 
     */
    private void clickInputs() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            endTime = System.currentTimeMillis();
            reaction = endTime - startTime;
            state = "result";
            textStage.clear();
            titleTable.clear();
            resetText(textStage, titleTable, 115, 50, titleLabel, 115);
        }
    }

    /*
     * detecting input when state is early clicked 
     */
    private void earlyClickedInputs() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            countDown = (5 + (float)(Math.random() * ((10 - 5) + 1)));
            state = "waiting";
            textStage.clear();
            titleTable.clear();
            resetText(textStage, titleTable, 100, 50, titleLabel, 100);
        }
    }

    /*
     * detecting input when result comes out 
     */
    private void resultInput() {

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (reaction >= 450) {
                editReactionPass(false);
                game.setScreen(new Game(game, false));
            } else {
                editReactionPass(true);
                game.setScreen(new Game(game, false));
            }
        }
    }

    /**
     * A method to reduce the number of lines of code takes to reset text element in a game state 
     * @param stage stage that the table is added to 
     * @param table table that the label is added to 
     * @param tableWidth    width of the table
     * @param tableHeight   height of the table
     * @param label label that displays the text 
     * @param labelWidth label width
     */
    private void resetText(Stage stage, Table table, float tableWidth, float tableHeight, Label label, float labelWidth) {
        stage.addActor(table);
        table.setSize(tableWidth, tableHeight);
        table.setPosition(game.getWorldWidth()*0.5f - titleTable.getWidth()/2, game.getWorldHeight()*0.5f - titleTable.getHeight()/2);
        table.add(label).width(labelWidth);
    }

    @Override
	public void resize(int width, int height) {
		cam.viewportWidth = 1000f;
		cam.viewportHeight = 1000f*height/width;
		cam.update();
	}

    /**
     * A method that allows you to change the reaction pass value and saved it in txt file.
     * @param reactionPass the value that you want the reaction pass to be
     */
    private void editReactionPass(Boolean reactionPass) {
        try{
            file = Gdx.files.local("SavedSetting.txt");
            file.writeString(settings.get(0) + "\n" + settings.get(1) + "\n" + settings.get(2) + "\n" + settings.get(3) + "\n" + settings.get(4) + "\n" + settings.get(5) + "\n" + String.valueOf(reactionPass) + "\n" + settings.get(7) + "\n" + true + "\n" + settings.get(9) + "\n" + settings.get(10) + "\n" + settings.get(11), false);   
        } catch(Exception e) {
            game.setScreen(new ErrorScreen(game));
            e.printStackTrace();
        }   
    }
    
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class Vent extends ScreenAdapter{
    MyGdxGame game;

    Texture backgroundTexture;
    
    private OrthographicCamera cam;

    private Array<Screws> screws;
    private Array<Rectangle> progressOutlines;
    private Array<Rectangle> progressBars;

    private Vector3 mousePos;

    private float timeUsed;
    private float timelimit;
    private int timerMin;
    private int timerSec;

	private Stage timerStage;
	private Table timerTable;
	private Skin timerSkin;
	private Label timerLabel;

    public Vent(MyGdxGame game) {
        this.game = game;
    }
    
    @Override
    public void show() {
        backgroundTexture = new Texture("Vent.png");
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 1000, 1000 * (game.getWorldHeight() / game.getWorldWidth()));
        cam.position.set(backgroundTexture.getWidth()*0.5f, backgroundTexture.getHeight()*0.5f, 0);
        cam.update();

        screws = new Array<Screws>();
        progressOutlines = new Array<Rectangle>();
        progressBars = new Array<Rectangle>();

        
        screws.add(new Screws(35,215, false, 0));
        progressOutlines.add(new Rectangle(35,215, 50, 10));
        progressBars.add(new Rectangle (35,215, 0, 8));

        screws.add(new Screws(35,790, false, 0));
        progressOutlines.add(new Rectangle(35,790, 50, 10));
        progressBars.add(new Rectangle (35,790, 0, 8));

        screws.add(new Screws(960, 790, false, 0));
        progressOutlines.add(new Rectangle(960, 790, 50, 10));
        progressBars.add(new Rectangle (960, 790, 0, 8));

        screws.add(new Screws(960, 215, false, 0));
        progressOutlines.add(new Rectangle(960, 215, 50, 10));
        progressBars.add(new Rectangle (960, 215, 0, 8));

        timeUsed = 0f;
        timelimit = 60f;

		timerStage = new Stage();
		timerTable = new Table();
		timerSkin = new Skin(Gdx.files.internal("uiskin.json"));
		timerLabel = new Label(null, timerSkin);
		
        // timerTable.setDebug(true);

        timerLabel.setPosition(0,0);

        timerTable.add(timerLabel).align(0).top().pad(10).expand();
        timerStage.addActor(timerTable);
        
        timerTable.setSize(200, 100);
		timerTable.setFillParent(true);
        timerTable.setPosition(0,0);
		timerLabel.setFontScale(2.0f);
    }

    @Override 
    public void render(float delta) {
        input();
        checkWin();
        game.batch.setProjectionMatrix(cam.combined);
        game.shapeRenderer.setProjectionMatrix(cam.combined);
        ScreenUtils.clear(255, 255, 255, 1);

        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setColor(Color.GRAY);
        game.shapeRenderer.rect(0, 0, cam.viewportWidth, cam.viewportHeight) ;
        game.shapeRenderer.end();

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);

        for (Screws screw : screws) {
            screw.draw(game.batch);
            // System.out.println(screws.indexOf(screw, true) + " : " + screw.getProgress());
            // System.out.println((screw.getY() + screw.getHeight()/2));
        }
        
        game.batch.end();

        game.shapeRenderer.begin(ShapeType.Line);
        game.shapeRenderer.setColor(Color.BLACK);

        for (Rectangle progressOutline : progressOutlines) {
            game.shapeRenderer.rect(progressOutline.x - (progressOutline.width/2), progressOutline.y - (progressOutline.height + 30), progressOutline.width, progressOutline.height);
        }
        game.shapeRenderer.end();

        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setColor(Color.GREEN);
        
        for(Rectangle progressBar : progressBars) {
            game.shapeRenderer.rect(progressBar.x - (progressOutlines.get(progressBars.indexOf(progressBar, true)).width/2), progressBar.y - (progressBar.height + 31), screws.get(progressBars.indexOf(progressBar, true)).getProgress(), progressBar.height);
        }

        game.shapeRenderer.end();

        timeUsed += Gdx.graphics.getDeltaTime();
        timerMin = (int) ((timelimit - timeUsed) / 60);
        timerSec = (int) ((timelimit - timeUsed) % 60);
		timerLabel.setText(timerMin+":"+timerSec);
		timerStage.draw();
        
        System.out.println(game.mousePositionInWorld(cam));
    }

    private void input() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            mousePos = game.mousePositionInWorld(cam);
            System.out.println(mousePos);
            for (Screws screw : screws) {
                if (mousePos.x >= (screw.getX() - screw.getWidth()/2) && mousePos.x <= (screw.getX() + screw.getWidth()/2) && mousePos.y >= (screw.getY() - screw.getHeight()/2) && mousePos.y <= (screw.getY() + screw.getHeight()/2)) {
                    // System.out.println("hello");
                    screw.setProgress(screw.getProgress() + 5);
                    screw.update(1.03f, 36);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game, new Game(game, false)));
        }
    }

    private void checkWin() {
        if (screws.get(0).getProgress() >= 49f && screws.get(1).getProgress() >= 49f && screws.get(2).getProgress() >= 49f && screws.get(3).getProgress() >= 49f) {
            game.setScreen(new Win(game));
        }
        if (timelimit - timeUsed < 0) {
            game.setScreen(new Lose(game));
        }
    }

    @Override
	public void resize(int width, int height) {
		cam.viewportWidth = 1000f;
		cam.viewportHeight = 1000f*height/width;
		cam.update();
	}


}

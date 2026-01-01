package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collections;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
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

public class WireConnection extends ScreenAdapter{
    
    MyGdxGame game;

    Texture backgroundTexture;
    
    private OrthographicCamera cam;
    private Array<Rectangle> beginWires;
	private Array<Rectangle> endWires;
	private ArrayList<Color> Colors;
	private ArrayList<Color> beginColors;
	private ArrayList<Color> endColors;

    private boolean dragging = false;
	private int wireDragged;
	private int wireConnected;
	private Vector3 mousePos;
	private Array<Wires> connectedWires = new Array<Wires>();

	private FileHandle file;
    private String text;
    private String[] wordsArray;
    private Array<String> settings;

	private float timeUsed;
    private float timelimit;
    private int timerMin;
    private int timerSec;

	private Stage timerStage;
	private Table timerTable;
	private Skin timerSkin;
	private Label timerLabel;
    
    public WireConnection(MyGdxGame game) {
        this.game = game;
    }

    @Override 
    public void show() {
        backgroundTexture = new Texture("wireBackground.png");
        beginWires = new Array<Rectangle>();
		endWires = new Array<Rectangle>();
        Colors  =new ArrayList<Color>();
        beginColors = new ArrayList<Color>();
        endColors = new ArrayList<Color>();
		
		cam = new OrthographicCamera();

        cam.setToOrtho(false, 1000, 1000 * (game.getWorldHeight() / game.getWorldWidth()));
        cam.position.set(backgroundTexture.getWidth()*0.5f, backgroundTexture.getHeight()*0.5f, 0);
        cam.update();

		beginWires.add(new Rectangle(5, 200, 60, 30));
		beginWires.add(new Rectangle(5, 405, 60, 30));
		beginWires.add(new Rectangle(5, 610, 60, 30));
		beginWires.add(new Rectangle(5, 815, 60, 30));

		endWires.add(new Rectangle(930, 200, 60, 30));
		endWires.add(new Rectangle(930, 405, 60, 30));
		endWires.add(new Rectangle(930, 610, 60, 30));
		endWires.add(new Rectangle(930, 815, 60, 30));

		Colors.add(Color.PINK);
		Colors.add(Color.BLUE);
		Colors.add(Color.YELLOW);
		Colors.add(Color.RED);

		Collections.shuffle(Colors);
		for (int i = 0; i < Colors.size(); i++) {
			beginColors.add(Colors.get(i)); 
		}
		
		Collections.shuffle(Colors);
		for (int i = 0; i < Colors.size(); i++) {
			endColors.add(Colors.get(i)); 
		}

		settings = new Array<String>();

        game.setupSettings(file, text, wordsArray, settings, game);

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
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();

		game.batch.draw(backgroundTexture, 0, 0);
		game.batch.end();

        game.shapeRenderer.begin(ShapeType.Filled);
		game.shapeRenderer.setProjectionMatrix(cam.combined);
		for (int i = 0; i < beginWires.size; i++) {
			game.shapeRenderer.setColor(beginColors.get(i));
			game.shapeRenderer.rect(beginWires.get(i).x, beginWires.get(i).y, beginWires.get(i).width, beginWires.get(i).height);
		}

		for (int i = 0; i < endWires.size; i++) {
			game.shapeRenderer.setColor(endColors.get(i));
			game.shapeRenderer.rect(endWires.get(i).x, endWires.get(i).y, endWires.get(i).width, endWires.get(i).height);
		}

		if (dragging) {
			game.shapeRenderer.setProjectionMatrix(cam.combined);
			game.shapeRenderer.setColor(beginColors.get(wireDragged));
			Vector3 mousePos = game.mousePositionInWorld(cam);
			game.shapeRenderer.rectLine(beginWires.get(wireDragged).getX() + beginWires.get(wireDragged).getWidth()-10, beginWires.get(wireDragged).getY() + (beginWires.get(wireDragged).getHeight()/2), mousePos.x, mousePos.y, 30);
		}	

		for (int i = 0; i < connectedWires.size; i++) {
			game.shapeRenderer.setProjectionMatrix(cam.combined);
			game.shapeRenderer.setColor(connectedWires.get(i).getColor());
			game.shapeRenderer.rectLine(connectedWires.get(i).getStartPositionX(), connectedWires.get(i).getStartPositionY(), connectedWires.get(i).getEndPositionX(), connectedWires.get(i).getEndPositionY(), 30);
		}
		game.shapeRenderer.end();

		timeUsed += Gdx.graphics.getDeltaTime();
        timerMin = (int) ((timelimit - timeUsed) / 60);
        timerSec = (int) ((timelimit - timeUsed) % 60);
		timerLabel.setText(timerMin+":"+timerSec);
		timerStage.draw();
    }

    private void input() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game, new Game(game, false)));
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			if (!dragging) {
				for (int i = 0; i < beginWires.size; i++) {
				mousePos = game.mousePositionInWorld(cam);
				if (mousePos.x >= beginWires.get(i).getX() && mousePos.x <= (beginWires.get(i).getX()+ beginWires.get(i).getWidth()) && mousePos.y >= beginWires.get(i).getY() && mousePos.y <= (beginWires.get(i).getY()+ beginWires.get(i).getHeight())) {
					wireDragged = i;
					dragging = true;
					System.out.println("dragging enabled");
					System.out.println(i);
				}
				}
			} else {
				for (int i = 0; i < endWires.size; i++) {
					mousePos = game.mousePositionInWorld(cam);
					if (mousePos.x >= endWires.get(i).getX() && mousePos.x <= (endWires.get(i).getX()+ endWires.get(i).getWidth()) && mousePos.y >= endWires.get(i).getY() && mousePos.y <= (endWires.get(i).getY()+ endWires.get(i).getHeight())) {
						wireConnected = i;
						if (beginColors.get(wireDragged).equals(endColors.get(wireConnected))) {
							Wires connectedWire = new Wires(beginWires.get(wireDragged).getX() + beginWires.get(wireDragged).getWidth()-10, beginWires.get(wireDragged).getY() + (beginWires.get(wireDragged).getHeight()/2), endWires.get(wireConnected).getX() + 10, endWires.get(wireConnected).getY() + (endWires.get(wireConnected).getHeight()/2), beginColors.get(wireDragged));
							connectedWires.add(connectedWire);
							System.out.println("right color");
						} else {
							System.out.println("wrong color");
							

						}
						System.out.println("clicked on the wire");
					}
				}
				System.out.println("dragging disabled");
				dragging = false;
			}
		}
    }

	private void checkWin() {
		if (connectedWires.size == 4) {
			editVentAccess(true);
			game.setScreen(new Game(game, false));
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

	private void editVentAccess(Boolean ventAccess) {
        try{
            file = Gdx.files.local("SavedSetting.txt");
            file.writeString(settings.get(0) + "\n" + settings.get(1) + "\n" + settings.get(2) + "\n" + settings.get(3) + "\n" + settings.get(4) + "\n" + settings.get(5) + "\n" + settings.get(6) + "\n" + ventAccess + "\n" + settings.get(8) + "\n" + true + "\n" + settings.get(10) + "\n" + settings.get(11), false);   
        } catch(Exception e) {
			game.setScreen(new ErrorScreen(game));
            e.printStackTrace();
        }   
    }
    
}

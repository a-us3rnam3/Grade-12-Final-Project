package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * A class that extends games, acts as an entry point of the core project
 * <p> provides useful functions such as switching between screens
 */
public class MyGdxGame extends Game{
	
	//initializing public variables
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;	

	/**
	 * called when the application/game starts 
	 */
	@Override
	public void create() { 
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();

		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setScreen(new Start(this));
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
		font.dispose();
	}

	/**Called to position the mouse within the world instead of the window screen
	 * 
	 * @param camera the current OrthographicCamera being used to view the scene
	 * @return the vector location of where the mouse button just clicked
	 */
	public Vector3 mousePositionInWorld(OrthographicCamera camera) {
		// https://jvm-gaming.org/t/libgdx-mouse-coordinates-after-resizing-screen/59933/2
		Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
		camera.unproject(v);
		return v;
	}

	/**
	 * A convertor method that help to convert 3D vector into 2D vector
	 * @param v	the 3D vector that needs to be converted into 2D vector
	 * @return the 2D vector that is converted from the 3D vector
	 */
	public Vector2 vector3Converter(Vector3 v) {
		return new Vector2(v.x, v.y);
	}

	/**
	 * Getter method to get the world width when is called
	 * @return world width
	 */
	public float getWorldWidth() {
		return Gdx.graphics.getWidth();
	}

	/**
	 * Getter method to get the world height when is called
	 * @return world height
	 */
	public float getWorldHeight() {
		return Gdx.graphics.getHeight();
	}

	/**
	 * A method used to set up an Array of string that contains the saved settings
	 * @param file	file handler that handles the file that is being loaded 
	 * @param text	A string that is used to read the entire txt file as a string
	 * @param wordsArray	A string array that stores all the settings after being splited between lines
	 * @param settings	A Array (acts as an ArrayList, from libgdx tools) that stores all the settings boolean and values
	 */
	public void setupSettings(FileHandle file, String text, String[] wordsArray, Array<String> settings, MyGdxGame game) {
		try{
            file = Gdx.files.internal("SavedSetting.txt");
            text = file.readString();
            wordsArray = text.split("\\r?\\n");
            for (String word : wordsArray) {
                settings.add(word);
            }
        } catch(Exception e) {
			game.setScreen(new ErrorScreen(game));
            e.printStackTrace();
        }   
	}

	public void setupNewSettings(FileHandle file, String text, String[] wordsArray, Array<String> settings, MyGdxGame game) {
		try{
            file = Gdx.files.internal("NewSetting.txt");
            text = file.readString();
            wordsArray = text.split("\\r?\\n");
            for (String word : wordsArray) {
                settings.add(word);
            }
        } catch(Exception e) {
			game.setScreen(new ErrorScreen(game));
            e.printStackTrace();
        }   
	}

	/**
	 * A method that is used for overwriting all the existing data for the saved settings, called when the player loses or wins
	 * <p> resets the file that saves the saved settings to default values
	 * @param file	location of the file that needs to be overwritten
	 */
	public void resetSaved(FileHandle file, MyGdxGame game) {
		try{
            file = Gdx.files.local("SavedSetting.txt");
            file.writeString(1000 + "\n" + 60 + "\n" + 1851 + "\n" + 3215 + "\n" + 0 + "\n" + 0.0 + "\n" + false + "\n" + false + "\n" + false + "\n" + false + "\n" + false + "\n" + "PROGRAMMING", false);   
        } catch(Exception e) {
			game.setScreen(new ErrorScreen(game));
            e.printStackTrace();
        }   
	}
}

	
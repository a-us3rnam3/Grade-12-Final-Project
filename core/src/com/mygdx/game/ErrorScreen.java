package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class ErrorScreen extends ScreenAdapter{
    
    MyGdxGame game;

    public ErrorScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
    }

    @Override 
    public void render(float delta) {
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.font.draw(game.batch, "It appears that some of the necessary files for this game to work are missing,\n please close this screen and redownload all game files, thanks!", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.75f);
        game.batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    
}
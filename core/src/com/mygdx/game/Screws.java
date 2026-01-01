package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Screws extends Sprite{

    private boolean Screwable;
    private float progress;

    public Screws(float x, float y, boolean Screwable, float progress) {
        super(new Sprite(new Texture("screw.png")));
        this.Screwable = Screwable;
        this.progress = progress;
        setSize(25, 25);
        setPosition(x, y);
        setOriginCenter();
    }   

    @Override 
    public void draw(Batch batch) {
        super.draw(batch);        
    }

    @Override
    public void setX(float x) {
        super.setX(x-getWidth()/2);
    }

    @Override
    public void setY(float y) {
        super.setY(y-getWidth()/2);
    }

    @Override 
    public void setPosition(float x, float y) {
        super.setPosition(x-getWidth()/2, y-getWidth()/2);
    }

    @Override
    public float getX() {
        return super.getX()+getWidth()/2;
    }

    @Override 
    public float getY() {
        return super.getY()+getHeight()/2;
    }

    public boolean getScrewable() {
        return Screwable;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (progress >= 50) {
            this.progress = 49;
        }
    }

    @Override
    public void setSize(float width, float height) {
        if (width > (25*Math.pow(1.03, 10))) {
            width = (float)(25*Math.pow(1.03, 10));
        }
        if (height > (25*Math.pow(1.03, 10))) {
            height = (float)(25*Math.pow(1.03, 10));
        }
        super.setSize(width, height);
    } 

    @Override
    public void setRotation(float degrees) {
        if (degrees > 360) {
            degrees = 360;
        }
        super.setRotation(degrees);
    }

    public void update (float scale, float rotation) {
        setSize(getWidth()*scale, getHeight()*scale);
        setRotation(getRotation() + rotation);
        setOriginCenter();
    }
}

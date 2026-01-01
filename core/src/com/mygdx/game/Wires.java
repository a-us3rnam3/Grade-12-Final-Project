package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

 /**
  * A class that holds the value of the wires
  */
public class Wires{
    
    //Initialize the variables
    private float startPositionX;
    private float startPositionY;
    private float endPositionX;
    private float endPositionY;
    private Color color;
    
    /**
     * Stores all the value of a wire
     * @param x1    x position of start position  
     * @param y1    y position of start position  
     * @param x2    x position of end position  
     * @param y2    y position of end position  
     * @param color Color of the Wire
     */
    public Wires(float x1, float y1, float x2, float y2, Color color) {
        this.startPositionX = x1;
        this.startPositionY = y1;
        this.endPositionX = x2;
        this.endPositionY = y2;
        this.color = color;
    }

    /**
     * Getter for x position of start position  
     * @return start postion x
     */
    public float getStartPositionX() {
        return startPositionX;
    }

    /**
     * Getter for y position of start position  
     * @return start postion y
     */
    public float getStartPositionY() {
        return startPositionY;
    }

    /**
     * Getter for x position of end position  
     * @return end postion x
     */
    public float getEndPositionX(){
        return endPositionX;
    }

    /**
     * Getter for y position of end position  
     * @return end postion y
     */
    public float getEndPositionY() {
        return endPositionY;
    }

    /**
     * Getter for color of wire
     * @return wire color
     */
    public Color getColor(){
        return color;
    }
    
}

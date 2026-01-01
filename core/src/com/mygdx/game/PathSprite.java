package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PathSprite extends Sprite{ //extending pathSprite from the sprite superclass
    
    private int wait = 100;
    private Vector2 velocity = new Vector2(); //setting velocity to be a 2D vector
    private float speed = 250f, tolerance = 2f; //setting speed and tolerance

    private Array<Vector2> path; //storing all the waypoints in the path array in order
    private int wayPoint; //index of the waypoint in the path array
    private boolean waiting;

    /**
     * Constructor for PathSprite, creates a sprite that follows a given path
     * @param sprite    the "template of the PathSprite"
     * @param path  the "path" of the PathSprite that is made a series of way points that it goes through
     */
    public PathSprite(Sprite sprite, Array<Vector2>path) {
        super(sprite);
        this.path = path;
    }

    //draw the sprite using sprite batch and constantly update its position using built-in time method
    @Override
    public void draw(Batch batch) {
        update (Gdx.graphics.getDeltaTime());
        super.draw(batch);        
    }

    public void update(float delta) {
        float angle = (float) Math.atan2(path.get(wayPoint).y - getY(), path.get(wayPoint).x - getX()); //get the angle from the position it is at right now to the waypoint
        velocity.set((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed); // get the velocity for x and y by cosine and sine the angle and multiply it by the speed

        setPosition(getX() + (velocity.x * delta), getY() + (velocity.y * delta)); //update the position by adding displacement (velocity * delta time) to the original position
        setRotation(angle * MathUtils.radDeg); // set rotation of the character to the approapriate rotation while moving towards the waypoint (the orientation uses degrees and the atan uses radians)
    
        //if waypoint is reached, make the increase the waypoint index to make the next waypoint the sprite's destination
        //if the next waypoint is the last waypoint in the array, make the first waypoint the next destination
        if (wayPointReached()) {
            setPosition(path.get(wayPoint).x, path.get(wayPoint).y);
            if (wayPoint + 1 == path.size) {
                if (wait >= 0) {
                    wait--;
                    waiting = true;
                } else {
                    wayPoint = 0;
                    wait = 100;
                    waiting = false;
                }
            } else {
                wayPoint++;
            }
        }
    }

    /**
     * Setter for speed variable
     * @param newSpeed  the new speed value
     */
    public void setSpeed(float newSpeed){
        speed = newSpeed;
    }

    /**
     * A getter for the PathSprite waiting status
     * @return  A boolean that represennts PathSprite waiting status
     */
    public boolean isWaiting(){
        if (waiting){
            return true;
        }
        else{
            return false;
        }
    }

    //if the sprite is within the waypoint's x and y tolerance, returns true to show that the waypoint is reached, else return false
    public boolean wayPointReached() {
        return Math.abs(path.get(wayPoint).x - getX()) <= speed/tolerance*Gdx.graphics.getDeltaTime() && Math.abs(path.get(wayPoint).y - getY()) < speed/tolerance*Gdx.graphics.getDeltaTime();
    }

    /**
     * Getter method to get the series of way points of the sprite
     * @return
     */
    public Array<Vector2> getPath() {
        return path;
    }

    /**
     * Getter method to get the index of the waypoint
     * @return  index of the waypoint in the path
     */
    public int getWaypoint() {
        return wayPoint;
    }

    /**
     * Setter method to set the index of the waypoint
     * @param waypoint  the index of the waypoint that it is set to
     */
    public void setWaypoint(int waypoint) {
        this.wayPoint = waypoint;
    }

    //https://stackoverflow.com/questions/61258928/how-do-i-attach-the-coordinates-of-a-sprite-to-its-center-libgdx
    @Override
    public void setX(float x) {
        super.setX(x-getWidth()/2);
    }

    @Override
    public void setY(float y) {
        super.setY(y-getHeight()/2);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x-getWidth()/2, y-getHeight()/2);
    }

    @Override
    public float getX() {
        return super.getX()+getWidth()/2;
    }

    @Override
    public float getY() {
        return super.getY()+getHeight()/2;
    }

    
    
}

package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Null;

/**
 * A label with the extra functionality to fill its background
 */
public class BetterLabel extends Label{
    Pixmap labelColor;
    
    
    /**
     * Creates a betterlabel with text and a skin
     * 
     * @param text the text you want it to display initially (if any)
     * @param skin the skin needed to allow for labels to create text
     */
    public BetterLabel(CharSequence text, Skin skin) {
        super(text, skin);
    }

    public void setBackground(boolean filled, Color bgColor){ //https://stackoverflow.com/questions/18166556/how-to-change-the-color-of-the-background-in-libgdx-labels

        int labelWidth = 1; //used to avoid using super when calling (issues with timer), also below
        int labelHeight = 1; //used as the preferred minimum, allows for the background fill to shrink

        labelColor = new Pixmap(labelWidth, labelHeight, Pixmap.Format.RGBA8888);
        
        
        if (!filled){
            labelColor.setColor(1,1,1,0);
            labelColor.fill();
        }
        else if (filled){
            labelColor.setColor(bgColor);
            labelColor.fill();
        }
        
        super.getStyle().background = new Image(new Texture(labelColor)).getDrawable(); //bug due to using super
        //gets the background of a label, and fills it with the pixmap of the chosen colour
    }

    /**
     * sets the text and changes the background in the process
     */
    @Override
    public void setText(@Null CharSequence newText){
        super.setText(newText); //uses label to change text
        if (super.getText().length == 0){
            setBackground(false, new Color());
            return;
        }
        setBackground(true, new Color(0.329f, 0.329f, 0.329f, 0.8f));
    }
    
}

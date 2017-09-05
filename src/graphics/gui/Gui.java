package graphics.gui;

import org.joml.Vector2f;

/**
 * Created by Bailey on 9/5/2017.
 */
public class Gui {
    private int textureID;
    private Vector2f position;
    private Vector2f scale;

    public Gui(int textureID, Vector2f position, Vector2f scale){
        this.textureID = textureID;
        this.position = position;
        this.scale = scale;
    }

    public int getTextureID() {
        return textureID;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}

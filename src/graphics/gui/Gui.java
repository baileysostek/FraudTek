package graphics.gui;

import Base.engine.Game;
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

    //in GL space -1 to 1 xy
    public void setPosition(Vector2f pos){
        this.position = pos;
    }

    //Input Vector in GL Space
    public boolean pointInside(Vector2f pt){
        Vector2f newpt = new Vector2f(pt);
        if(newpt.x() >= (position.x() - scale.x()) && (newpt.x()) <= (position.x() + scale.x())){
            if(newpt.y() <= (position.y() + scale.y()) && newpt.y() >= (position.y() - scale.y())){
                return true;
            }
        }
        return false;
    }
}

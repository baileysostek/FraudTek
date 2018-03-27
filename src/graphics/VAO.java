package graphics;

/**
 * Created by Bailey on 1/30/2018.
 */

import base.engine.Game;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;

public class VAO {
    private final int ID;
    private int size = 0;

    private HashMap<Integer, Integer> sizes = new HashMap<>();

    public VAO() {
        ID = Game.vaoManager.createVAO();
    }

    public void addVBO(int size, float[] data){
        sizes.put(this.size, data.length);
        Game.vaoManager.addVBO(this, size, data);
    }

    public int getSize(){
        return this.size;
    }

    public void setSize( int size){
        this.size = size;
    }

    public int getID(){
        return this.ID;
    }

    public int getVBOLength(int index){
        return sizes.get(index);
    }
}

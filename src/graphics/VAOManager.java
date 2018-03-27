package graphics;

import base.engine.Engine;
import base.engine.Game;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import javax.script.ScriptEngine;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Created by Bailey on 1/30/2018.
 */
public class VAOManager extends Engine{

    private ArrayList<Integer> vaos = new ArrayList<Integer>();
    private  ArrayList<Integer> vbos = new ArrayList<Integer>();

    public VAOManager() {
        super("VAOManager");
    }

    public int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    public void bindIndiciesBuffer(int[] indicies){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indicies);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public void addVBO(VAO vao, int coordinateSize, float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(vao.getSize(), coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        vao.setSize(vao.getSize()+1);
    }

    public void unbindVAO(){
        GL30.glBindVertexArray(0);
    }


    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {

    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }


    @Override
    public void onShutdown() {
        for(Integer id : vaos){
            GL30.glDeleteVertexArrays(id);
            Game.logManager.println("Deleting VAO:"+id);
        }
        for(Integer id : vbos){
            GL15.glDeleteBuffers(id);
            Game.logManager.println("Deleting VBO:"+id);
        }
    }
}
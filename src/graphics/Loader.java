/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import models.RawModel;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author Bailey
 */
public class Loader {
    
    private ArrayList<Integer> vaos = new ArrayList<Integer>();
    private ArrayList<Integer> vbos = new ArrayList<Integer>();
    private ArrayList<Integer> textures = new ArrayList<Integer>();
    
    //Vertex Array Object
    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals,  int[] indicies){
        VAO vao = new VAO();
        RawModel out = new RawModel(vao.getID(), indicies.length, positions, textureCoords, normals, indicies);
        bindIndiciesBuffer(indicies);


        vao.addVBO(3, positions);
        vao.addVBO(2, textureCoords);
        vao.addVBO(3, out.getNormals());
        vao.addVBO(3, out.getTangents());
        vao.addVBO(3, out.getBitangents());

        unbindVAO();
        return out;
    }

    //Vertex Array Object
    public RawModel loadToVAO(float[] positions, int[] indicies){
        VAO vao = new VAO();
        RawModel out = new RawModel(vao.getID(), indicies.length, positions, indicies);
        bindIndiciesBuffer(indicies);

        vao.addVBO(3, positions);
        vao.addVBO(3, positions);
        vao.addVBO(3, out.getNormals());
        vao.addVBO(3, out.getTangents());
        vao.addVBO(3, out.getBitangents());

        unbindVAO();
        return out;
    }


    //UI
    public RawModel loadToVAO(float[] positions){
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, 3, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length/3);
    }
    
    public int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }
    
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
        //put data into vbo format, then load vbo containing data into VAO at position attributeNumber
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        
    }
    
    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }
    
    private void bindIndiciesBuffer(int[] indicies){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indicies);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }
    
    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    
    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    
    public void cleanUp(){
        for(Integer id : vaos){
            GL30.glDeleteVertexArrays(id);
        }
        for(Integer id : vbos){
            GL15.glDeleteBuffers(id);
        }
    }
    
}

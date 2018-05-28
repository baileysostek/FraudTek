package models;

import base.engine.Game;
import base.util.EnumErrorLevel;
import base.util.LogManager;
import entity.Attribute;
import graphics.VAO;


/**
 * Created by Bailey on 2/17/2018.
 */
public class Model {
    private int vaoID;
    private int vertexCount;

    private int[] indicies;
    float[] verticies;
    float[] textureCoords = new float[]{};


    float[] loadedNormals = new float[]{};
    float[] loadedTangents = new float[]{};
    float[] loadedBitangents = new float[]{};


    public Model(Attribute... vars){
        VAO vao = new VAO();
        vaoID = vao.getID();
        for(int i = 0; i < vars.length; i++){
            String id = vars[i].getID();
            if(id.toLowerCase().equals("verticies")){
                verticies = (float[])vars[i].getData();
            }
            if(id.toLowerCase().equals("indicies")){
                indicies = (int[])vars[i].getData();
                vertexCount = indicies.length;
            }
            if(id.toLowerCase().equals("normals")){
                loadedNormals = (float[])vars[i].getData();
            }
            if(id.toLowerCase().equals("tangents")){
                loadedTangents = (float[])vars[i].getData();
            }
            if(id.toLowerCase().equals("bitangents")){
                loadedBitangents = (float[])vars[i].getData();
            }
            if(id.toLowerCase().equals("texturecoords")){
                textureCoords = (float[])vars[i].getData();
            }
        }

        //Calculate Normals
        newellMethod();
        Game.vaoManager.bindIndiciesBuffer(indicies);

        Game.logManager.println("verticies:"+vertexCount);
        Game.logManager.println("textures:"+textureCoords.length/2);
        Game.logManager.println("normals:"+loadedNormals.length/3);

        vao.addVBO(3, verticies);
        vao.addVBO(2, textureCoords);
        if(loadedNormals.length > 0) {
            //If normals are pre-computed
            vao.addVBO(3, loadedNormals);
        }else{
            Game.logManager.println("No Normals loaded with model.", EnumErrorLevel.SEVERE);
        }
        if(loadedTangents.length > 0) {
            //If normals are pre-computed
            vao.addVBO(3, loadedTangents);
        }else{
            Game.logManager.println("No Tangents loaded with model.", EnumErrorLevel.SEVERE);
        }
        if(loadedBitangents.length > 0) {
            //If normals are pre-computed
            vao.addVBO(3, loadedBitangents);
        }else{
            Game.logManager.println("No BiTangents loaded with model.", EnumErrorLevel.SEVERE);
        }

        Game.vaoManager.unbindVAO();
    }

    public void newellMethod(){

    }

    public float[] getFaceCenterpoint(int faceIndex){
        float[][] facePoints = new float[][]{
                new float[]{this.verticies[this.indicies[faceIndex * 3 + 0] * 3 + 0], this.verticies[this.indicies[faceIndex * 3 + 0] * 3 + 1], this.verticies[this.indicies[faceIndex * 3 + 0] * 3 + 2]},
                new float[]{this.verticies[this.indicies[faceIndex * 3 + 1] * 3 + 0], this.verticies[this.indicies[faceIndex * 3 + 1] * 3 + 1], this.verticies[this.indicies[faceIndex * 3 + 1] * 3 + 2]},
                new float[]{this.verticies[this.indicies[faceIndex * 3 + 2] * 3 + 0], this.verticies[this.indicies[faceIndex * 3 + 2] * 3 + 1], this.verticies[this.indicies[faceIndex * 3 + 2] * 3 + 2]}
        };

        float x = 0;
        float y = 0;
        float z = 0;

        for(int i = 0; i < 3; i++){
            x+=facePoints[i][0];
            y+=facePoints[i][1];
            z+=facePoints[i][2];
        }

        x/=3;
        y/=3;
        z/=3;

        float[] centerpoint = new float[]{x, y, z};

        return centerpoint;
    }

    public int getVaoID(){
        return this.vaoID;
    }

    public int getVertexCount(){
        return this.vertexCount;
    }
    public float[] getVerticies(){
        return this.verticies;
    }
    public int[] getIndicies(){
        return this.indicies;
    }
    public float[] getNormals(){
        return this.loadedNormals;
    }
    public float[] getTangents(){
        return this.loadedTangents;
    }
    public float[] getBitangents(){
        return this.loadedBitangents;
    }

    public float[] expandData(float[] in, int size, int copy){
        float[] out = new float[in.length * copy];
        for(int i = 0; i < in.length / size; i++){
            float[] data = new float[size];
            for(int j = 0; j < size; j++){
                data[j] = in[i * size + j];
            }
            for(int j = 0; j < copy; j++){
                out[(i * (size * copy)) + (j * size) + 0] = data[0];
                out[(i * (size * copy)) + (j * size) + 1] = data[1];
                out[(i * (size * copy)) + (j * size) + 2] = data[2];
            }
        }
        return out;
    }

    //TODO before bed link indicies to face

}

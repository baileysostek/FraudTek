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
    private VAO normalVAO;
    private int normalVAOID;
    private int vertexCount;

    private int[] indicies;
    float[] verticies;
    float[] textureCoords = new float[]{};
    float[] pointData;

    float[] normals;
    float[] loadedNormals;
    float[] tangents;
    float[] bitangents;


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
                normals = new float[indicies.length/2];
            }
            if(id.toLowerCase().equals("normals")){
                loadedNormals = (float[])vars[i].getData();
            }
            if(id.toLowerCase().equals("texturecoords")){
                textureCoords = (float[])vars[i].getData();
            }
        }

        //Calculate Normals
        newellMethod();
        Game.vaoManager.bindIndiciesBuffer(indicies);

        if(loadedNormals.length != normals.length) {
            Game.logManager.println("LoadedNormals = " + loadedNormals.length + " Newell Normals:" + normals.length, EnumErrorLevel.WARNING);
        }

        vao.addVBO(3, verticies);
        vao.addVBO(2, textureCoords);
        vao.addVBO(3, loadedNormals);


        Game.vaoManager.unbindVAO();

        //Normal VAO
        //Buffer only once in the future, per model
        normalVAO = new VAO();
        normalVAOID = normalVAO.getID();
        float[] normalList = new float[normals.length * 3];
        for (int j = 0; j < normals.length / 3; j++) {
            float[] faceCenter = getFaceCenterpoint(j);
            normalList[j * 6 + 0] = faceCenter[0];
            normalList[j * 6 + 1] = faceCenter[1];
            normalList[j * 6 + 2] = faceCenter[2];
            normalList[j * 6 + 3] = normals[j * 3 + 0] + faceCenter[0];
            normalList[j * 6 + 4] = normals[j * 3 + 1] + faceCenter[1];
            normalList[j * 6 + 5] = normals[j * 3 + 2] + faceCenter[2];
        }

        int[] indicieList = new int[normalList.length/3];
        for (int j = 0; j < normalList.length / 3; j++) {
            indicieList[j] = j;
        }
        Game.vaoManager.bindIndiciesBuffer(indicieList);

        normalVAO.addVBO(3, normalList);
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


    public int getNormalVAOID(){
        return this.normalVAOID;
    }

    public VAO getNormalVAO(){
        return normalVAO;
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
        return this.normals;
    }
    public float[] getTangents(){
        return this.tangents;
    }
    public float[] getBitangents(){
        return this.bitangents;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import base.engine.Game;
import base.util.EnumErrorLevel;
import base.util.StringUtils;
import entity.Attribute;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bailey
 */
public class ModelLoader {

    private static final String[] SUPPORTED_FILE_FORMATS = new String[]{".ply", ".obj"};
    
    public static String loadModel(String file){
        String id = file;
        String fileFormat = "";

        if(Game.modelManager.hasModel(id)){
            return id;
        }

        for(String extension : SUPPORTED_FILE_FORMATS){
            File test = new File(Game.Path+"/Models/" + file + extension);
            if(test.exists()){
                fileFormat = extension;
                Game.logManager.println("File Extension:"+file+fileFormat);
            }
        }

        Model model = null;
        loop:{
            switch (fileFormat) {
                case ".ply": {
                    model = loadPLYModel(file);
                    break;
                }
                case ".obj": {
                    model = loadObj(file);
                    break;
                }
            }
        }

        Game.modelManager.addModel(id, model);
        return id;
    }
    
    public static String generateQuad(float width, float height){
        Model model;
        
        String id = "quad"+width+","+height;
        
        if(Game.modelManager.hasModel(id)){
            return id;
        }

        model = new Model(new Attribute("verticies", new float[]{
                -width/2, -height/2, 0,
                width/2, -height/2, 0,
                -width/2, height/2, 0,
                width/2, height/2, 0,
                -width/2, -height/2, 0,
                width/2, -height/2, 0,
                -width/2, height/2, 0,
                width/2, height/2, 0,
        }), new Attribute("indicies", new int[]{
                2,1,0,
                3,1,2,
                4,5,6,
                5,7,6
        }), new Attribute("textureCoords", new float[]{
                0,0,
                1,0,
                0,1,
                1,1
        }), new Attribute("normals", new float[]{
                0,0,-1,
                0,0,-1,
                0,0,-1,
                0,0,-1,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,
        }));

        Game.modelManager.addModel(id, model);
        return id;
    }
    
    public static String generateCube(float width, float height, float deapth){
        RawModel model;
                
        String id = "cube"+width+","+height+","+deapth;
        
        if(Game.modelManager.hasModel(id)){
            return id;
        }
        
        model = Game.loader.loadToVAO(
        new float[]{
            -width/2, -height/2, -deapth/2,
            width/2, -height/2, -deapth/2,
            -width/2, height/2, -deapth/2,
            width/2, height/2, -deapth/2,
            -width/2, -height/2,deapth/2,
            width/2, -height/2, deapth/2,
            -width/2, height/2, deapth/2,
            width/2, height/2, deapth/2,
            -width/2, -height/2,-deapth/2,
            -width/2, height/2, -deapth/2,
            -width/2, -height/2,deapth/2,
            -width/2, height/2, deapth/2,
            width/2, -height/2,-deapth/2,
            width/2, height/2, -deapth/2,
            width/2, -height/2,deapth/2,
            width/2, height/2, deapth/2,
            -width/2, height/2, -deapth/2,
            width/2, height/2, -deapth/2,
            -width/2, height/2, deapth/2,
            width/2, height/2, deapth/2,
            -width/2, -height/2, -deapth/2,
            width/2, -height/2, -deapth/2,
            -width/2, -height/2, deapth/2,
            width/2, -height/2, deapth/2,

        }, new float[]{
            0,1,
            1,1,
            0,0,
            1,0,
            
            1,1,
            0,1,
            1,0,
            0,0,

            0,1,
            1,1,
            0,0,
            1,0,

            0,1,
            1,1,
            0,0,
            1,0,
            
            0,1,
            1,1,
            0,0,
            1,0,
            
            1,0,
            0,0,
            1,1,
            0,1, 
        },
        new float[]{
            0,0,-1,
            0,0,-1,
            0,0,-1,
            0,0,-1,
            0,0,1,
            0,0,1,
            0,0,1,
            0,0,1,
            -1,0,0,
            -1,0,0,
            -1,0,0,
            -1,0,0,
            1,0,0,
            1,0,0,
            1,0,0,
            1,0,0,
            0,1,0,
            0,1,0,
            0,1,0,
            0,1,0,
            0,-1,0,
            0,-1,0,
            0,-1,0,
            0,-1,0,
        },
        new int[]{
            2,1,0,
            3,1,2,
            4,5,6,
            6,5,7,
            8,10,9,
            11,9,10,
            8,10,9,
            11,9,10,
            14,13,15,
            12,13,14,
            16,18,17,
            19,17,18,
            21,23,20,
            22,20,23
        });

//        Game.modelManager.addModel(id, model);
        return id;
    }

    public static Model loadObj(String fileName) {
        String[] data = StringUtils.loadData(Game.Path+"/Models/" + fileName + ".obj");

        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();

        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = new float[]{};
        int[] indicesArray = null;

        for(int i = 0; i < data.length; i++){
            String line = data[i];
            if(line.startsWith("v ")){
                String[] lineData = line.split(" ");
                if(lineData.length!=4){
                    //TODO set flag to failed and return the cube model
                    Game.logManager.println("This line is wrong:"+line, EnumErrorLevel.ERROR);
                }
                Vector3f vector = new Vector3f(Float.parseFloat(lineData[1]),Float.parseFloat(lineData[2]),Float.parseFloat(lineData[3]));
                vertices.add(vector);
            }
            if(line.startsWith("vn ")){
                String[] lineData = line.split(" ");
                if(lineData.length!=4){
                    //TODO set flag to failed and return the cube model
                    Game.logManager.println("This line is wrong:"+line, EnumErrorLevel.ERROR);
                }
                Vector3f vector = new Vector3f(Float.parseFloat(lineData[1]),Float.parseFloat(lineData[2]),Float.parseFloat(lineData[3]));
                normals.add(vector);
            }
            if(line.startsWith("f ")){
                if(normalsArray == null){
                    normalsArray = new float[vertices.size() * 3];
                }
                String[] lineData = line.split(" ");
                if(lineData.length!=4){
                    //TODO set flag to failed and return the cube model
                    Game.logManager.println("This is a non-triangulatedOBJ file:"+line, EnumErrorLevel.ERROR);
                }
                String[] vertex1 = lineData[1].split("/");
                String[] vertex2 = lineData[2].split("/");
                String[] vertex3 = lineData[3].split("/");

                int vertex1Pointer = Integer.parseInt(vertex1[0])-1;
                int vertex2Pointer = Integer.parseInt(vertex2[0])-1;
                int vertex3Pointer = Integer.parseInt(vertex3[0])-1;
//                //DUMMY for now
                textures.add(new Vector2f(0, 0));

                {
                    indices.add(vertex1Pointer);
                    Vector3f currentNorm = normals.get(Integer.parseInt(vertex1[2]) - 1);
                    normalsArray[vertex1Pointer * 3 + 0] = currentNorm.x;
                    normalsArray[vertex1Pointer * 3 + 1] = currentNorm.y;
                    normalsArray[vertex1Pointer * 3 + 2] = currentNorm.z;
                }
                {
                    indices.add(vertex2Pointer);
                    Vector3f currentNorm = normals.get(Integer.parseInt(vertex2[2]) - 1);
                    normalsArray[vertex2Pointer * 3 + 0] = currentNorm.x;
                    normalsArray[vertex2Pointer * 3 + 1] = currentNorm.y;
                    normalsArray[vertex2Pointer * 3 + 2] = currentNorm.z;
                }
                {
                    indices.add(vertex3Pointer);
                    Vector3f currentNorm = normals.get(Integer.parseInt(vertex3[2]) - 1);
                    normalsArray[vertex3Pointer * 3 + 0] = currentNorm.x;
                    normalsArray[vertex3Pointer * 3 + 1] = currentNorm.y;
                    normalsArray[vertex3Pointer * 3 + 2] = currentNorm.z;
                }
            }
        }

        //Flush indices to array.
        indicesArray = new int[indices.size()];
        for(int i = 0; i < indices.size(); i++){
            indicesArray[i] = indices.get(i);
        }

        //Flush vertices to array.
        verticesArray = new float[vertices.size() * 3];
        for(int i = 0; i < vertices.size(); i++){
            verticesArray[i * 3 + 0] = vertices.get(i).x();
            verticesArray[i * 3 + 1] = vertices.get(i).y();
            verticesArray[i * 3 + 2] = vertices.get(i).z();
        }

        //Flush textureCoords to array.
        textureArray = new float[textures.size() * 2];
        for(int i = 0; i < textures.size(); i++){
            textureArray[i * 2 + 0] = textures.get(i).x();
            textureArray[i * 2 + 1] = textures.get(i).y();
        }

        //Throw an error and return the cube model
        if(verticesArray == null){
            Game.logManager.println("Error loading model file:"+Game.Path+"/Models/" + fileName + ".obj", EnumErrorLevel.ERROR);
            return Game.modelManager.getModel(ModelLoader.loadModel("cube"));
        }

        return new Model(new Attribute<float[]>("verticies", verticesArray), new Attribute<float[]>("normals", normalsArray), new Attribute<int[]>("indicies", indicesArray), new Attribute<float[]>("textureCoords", textureArray));
    }

    public static Model loadPLYModel(String fileName){
        String path = Game.Path+"/Models/" + fileName + ".ply";
        String[] data = StringUtils.loadData(path);

        float[] verticies = new float[]{};
        int[] indicies = new int[]{};
        float[] textureCoords = new float[]{};

        int vLength = 0;
        int fLength = 0;
        int startIndex = 0;

        for(int i = 0; i < data.length; i++){
            String line = data[i];
            if(line.startsWith("element vertex")){
                vLength = Integer.parseInt(line.replace("element vertex ", ""));
                verticies = new float[vLength * 3];
            }
            if(line.startsWith("element face")){
                fLength = Integer.parseInt(line.replace("element face ", ""));
                indicies = new int[fLength * 3];
            }
            if(line.startsWith("end_header")){
                startIndex = i+1;
                break;
            }
        }

        int index = startIndex;
        for(int i = 0; i < vLength; i++){
            String[] splitLine = data[index].split(" ");
            verticies[i * 3 + 0] = Float.parseFloat(splitLine[0]);
            verticies[i * 3 + 1] = Float.parseFloat(splitLine[1]);
            verticies[i * 3 + 2] = Float.parseFloat(splitLine[2]);
            index++;
        }

        index = startIndex + vLength;
        for(int i = 0; i < fLength; i++){
            String[] splitLine = data[index].split(" ");
            indicies[i * 3 + 0] = Integer.parseInt(splitLine[1]);
            indicies[i * 3 + 1] = Integer.parseInt(splitLine[2]);
            indicies[i * 3 + 2] = Integer.parseInt(splitLine[3]);
            index++;
        }

        textureCoords = new float[(verticies.length/3) * 2];
        for(int i = 0; i < textureCoords.length/2; i++){
            float distance = (float)((float)i/(float)textureCoords.length/2.0);
            textureCoords[i+0] = distance;
            textureCoords[i+1] = distance;
        }

        return new Model(new Attribute<float[]>("verticies", verticies), new Attribute<int[]>("indicies", indicies), new Attribute<float[]>("textureCoords", textureCoords));
    }
}

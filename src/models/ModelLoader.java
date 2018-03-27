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
        float[] textureArray = null;
        int[] indicesArray = null;
        try {
            int faceDataIndex = 0;
            for(int i = 0; i < data.length; i++) {
                String line = data[i];
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    faceDataIndex = i;
                    Game.logManager.println(faceDataIndex+"");
                    textureArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }
            for(int i = faceDataIndex; i < data.length; i++) {
                String line = data[i];
                if (!line.startsWith("f ")) {
                    continue;
                }
                String[] currentLine = line.split(" ");
                for(int j = 0; j < (currentLine.length-3); j++) {
                    String[] vertex1 = currentLine[j+1].split("/");
                    String[] vertex2 = currentLine[j+2].split("/");
                    String[] vertex3 = currentLine[j+3].split("/");

                    processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                    processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                    processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
                }
            }
        } catch (Exception e) {
            Game.logManager.println("Error loading model:"+fileName+" at path:"+Game.Path+"/Models/" + fileName + ".obj", EnumErrorLevel.ERROR);
            Game.logManager.printStackTrace(e);
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];
        textureArray = new float[textures.size() * 2];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        vertexPointer = 0;
        for (Vector2f texture : textures) {
            textureArray[vertexPointer++] = texture.x;
            textureArray[vertexPointer++] = texture.y;
        }

        return new Model(new Attribute<float[]>("verticies", verticesArray), new Attribute<float[]>("normals", normalsArray), new Attribute<int[]>("indicies", indicesArray), new Attribute<float[]>("textureCoords", textureArray));
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        //check for texture
        if(!vertexData[1].isEmpty()){
            Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
            textureArray[currentVertexPointer * 2] = currentTex.x;
            textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
        }
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3 + 0] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
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

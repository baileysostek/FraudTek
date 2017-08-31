/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import Base.engine.Game;
import Base.util.OBJLoader;

/**
 *
 * @author Bailey
 */
public class ModelLoader {
    
    public static String loadModel(String file){
        String id = file;
        if(Game.modelManager.hasModel(id)){
            return id;
        }
        RawModel model = OBJLoader.loadObjModel(file, Game.loader);
        Game.modelManager.addModel(id, model);        
        return id;
    }
    
    public static String generateQuad(float width, float height){
        RawModel model;
        
        String id = "quad"+width+","+height;
        
        if(Game.modelManager.hasModel(id)){
            return id;
        }
        
        model = Game.loader.loadToVAO(
        new float[]{
            -width/2, -height/2, 0,
            width/2, -height/2, 0,
            -width/2, height/2, 0,
            width/2, height/2, 0,
            -width/2, -height/2,0,
            width/2, -height/2, 0,
            -width/2, height/2, 0,
            width/2, height/2, 0,
        }, new float[]{
            0,1,
            1,1,
            0,0,
            1,0,
            1,1,
            0,1,
            1,0,
            0,0, 
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
        },
        new int[]{
            2,1,0,
            3,1,2,
            4,5,6,
            6,5,7,
        });

        Game.modelManager.addModel(id, model);        
        return id;
    }
    
      public static String generateTerrain(float width, float height, int subDivisions){
        RawModel model;
        
        String id = "terrain"+width+","+height;
        
        if(Game.modelManager.hasModel(id)){
            return id;
        }
        
        float[] verticies = new float[((subDivisions + 1) * (subDivisions + 1)) * 3];
        for(int j = 0; j < (subDivisions+1); j++){
            for(int i = 0; i < (subDivisions+1); i++){
                verticies[((i+(j * (subDivisions + 1))) * 3) + 0] = (-width/2) + (i * (width/(float)subDivisions)); 
                verticies[((i+(j * (subDivisions + 1))) * 3) + 1] =  -1 * ((float) Math.pow((((float)i/(float)(subDivisions+1))-0.5f) * 6, 2) + (float) Math.pow((((float)j/(float)(subDivisions+1))-0.5f) * 6, 2));
//                verticies[((i+(j * (subDivisions + 1))) * 3) + 1] = 0;
                verticies[((i+(j * (subDivisions + 1))) * 3) + 2] = (-height/2) + (j * (height/(float)subDivisions));
            }  
        }
        
        int[] indicies = new int[((subDivisions + 1) * (subDivisions + 1)) * 6];
        int index = 0;
        for(int j = 0; j < (subDivisions); j++){
            for(int i = 0; i < (subDivisions); i++){
                indicies[index + 0] = (i+((j + 1) * (subDivisions+1)));
                indicies[index + 1] = ((i + 1)+(j * (subDivisions+1)));
                indicies[index + 2] = (i+(j * (subDivisions+1)));
                indicies[index + 3] = ((i + 1)+(j * (subDivisions+1)));
                indicies[index + 4] = (i+((j + 1) * (subDivisions+1)));
                indicies[index + 5] = ((i + 1)+((j + 1) * (subDivisions+1)));
                index+=6;
            }  
        }
        
        float[] textures = new float[(((subDivisions + 1) * (subDivisions + 1)) * 8)];
        for(int j = 0; j < (subDivisions); j++){
            for(int i = 0; i < (subDivisions); i++){
                textures[((i+(j * (subDivisions+1))) * 8)+ 0] = ((float)(i + 0))/((float)(subDivisions+1));
                textures[((i+(j * (subDivisions+1))) * 8)+ 1] = ((float)(j + 1))/((float)(subDivisions+1));
                textures[((i+(j * (subDivisions+1))) * 8)+ 2] = ((float)(i + 1))/((float)(subDivisions+1));
                textures[((i+(j * (subDivisions+1))) * 8)+ 3] = ((float)(j + 1))/((float)(subDivisions+1));
                textures[((i+(j * (subDivisions+1))) * 8)+ 4] = ((float)(i + 0))/((float)(subDivisions+1));
                textures[((i+(j * (subDivisions+1))) * 8)+ 5] = ((float)(j + 0))/((float)(subDivisions+1));
                textures[((i+(j * (subDivisions+1))) * 8)+ 6] = ((float)(i + 1))/((float)(subDivisions+1));
                textures[((i+(j * (subDivisions+1))) * 8)+ 7] = ((float)(j + 0))/((float)(subDivisions+1));
            }  
        }

        
        model = Game.loader.loadToVAO(
        verticies
        ,textures,
        new float[]{
            0,0,-1,
            0,0,-1,
            0,0,-1,
            0,0,-1,
        },
        indicies);

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
            0,0,
            1,1,
            0,1,
            1,0,
            
            1,1,
            0,1,
            1,0,
            0,0,
            
            0,1,
            0,0,
            1,1,
            1,0,
            
            0,0,
            0,1,
            1,0,
            1,1,
            
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

        Game.modelManager.addModel(id, model);
        return id;
    }
}

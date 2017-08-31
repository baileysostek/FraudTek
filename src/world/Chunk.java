/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import Base.engine.Game;
import graphics.PixelUtils;
import graphics.Renderer;
import graphics.Sprite;
import models.ModelLoader;
import models.RawModel;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class Chunk {
    private int x;
    private int y;
    
    float width = 16;
    float height = 16;
    
    private RawModel model;
    
    public Chunk(int x, int y, Sprite s){
        this.x = x;
        this.y = y;
        generate(s);
    }
    
    public void generate(Sprite s){
        int subDivisions = 64;

        float[] verticies = new float[((subDivisions + 1) * (subDivisions + 1)) * 3];
        for(int j = 0; j < (subDivisions+1); j++){
            for(int i = 0; i < (subDivisions+1); i++){
                verticies[((i+(j * (subDivisions + 1))) * 3) + 0] = (-width/2) + (i * (width/(float)(subDivisions))); 
//                verticies[((i+(j * (subDivisions + 1))) * 3) + 1] =  -1 * ((float) Math.pow((((float)i/(float)(subDivisions+1))-0.5f) * 6, 2) + (float) Math.pow((((float)j/(float)(subDivisions+1))-0.5f) * 6, 2));
                verticies[((i+(j * (subDivisions + 1))) * 3) + 1] = (PixelUtils.grayScaleColor(s.getPixelColor(Math.max((int)(s.width * ((float)i/(float)(subDivisions+1)))-1, 0), Math.max((int)(s.height * ((float)j/(float)(subDivisions+1)))-1, 0))) -0.5f) * 128;
                verticies[((i+(j * (subDivisions + 1))) * 3) + 2] = (-height/2) + (j * (height/(float)(subDivisions)));
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
    }
    
    
    public void render(Renderer r, StaticShader s){
        r.render(model, Game.materialManager.getMaterial("white"), new Vector3f(x * width, 0, y * height), new Vector3f(0, 0, 0), 1, s);
    }
    
}

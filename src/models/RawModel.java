/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import base.engine.Game;
import org.joml.GeometryUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author Bailey
 */
public class RawModel {
    
    private int vaoID;
    private int vertexCount;
    
    float[] positions;
    float[] textureCoords;
    
    float[] normals;
    float[] tangents;
    float[] bitangents;
    
    private int[] indicies;
            
    public RawModel(int vaoID, int vertexCount, float[] positions, float[] textureCoords, float[] normals, int[] indicies){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
        this.positions = positions;
        this.textureCoords = textureCoords;
        this.indicies = indicies;

        this.normals = new float[indicies.length];
        this.tangents = new float[normals.length];
        this.bitangents = new float[normals.length];

        //Newell Method for calculating normals
        for(int i = 0; i < indicies.length/3; i++){
            float mx = 0;
            float my = 0;
            float mz = 0;
            float[][] normalPoints = new float[][]{
                    new float[]{positions[indicies[(i*3)+0]+0], positions[indicies[(i*3)+0]+1], positions[indicies[(i*3)+0]+2]},
                    new float[]{positions[indicies[(i*3)+1]+0], positions[indicies[(i*3)+1]+1], positions[indicies[(i*3)+1]+2]},
                    new float[]{positions[indicies[(i*3)+2]+0], positions[indicies[(i*3)+2]+1], positions[indicies[(i*3)+2]+2]},
                    new float[]{positions[indicies[(i*3)+0]+0], positions[indicies[(i*3)+0]+1], positions[indicies[(i*3)+0]+2]}
            };
            for(int j = 0; j < normalPoints.length-1; j++){
                mx += (normalPoints[j][1] - normalPoints[j+1][1]) * (normalPoints[j][2]+normalPoints[j+1][2]);
                my += (normalPoints[j][2] - normalPoints[j+1][2]) * (normalPoints[j][0]+normalPoints[j+1][0]);
                mz += (normalPoints[j][0] - normalPoints[j+1][0]) * (normalPoints[j][1]+normalPoints[j+1][1]);
            }
            float[] normal = new float[]{mx, my, mz};
            float length = (float) Math.sqrt((normal[0] * normal[0])+(normal[1] * normal[1])+(normal[2] * normal[2]));
            normal[0]/=length;
            normal[1]/=length;
            normal[2]/=length;
            this.normals[(i*3)+0] = normal[0];
            this.normals[(i*3)+1] = normal[1];
            this.normals[(i*3)+2] = normal[2];
        }
    }

    public RawModel(int vaoID, int vertexCount, float[] positions, int[] indicies){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
        this.positions = positions;
        this.indicies = indicies;

        this.normals = new float[indicies.length];
        this.tangents = new float[normals.length];
        this.bitangents = new float[normals.length];

        //Newell Method for calculating normals
        for(int i = 0; i < indicies.length/3; i++){
            float mx = 0;
            float my = 0;
            float mz = 0;
            float[][] normalPoints = new float[][]{
                    new float[]{positions[indicies[(i*3)+0] * 3 +0], positions[indicies[(i*3)+0] * 3 +1], positions[indicies[(i*3)+0] * 3 +2]},
                    new float[]{positions[indicies[(i*3)+1] * 3 +0], positions[indicies[(i*3)+1] * 3 +1], positions[indicies[(i*3)+1] * 3 +2]},
                    new float[]{positions[indicies[(i*3)+2] * 3 +0], positions[indicies[(i*3)+2] * 3 +1], positions[indicies[(i*3)+2] * 3 +2]},
                    new float[]{positions[indicies[(i*3)+0] * 3 +0], positions[indicies[(i*3)+0] * 3 +1], positions[indicies[(i*3)+0] * 3 +2]}
            };
            for(int j = 0; j < normalPoints.length-1; j++){
                mx += (normalPoints[j][1] - normalPoints[j+1][1]) * (normalPoints[j][2]+normalPoints[j+1][2]);
                my += (normalPoints[j][2] - normalPoints[j+1][2]) * (normalPoints[j][0]+normalPoints[j+1][0]);
                mz += (normalPoints[j][0] - normalPoints[j+1][0]) * (normalPoints[j][1]+normalPoints[j+1][1]);
            }
            float[] normal = new float[]{mx, my, mz};
            float length = (float) Math.sqrt((normal[0] * normal[0])+(normal[1] * normal[1])+(normal[2] * normal[2]));
            normal[0]/=length;
            normal[1]/=length;
            normal[2]/=length;
            this.normals[(i*3)+0] = normal[0];
            this.normals[(i*3)+1] = normal[1];
            this.normals[(i*3)+2] = normal[2];
        }
    }

    public RawModel(int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }
    
    public int getVaoID(){
        return this.vaoID;
    }
    
    public int getVertexCount(){
        return this.vertexCount;
    }
    
    public float[] getPositions(){
        return this.positions;
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

    public float[] getFaceCenterpoint(int faceIndex){
        float[][] facePoints = new float[][]{
            new float[]{this.positions[this.indicies[faceIndex * 3 + 0] * 3 + 0], this.positions[this.indicies[faceIndex * 3 + 0] * 3 + 1], this.positions[this.indicies[faceIndex * 3 + 0] * 3 + 2]},
            new float[]{this.positions[this.indicies[faceIndex * 3 + 1] * 3 + 0], this.positions[this.indicies[faceIndex * 3 + 1] * 3 + 1], this.positions[this.indicies[faceIndex * 3 + 1] * 3 + 2]},
            new float[]{this.positions[this.indicies[faceIndex * 3 + 2] * 3 + 0], this.positions[this.indicies[faceIndex * 3 + 2] * 3 + 1], this.positions[this.indicies[faceIndex * 3 + 2] * 3 + 2]}
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
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

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
        
        this.normals = normals;
        this.tangents = new float[normals.length];
        this.bitangents = new float[normals.length];
        
        for(int i = 0; i < normals.length/3; i++){
            Vector3f normal = new Vector3f(this.normals[i * 3 + 0], this.normals[i * 3 + 1] , this.normals[i * 3 + 2]);

            Vector3f tangent = new Vector3f();
            Vector3f c1 = new Vector3f();
            Vector3f c2 = new Vector3f();
            
            normal.cross(new Vector3f(0.0f, 0.0f, 1.0f), c1); 
            normal.cross(new Vector3f(0.0f, 1.0f, 0.0f), c2); 

            if(c1.length() > c2.length()){
                tangent = c1;	
            }else{
                tangent = c2;	
            }

//            tangent.normalize(tangent);
            
            this.tangents[i * 3 + 0] = tangent.x; 
            this.tangents[i * 3 + 1] = tangent.y;
            this.tangents[i * 3 + 2] = tangent.z;
            
            Vector3f bitangent = new Vector3f();
            normal.cross(tangent, bitangent);
            
//            bitangent.normalize(bitangent);
            this.bitangents[i * 3 + 0] = bitangent.x; 
            this.bitangents[i * 3 + 1] = bitangent.y;
            this.bitangents[i * 3 + 2] = bitangent.z;
        }
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
    
}

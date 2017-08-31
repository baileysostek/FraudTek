/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shaders;

import camera.Camera;
import lighting.Light;
import math.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import Base.engine.Game;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL33;

/**
 *
 * @author Bailey
 */
public class StaticShader extends ShaderProgram{
    
    private static final int MAX_LIGHTS = 4;
    
    private static final String VERTEX_FILE = "/Shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "/Shaders/fragmentShader.txt";
    
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_light_position[];
    private int location_light_color[];
    private int location_light_attenuation[];

    
    //texture sampler stuff
    //These are the pointers to samplers which take an interger representing the GL textures that it should refrence// EX. GL13.GL_TEXTURE0, GL13.GL_TEXTURE1.. ect 
        //the texture of the material
        private int location_texture;
        //the normal of the face
        private int location_normal;
        //texture to reflect
        private int location_specular;
        //how reflective the material is
        private int location_roughness;
    
    public StaticShader(String basePath){
        super(basePath+VERTEX_FILE, basePath+FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
        super.bindAttribute(3, "tangent");
        super.bindAttribute(4, "bitangent");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        
        //texture material stuff
        location_texture = super.getUniformLocation("textureSampler");
        location_normal = super.getUniformLocation("normalSampler");
        location_specular = super.getUniformLocation("specularSampler");
        location_roughness = super.getUniformLocation("roughnessSampler");
        
        location_light_position = new int[MAX_LIGHTS];
        location_light_color = new int[MAX_LIGHTS];
        location_light_attenuation = new int[MAX_LIGHTS];
        for(int i = 0; i < MAX_LIGHTS; i++){
            location_light_position[i] = super.getUniformLocation("lightPosition["+i+"]");
            location_light_color[i] = super.getUniformLocation("lightColor["+i+"]");
            location_light_attenuation[i] = super.getUniformLocation("attenuation["+i+"]");
        }
    }
    
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
    
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
    
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
    
    public void loadLights(Light[] lights){
        for(int i = 0; i < MAX_LIGHTS; i++){
            if(i < lights.length){
                super.loadVector(location_light_position[i], lights[i].getPosition());
                super.loadVector(location_light_color[i], lights[i].getColor());
                super.loadVector(location_light_attenuation[i], lights[i].getAttenuation());
            }else{
                super.loadVector(location_light_position[i], new Vector3f(0,0,0));
                super.loadVector(location_light_color[i], new Vector3f(0,0,0));
                super.loadVector(location_light_attenuation[i], new Vector3f(1,0,0));
            }
        }
    }
    
    public void loadTexture(int textureID){
        super.loadInteger(location_texture, textureID);
        GL33.glBindSampler(textureID, location_texture);
    }
    
    public void loadNormal(int textureID){
        super.loadInteger(location_normal, textureID);
        GL33.glBindSampler(textureID, location_normal);
    }
    
    public void loadSpecular(int textureID){
        super.loadInteger(location_specular, textureID);
        GL33.glBindSampler(textureID, location_specular);
    }
    
    public void loadRoughness(int textureID){
        super.loadInteger(location_roughness, textureID);
        GL33.glBindSampler(textureID, location_roughness);
    }
}

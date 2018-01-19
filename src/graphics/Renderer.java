/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import base.engine.Game;
import base.util.DistanceCalculator;
import input.Keyboard;
import entity.Entity;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import lighting.Light;
import math.Maths;
import models.RawModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

import shaders.StaticShader;
import textures.Material;

/**
 *
 * @author Bailey
 */
public class Renderer {

    private static final float FOV = 70;
    private static final float  NEAR_PLANE = 0.1f;
    private static final float  FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    private FBO fbo;

    public Renderer(StaticShader shader){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();

        fbo = new FBO();

    }

    public void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GL11.glClear(GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);

        if (Keyboard.isKeyDown(KeyEvent.VK_R)) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }

        if (!Keyboard.isKeyDown(KeyEvent.VK_R)) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

    }

    public void render(StaticShader shader){
        int index = 0;


        for(LinkedList<Entity> e : Game.entityManager.getSortedEntities().values()){

            //bind model
            RawModel model = Game.modelManager.getModel((String) Game.entityManager.getSortedEntities().keySet().toArray()[index]);
            GL30.glBindVertexArray(model.getVaoID());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            //Normal, Tangent, Bitangent
            GL20.glEnableVertexAttribArray(2);
            GL20.glEnableVertexAttribArray(3);
            GL20.glEnableVertexAttribArray(4);

            int batch = 0;

            //per entity
            for(Entity entity : e) {
                if(entity.hasAttribute("shouldRender")){
                    //Lighting calculations
                    Light[] sortedLights = Game.lightingEngine.getLights();
                    Arrays.sort(sortedLights, new Comparator<Light>(){
                        @Override
                        public int compare(Light light1, Light light2){
                            return (int)(DistanceCalculator.distance(entity.getPosition(), light1.getPosition())- DistanceCalculator.distance(entity.getPosition(), light2.getPosition()));
                        }
                    });
                    shader.loadLights(sortedLights);
                    //material stuff
                    if (entity.hasMaterial()) {
                        Material material = entity.getMaterial();

                        //bind textures from the material
                        if (material.getTextureID() > 0) {
                            GL13.glActiveTexture(GL13.GL_TEXTURE0);
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.getTextureID());
                            shader.loadTexture(0);
                        }
                        if (material.getNormalID() > 0) {
                            GL13.glActiveTexture(GL13.GL_TEXTURE0 + 2);
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.getNormalID());
                            shader.loadNormal(2);
                        }
                        if (material.getSpecularID() > 0) {
                            GL13.glActiveTexture(GL13.GL_TEXTURE0 + 4);
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.getSpecularID());
                            shader.loadSpecular(4);
                        }
                        if (material.getRougnessID() > 0) {
                            GL13.glActiveTexture(GL13.GL_TEXTURE0 + 6);
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.getRougnessID());
                            shader.loadRoughness(6);
                        }
                    } else {
                        if (entity.getTextureID() > 0) {
                            GL13.glActiveTexture(GL13.GL_TEXTURE0);
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getTextureID());
                            shader.loadTexture(0);
                        }
                    }
                    //Load transform
                    Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
                    shader.loadTransformationMatrix(transformationMatrix);

                    //actual render
                    GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
                    batch++;
                }
            }
            //unbind model
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL20.glDisableVertexAttribArray(2);
            GL20.glDisableVertexAttribArray(3);
            GL20.glDisableVertexAttribArray(4);
            GL30.glBindVertexArray(0);
            index++;
        }
    }


    private void createProjectionMatrix(){
        float aspectRatio = (float) Game.WIDTH / (float) Game.HEIGHT;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.set(
                x_scale, 0, 0, 0,
                0, y_scale, 0, 0,
                0, 0, -((FAR_PLANE + NEAR_PLANE) / frustum_length), -1,
                0, 0, -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length), 0
        );

    }

    public FBO getFBO(){
        return fbo;
    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entity.Entity;
import graphics.Renderer;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class EntityModel extends Entity {

    public EntityModel(String modelID, String materialID, Vector3f position, float rotx, float roty, float rotz, float scale) {
        super(EnumEntityType.MODEL, modelID, materialID, position, rotx, roty, rotz, scale);
    }

    @Override
    public void update() {
//        super.rotate(0.1f, 0.1f, 0.1f);
    }

    @Override
    public void render(Renderer r, StaticShader shader) {
        
    }
    
}

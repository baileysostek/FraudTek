/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import Base.engine.Game;
import entity.component.ComponentCollision;
import graphics.Renderer;
import models.ModelLoader;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class EntityTable extends Entity{

    public EntityTable(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(EnumEntityType.TABLE, ModelLoader.generateCube(2, 1, 1), "brick", position, rotX, rotY, rotZ, scale);
        super.addComponent(new ComponentCollision(this));
    }

    @Override
    public void update() {
        
    }

    @Override
    public void render(Renderer r, StaticShader shader) {
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import Base.engine.Game;
import entity.component.*;
import graphics.Renderer;
import models.ModelLoader;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class EntityPlayer extends Entity{

    public EntityPlayer(Vector3f position) {
        super(EnumEntityType.PLAYER, ModelLoader.generateQuad(0.66f, 1), "front", position, 45, 0, 0, 1);
        super.addComponent(new ComponentGravity(this));
        super.addComponent(new ComponentController(this, 0));
        super.addComponent(new ComponentFPSController(this));
        super.addComponent(new ComponentCollision(this));

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }
    
}

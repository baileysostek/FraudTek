/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import base.engine.Game;
import entity.component.*;
import models.ModelLoader;
import models.RawModel;
import org.joml.Vector3f;
import shaders.Shader;

/**
 *
 * @author Bailey
 */
public class EntityVector extends Entity{

    private RawModel model;

    public EntityVector(Vector3f position) {
        super(EnumEntityType.PLAYER, "", position, 0, 0, 0, 1);
        ComponentMesh mesh = new ComponentMesh(this, ModelLoader.generateCube(0.66f, 1, 0.66f));
        super.addComponent(new ComponentCollision(this, mesh));
    }

    @Override
    public void render(Shader shader) {

    }

}

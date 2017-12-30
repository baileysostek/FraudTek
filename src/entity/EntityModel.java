/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entity.component.ComponentCollision;
import entity.component.ComponentMesh;
import entity.component.ComponentRender;
import entity.component.EnumComponentType;
import graphics.Renderer;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class EntityModel extends Entity {

    public EntityModel(String modelID, String materialID, Vector3f position, float rotx, float roty, float rotz, float scale) {
        super(EnumEntityType.MODEL, materialID, position, rotx, roty, rotz, scale);
        ComponentMesh mesh = new ComponentMesh(this, modelID);
        super.addComponent(mesh);
        super.addComponent(new ComponentCollision(this, mesh));
        super.addComponent(new ComponentRender(this, null));
    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }
    
}

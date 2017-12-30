/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import Base.engine.Game;
import entity.component.ComponentCollision;
import entity.component.ComponentMesh;
import entity.component.ComponentRender;
import entity.component.EnumComponentType;
import graphics.*;
import models.ModelLoader;
import org.joml.Vector3f;
import shaders.StaticShader;
import textures.Material;
import textures.MaterialManager;

/**
 *
 * @author Bailey
 */
public class EntitySprite extends Entity {
    Sprite sprite;

    public EntitySprite(Vector3f position, String spritePath, float rotx, float roty, float rotz) {
        super(EnumEntityType.SPRITE, "", position, rotx, roty, rotz, 1);

        //Load the sprite
        sprite = Game.spriteBinder.loadSprite(spritePath);

        //Link the entity material to a dynamic material instance of this newly loaded sprite.
        super.setMaterial(sprite.getID()+"");

        SpriteUtils.flipSpriteVertical(sprite);
        sprite.synchBuffer();

        ComponentMesh mesh = new ComponentMesh(this, ModelLoader.generateQuad(0.0625f * sprite.width, 0.0625f * sprite.height));

        super.addComponent(mesh);
        super.addComponent(new ComponentCollision(this, mesh));
        super.addComponent(new ComponentRender(this, null));
    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }

    public void synchSprite(){
        //Synch this sprite
        this.sprite.synchBuffer();
    }

    public Sprite getSprite(){
        return this.sprite;
    }
}

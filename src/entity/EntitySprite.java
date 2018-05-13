/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import base.engine.Game;
import entity.component.ComponentCollision;
import entity.component.ComponentMesh;
import entity.component.ComponentRender;
import graphics.*;
import models.ModelLoader;
import org.joml.Vector3f;
import shaders.Shader;

/**
 *
 * @author Bailey
 */
public class EntitySprite extends Entity {
    Sprite sprite;

    public float width = 0;

    public EntitySprite(Vector3f position, String spritePath, float rotx, float roty, float rotz, float scale) {
        super(EnumEntityType.SPRITE, "", position, rotx, roty + 180, rotz, scale);

        //Load the sprite
        sprite = Game.spriteBinder.loadSprite(spritePath);
        sprite = SpriteUtils.flipSpriteVertical(sprite);

        //Link the entity material to a dynamic material instance of this newly loaded sprite.
        super.setMaterial(sprite.getID()+"");
        super.setTexture(sprite.getID());


        ComponentMesh mesh = new ComponentMesh(this, ModelLoader.generateQuad(0.0625f * sprite.width, 0.0625f * sprite.height));

        super.addComponent(mesh);
        super.addComponent(new ComponentCollision(this, mesh));
        super.addComponent(new ComponentRender(this, null));

        width = 0.0625f * sprite.width;
    }

    @Override
    public void render(Shader shader) {

    }
    public void setNormal(String path){
        super.normalID = Game.spriteBinder.loadSprite(path).getID();
    }

    public void synchSprite(){
        //Synch this sprite
        this.sprite.synchBuffer();
    }

    public Sprite getSprite(){
        return this.sprite;
    }
}

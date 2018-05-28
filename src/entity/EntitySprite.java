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
import org.joml.Vector2f;
import org.joml.Vector3f;
import shaders.Shader;

/**
 *
 * @author Bailey
 */
public class EntitySprite extends Entity {
    Sprite sprite;

    private Vector2f size;

    public EntitySprite(Vector3f position, String spritePath, float rotx, float roty, float rotz, float scale) {
        super(EnumEntityType.SPRITE, "White", position, rotx, roty + 180, rotz, scale);

        //Load the sprite
        sprite = Game.spriteBinder.loadSprite(spritePath);
//        sprite = SpriteUtils.flipSpriteVertical(sprite);
//        sprite = SpriteUtils.flipSpriteHorisontal(sprite);

        //Link the entity material to a dynamic material instance of this newly loaded sprite.
        if(Game.materialManager.hasMaterial(spritePath)){
            super.setMaterial(Game.materialManager.getMaterial(spritePath));
        }else {
            super.setMaterial(Game.materialManager.clone("White", spritePath));
            super.getMaterial().overrideAlbedo(sprite);
        }

        size = new Vector2f(sprite.width * (1.0f / 32.0f), sprite.height * (1.0f / 32.0f));

        ComponentMesh mesh = new ComponentMesh(this, ModelLoader.loadModel("quad2", new Vector3f(size.x(), 1, size.y())));

        super.addComponent(mesh);
        super.addComponent(new ComponentCollision(this, mesh));
        super.addComponent(new ComponentRender(this, null));
    }

    @Override
    public void render(Shader shader) {

    }

    public Vector2f getSize(){
        return this.size;
    }

    public void synchSprite(){
        //Synch this sprite
        this.sprite.synchBuffer();
    }

    public Sprite getSprite(){
        return this.sprite;
    }
}

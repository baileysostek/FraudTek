/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particles;

import base.engine.Game;
import entity.Entity;
import entity.EnumEntityType;
import entity.component.ComponentRender;
import graphics.*;
import org.joml.Vector3f;
import shaders.Shader;

/**
 *
 * @author Bailey
 */
public class EntityParticleEmitter extends Entity {
    Sprite sprite;
    private Particle[] particles;

    private int numParticles = 1000;
    private int lifespan = 1000;
    final float rows;
    final float columns;
    boolean hasGravity = true;

    public EntityParticleEmitter(Vector3f position, String spritePath) {
        super(EnumEntityType.SPRITE, "White", position, 0, 0,0, 1);
        rows = 1.0f;
        columns = 43.0f;

        //Load the sprite
        sprite = Game.spriteBinder.loadSprite(spritePath);

        super.addComponent(new ComponentRender(this, null));

        particles = new Particle[numParticles];
        for(int i = 0; i < particles.length; i++){
            particles[i] = new Particle(new Vector3f(position), lifespan);
        }

    }

    @Override
    public void tick() {
        for(Particle p: particles){

            if(hasGravity){
                p.acceleration = p.acceleration.add(new Vector3f(0, (-0.000001f), 0));
            }

            p.velocity = p.velocity.add(p.acceleration);
            p.position = p.position.add(p.velocity);
            p.lifespan--;
            if(p.lifespan <= 0){
                p.position = new Vector3f(super.getPosition());
                p.lifespan = lifespan;
                p.resetVelocity();
            }
        }
    }

    public Particle[] getParticles(){
        return this.particles;
    }

    public Sprite getSprite(){
        return this.sprite;
    }


}

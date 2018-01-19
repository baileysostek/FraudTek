/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import base.engine.Game;
import com.google.gson.JsonObject;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import math.Maths;
import org.joml.Vector3f;
import shaders.StaticShader;
import world.GravityManager;

/**
 *
 * @author Bailey
 */
public class ComponentGravity extends Component{

    private Attribute<Float> gravity = new Attribute<Float>("gravity", GravityManager.GRAVITY/1200.0f);
    private Attribute<Boolean> onGround = new Attribute<Boolean>("onGround", false);
    
    public ComponentGravity(Entity e) {
        super(EnumComponentType.GRAVITY, e);
        gravity = super.addAttribute(gravity);
        onGround = super.addAttribute(onGround);
    }

    public ComponentGravity(Entity e, JsonObject object) {
        super(EnumComponentType.GRAVITY, e);
        gravity = super.addAttribute(gravity);
        onGround = super.addAttribute(onGround);
    }

    @Override
    public void tick() {
        //Check if gravity would put player into floor, if would, editor n times for new vector
        loop:{
            ComponentCollision collider = ((ComponentCollision) e.getComponent(EnumComponentType.COLLIDER));
            for(Entity entity: Game.entityManager.getEntities()){
                if(entity.hasComponent(EnumComponentType.COLLIDER)) {
                    ComponentCollision entityCollision = (ComponentCollision)entity.getComponent(EnumComponentType.COLLIDER);
                    if (!e.equals(entityCollision)) {
                        //Get velocity this entity is traveling at
                        //TODO as well as other entity
                        Vector3f dir = Maths.newInstance(e.getVelocity()).add(new Vector3f(0, gravity.getData(), 0)).add(e.getAcceleration()).mul(new Vector3f(0, 1, 0));
                        Vector3f hitPoint = new Vector3f(0, 0, 0);
                        if (entityCollision.rayHitsMesh(dir, Maths.newInstance(e.getPosition()).add(0, -collider.getHeight() / 2, 0), hitPoint)) {
                            e.setVelocity(0, 0, 0);
                            e.setAcceleration(0, 0, 0);
                            e.setPosition(hitPoint.add(0, collider.getHeight() / 2, 0));
                            onGround.setData(true);
                            //Send collision Events to both Objects
                            collider.runCollisionCallback();
                            entityCollision.runCollisionCallback();
                            break loop;
                        }
                    }
                }
            }
            //Case where player is not in ground
            onGround.setData(false);
            e.addAcceleration(0, gravity.getData(), 0);
        }
    }

    @Override
    public void render(Renderer r, StaticShader shader) {
        
    }
    
}

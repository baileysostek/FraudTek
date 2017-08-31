/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import Base.engine.Game;
import Base.util.DistanceCalculator;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import graphics.Sprite;
import java.awt.Color;
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

    @Override
    public void tick() {
        //Check if gravity would put player into floor, if would, test n times for new vector
        loop:{
            for(Entity entity: Game.entityManager.getEntities()){
                //Check that its not checking a collision with itself
                //TODO replace this with refrence to collision checker broadfase SAP program
                if(!e.equals(entity)){
                    //Get velocity this entity is traveling at
                    //TODO as well as other entity
                    Vector3f dir = Maths.newInstance(e.getVelocity()).add(new Vector3f(0, gravity.getData(), 0));
                    Vector3f hitPoint = new Vector3f(0,0,0);
                    if(entity.rayHitsMesh(dir, Maths.newInstance(e.getPosition()).add(0, -e.getHeight()/2, 0), hitPoint)){
                        e.setVelocity(0,0,0);
                        e.setAcceleration(0,0,0);
                        e.setPosition(hitPoint.add(0, e.getHeight()/2, 0));
                        onGround.setData(true);
                        break loop;   
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

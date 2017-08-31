/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import Base.engine.Game;
import entity.Entity;
import graphics.Renderer;
import math.Maths;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class ComponentCollision extends Component{

    public ComponentCollision(Entity e) {
        super(EnumComponentType.COLLIDER, e);
    }
    
    @Override
    public void tick() {
        //Check if gravity would put player into floor, if would, test n times for new vector
        Vector3f dir = new Vector3f(e.getVelocity()).add(e.getAcceleration());
        loop:{
            for(Entity entity: Game.entityManager.getEntities()){
                //Check that its not checking a collision with itself
                //TODO replace this with refrence to collision checker broadfase SAP program
                if(!e.equals(entity)){
                    Vector3f offset = new Vector3f();
                    //Move up vector
                    if(entity.rayHitsMesh(new Vector3f(0, -e.getHeight(), 0), new Vector3f(e.getPosition()).add(0, e.getHeight()/2f, 0), offset)){
                        e.setPosition(offset.add(0, e.getHeight()/2, 0));
                    }
                    //Get velocity this entity is traveling at
                    //TODO as well as other entity
                    //Create rays from the center of each face of the AABB
                    Vector3f hitPoint = new Vector3f(0,0,0);
                    if(dir.y>0){
                        //Up
                        if(entity.rayHitsMesh(new Vector3f(e.getVelocity()).add(e.getAcceleration()), Maths.newInstance(e.getPosition()).add(0, e.getHeight()/2, 0), hitPoint)){
                            e.setVelocity(0,0,0);
                            e.setAcceleration(0,0,0);
                            break loop;   
                        }
                    }
                    if(dir.y<0){
                        //Down
                        if(entity.rayHitsMesh(new Vector3f(e.getVelocity()).add(e.getAcceleration()), Maths.newInstance(e.getPosition()).add(0, -e.getHeight()/2, 0), hitPoint)){;
                            e.setVelocity(0,0,0);
                            e.setAcceleration(0,0,0);
                            break loop;   
                        }
                    }
                    if(dir.z<0){
                        //North
                        if(entity.rayHitsMesh(new Vector3f(e.getVelocity()).add(e.getAcceleration()), Maths.newInstance(e.getPosition()).add(0, 0, -e.getDepth()/2), hitPoint)){
                            e.setVelocity(0,0,0);
                            e.setAcceleration(0,0,0);
                            break loop;   
                        }
                    }
                    if(dir.z>0){
                        //South
                        if(entity.rayHitsMesh(new Vector3f(e.getVelocity()).add(e.getAcceleration()), Maths.newInstance(e.getPosition()).add(0, 0, e.getDepth()/2), hitPoint)){
                            e.setVelocity(0,0,0);
                            e.setAcceleration(0,0,0);
                            break loop;   
                        }
                    }
                    if(dir.x<0){
                        //West
                        if(entity.rayHitsMesh(new Vector3f(e.getVelocity()).add(e.getAcceleration()), Maths.newInstance(e.getPosition()).add(-e.getWidth()/2, 0, 0), hitPoint)){
                            e.setVelocity(0,0,0);
                            e.setAcceleration(0,0,0);
                            break loop;      
                        }
                    }
                    if(dir.x>0){
                        //East
                        if(entity.rayHitsMesh(new Vector3f(e.getVelocity()).add(e.getAcceleration()), Maths.newInstance(e.getPosition()).add(e.getWidth()/2, 0, 0), hitPoint)){
                            e.setVelocity(0,0,0);
                            e.setAcceleration(0,0,0);
                            break loop;     
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }
    
}

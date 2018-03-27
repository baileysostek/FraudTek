/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author Bailey
 */
public abstract class Camera {
    
    protected Vector3f position = new Vector3f(0,0,0);
    protected Vector3f rotation = new Vector3f(0,0,0);
    
    protected float speed = 0.1f;
    
    public Camera(){
    
    }
    
    public Camera(Vector3f position, Vector3f rotation){
        this.position = position;
        this.rotation = rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    
    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getLookingDirection(){
        Matrix4f rotationMatrix = new Matrix4f();
        rotationMatrix.rotateY((float) Math.toRadians(-rotation.x()));
        rotationMatrix.rotateX((float) Math.toRadians(-rotation.y()));
//        rotationMatrix.rotateZ((float) Math.toRadians(rotation.z()));
        return new Vector3f(0, 0, -1).mulDirection(rotationMatrix);
    }
    
    public abstract void tick();
}

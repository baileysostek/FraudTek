/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import base.engine.Game;
import camera.Camera;
import entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author Bailey
 */
public class Maths {
    private static final float FOV = 70;
    private static final float  NEAR_PLANE = 0.1f;
    private static final float  FAR_PLANE = 1000;

    private static Matrix4f projectionMatrix;

    public static Matrix4f createTransformationMatrix(Vector2f translation, float ry, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.translate(translation.x, translation.y, 0);
        matrix.scale(scale.x, scale.y, 0);
        matrix.rotateY((float) Math.toRadians(ry));
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){
        Matrix4f matrix = new Matrix4f();
                matrix.translate(translation);
        matrix.rotateX((float) Math.toRadians(rx));
        matrix.rotateY((float) Math.toRadians(ry));
        matrix.rotateZ((float) Math.toRadians(rz));
        matrix.scale(scale);
        return matrix;
    }
    
    public static Matrix4f createViewMatrix(Camera camera){
        Matrix4f out = new Matrix4f();
        out.rotate((float) Math.toRadians(camera.getRotation().y), 1f, 0f, 0f); //Pitch rotated first
        out.rotate((float) Math.toRadians(camera.getRotation().x), 0f, 1f, 0f); //Yaw
        out.rotate((float) Math.toRadians(camera.getRotation().z), 0f, 0f, 1f); //Roll
        out.translate(inverseVector(camera.getPosition()));
        return out;
    }

    public static Matrix4f getCameraRot(Camera camera){
        Matrix4f out = new Matrix4f();
        out.rotate((float) Math.toRadians(camera.getRotation().y), 1f, 0f, 0f); //Pitch rotated first
        out.rotate((float) Math.toRadians(camera.getRotation().x), 0f, 1f, 0f); //Yaw
        out.rotate((float) Math.toRadians(camera.getRotation().z), 0f, 0f, 1f); //Roll
        return out;
    }

    public static Matrix4f getEntityRot(Entity entity){
        Matrix4f out = new Matrix4f();
        out.rotate((float) Math.toRadians(entity.getRotX()), 1f, 0f, 0f); //Yaw
        out.rotate((float) Math.toRadians(entity.getRotY()), 0f, 1f, 0f); //Pitch rotated first
        out.rotate((float) Math.toRadians(entity.getRotZ()), 0f, 0f, 1f); //Roll
        return out;
    }


    public static Vector3f inverseVector(Vector3f in){
        return new Vector3f(-in.x, -in.y, -in.z);
    }
    
    public static Vector3f newInstance(Vector3f base){
        return new Vector3f(base.x, base.y, base.z);
    }
    
    public static boolean pointInside(Vector3f point, Vector3f[] box, Vector3f... offsetts){
        Vector3f totalOffset = new Vector3f(0,0,0);
        for(Vector3f offset: offsetts){
            totalOffset.x += offset.x();
            totalOffset.y += offset.y();
            totalOffset.z += offset.z();
        }
        if(point.x >= box[0].x + totalOffset.x && point.x <= box[1].x + totalOffset.x){
            if(point.y >= box[0].y + totalOffset.y && point.y <= box[1].y + totalOffset.y){
                if(point.z >= box[0].z + totalOffset.z && point.z <= box[1].z + totalOffset.z){
                    return true;
                }
            } 
        }
        
        return false;
    }

    public static void createProjectionMatrix(){
        float aspectRatio = (float) Game.WIDTH / (float) Game.HEIGHT;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.set(
                x_scale, 0, 0, 0,
                0, y_scale, 0, 0,
                0, 0, -((FAR_PLANE + NEAR_PLANE) / frustum_length), -1,
                0, 0, -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length), 0
        );
    }

    public static Matrix4f getProjectionMatrix(){
        if(projectionMatrix == null){
            createProjectionMatrix();
        }
        return projectionMatrix;
    }
}

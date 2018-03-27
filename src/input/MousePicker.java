/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import base.engine.Game;
import math.Maths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

/**
 *
 * @author Bailey
 */
public class MousePicker {

    private Vector3f currentRay;
    
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    
    public MousePicker(Matrix4f projection){
        this.projectionMatrix = projection;
        this.viewMatrix = Maths.createViewMatrix(Game.cameraManager.getCam());
        currentRay = calculateMouseRay();
    }
    
    public Vector3f getCurrentRay(){
        return this.currentRay;
    }
    
    public void tick(){
        this.viewMatrix = Maths.createViewMatrix(Game.cameraManager.getCam());
        currentRay = calculateMouseRay();
    }

    public boolean isPressed(EnumMouseButton index){
        return Mouse.pressed(index);
    }

    //Return Vector3f in ScreenSpace
    public Vector3f getMouseCoords(){
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(Game.getWindowPointer(), posX, posY);

        return new Vector3f(((float) posX.get(0) - Game.WIDTH/2) * 2, (Game.HEIGHT - (float) posY.get(0) - Game.HEIGHT/2) * 2, 0);
    }

    public Vector3f calculateMouseRay() {
        float mouseX = Game.MouseX;
        float mouseY = Game.MouseY;
        Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = new Matrix4f();
        invertedView.set(viewMatrix);
        invertedView.invert(invertedView);
        Vector4f rayWorld = new Vector4f();
        invertedView.transform(eyeCoords, rayWorld);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalize(mouseRay);
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = new Matrix4f();
        projectionMatrix.invert(invertedProjection);
        Vector4f eyeCoords = new Vector4f();
        invertedProjection.transformAffine(clipCoords, eyeCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2.0f * mouseX) / Game.WIDTH - 1f;
        float y = (2.0f * mouseY) / Game.HEIGHT - 1f;
        return new Vector2f(x, y);
    }


}

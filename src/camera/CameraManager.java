/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import Base.engine.Game;
import Base.util.Engine;
import graphics.Renderer;
import input.Keyboard;
import java.awt.event.KeyEvent;
import javax.script.ScriptEngine;
import lighting.Light;
import math.Maths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class CameraManager extends Engine{

    private Camera camera;

    private boolean isTransitioning = false;
    private Vector3f initial_pos;
    private Vector3f initial_rot;
    private Camera target = null;
    private Camera lastCam = null;
    private int targetTicks = 0;
    private int currentTicks = 0;
    
    public CameraManager() {
        super("CameraManager");
        this.camera = new FPSCamera();
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        if(camera!=null){
            if(isTransitioning){
                if(currentTicks < targetTicks){
                    Vector3f pos_dif = new Vector3f(target.getPosition()).sub(initial_pos).mul((float)currentTicks / (float)targetTicks);
                    Vector3f rot_dif = new Vector3f(target.getRotation()).sub(initial_rot).mul((float)currentTicks / (float)targetTicks);
                    camera.setPosition(new Vector3f(initial_pos).add(pos_dif));
                    camera.setRotation(new Vector3f(initial_rot).add(rot_dif));
                    currentTicks++;
                }else{
                    //Transition has ended, so turn of transition flag
                    this.isTransitioning = false;
                    this.camera = lastCam;
                }
            }
            camera.tick();
        }
    }

    @Override
    public void render(Renderer renderer, StaticShader shader) {
    
    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(this.getName(), this);
    }

    @Override
    public void onShutdown() {

    }

    public boolean isTransitioning(){
        return this.isTransitioning;
    }

    public void transition(Camera target, int frames){
        if(isTransitioning){
            return;
        }
        this.target = target;
        this.initial_pos = new Vector3f(camera.getPosition());
        this.initial_rot = new Vector3f(camera.getRotation());
        this.targetTicks = frames;
        this.currentTicks = 0;
        this.lastCam = this.camera;
        this.camera = new DynamicCamera();
        isTransitioning = true;
    }
    
    public void setCamera(Camera camera){
        this.camera = camera;
    }
    
    public Camera getCam(){
        return this.camera;
    }
    
}

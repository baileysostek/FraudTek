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
    private Light light;
    
    public CameraManager() {
        super("CameraManager");
        this.camera = new FPSCamera();
        light = new Light(new Vector3f(camera.getPosition()), new Vector3f(1,1, 1), new Vector3f(0.1f, 0.1f, 0.1f));
        Game.lightingEngine.addLight(light);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        if(camera!=null){
            camera.tick();
            if(Keyboard.isKeyDown(KeyEvent.VK_L)){
                light.setPosition(new Vector3f(camera.getPosition()));
            }
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
    
    public void setCamera(Camera camera){
        this.camera = camera;
    }
    
    public Camera getCam(){
        return this.camera;
    }
    
}

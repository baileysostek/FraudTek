/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lighting;

import base.util.DynamicCollection;
import base.engine.Engine;
import javax.script.ScriptEngine;
import graphics.Renderer;
import org.joml.Vector3f;

/**
 *
 * @author Bailey
 */
public class LightingEngine extends Engine{

    private DynamicCollection<Light> lights = new DynamicCollection<Light>() {
        @Override
        public void onAdd(Light object) {

        }

        @Override
        public void onRemove(Light object) {

        }
    };
    
    public LightingEngine() {
        super("LightingEngine");
    }

    @Override
    public void init() {
        
    }

    @Override
    public void tick() {
        lights.synch();
    }


    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(), this);
    }

    @Override
    public void onShutdown() {
        
    }
    
    public void addLight(Light light){
        this.lights.add(light);
    }

    public Light addLight(Vector3f pos, Vector3f color){
        Light light = new Light(pos, color, new Vector3f(0.1f, 0.1f, 0.1f));
        this.lights.add(light);
        return light;
    }

    public void addWorldLight(Vector3f pos, Vector3f color){
        this.lights.add(new Light(pos, color));
    }
    
    public void removeLight(Light light){
        this.lights.remove(light);
    }

    public Light[] getLights(){
        return this.lights.getCollection(Light.class);
    }
    
}

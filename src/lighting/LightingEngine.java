/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lighting;

import Base.util.DynamicCollection;
import Base.util.Engine;
import javax.script.ScriptEngine;
import graphics.Renderer;
import shaders.StaticShader;

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
    public void render(Renderer renderer, StaticShader shader) {
        
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
    
    public void removeLight(Light light){
        this.lights.remove(light);
    }

    public void loadLights(StaticShader shader) {
        shader.loadLights(lights.getCollection(Light.class));
    }
    
    public Light[] getLights(){
        return this.lights.getCollection(Light.class);
    }
    
}

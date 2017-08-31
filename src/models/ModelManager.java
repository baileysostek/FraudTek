/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import Base.util.Engine;
import graphics.Renderer;
import java.util.HashMap;
import javax.script.ScriptEngine;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class ModelManager extends Engine{

    private HashMap<String, RawModel> loadedModels = new HashMap<String, RawModel>();
    
    public ModelManager() {
        super("ModelManager");
    }

    @Override
    public void init() {
    
    }

    @Override
    public void tick() {
    
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
    
    public void addModel(String key, RawModel model){
        this.loadedModels.put(key, model);
    }
    
    public boolean hasModel(String id){
        return this.loadedModels.containsKey(id);
    }
    
    public RawModel getModel(String id){
        return this.loadedModels.get(id);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import base.engine.Engine;
import base.engine.Game;

import java.util.HashMap;
import javax.script.ScriptEngine;

/**
 *
 * @author Bailey
 */
public class ModelManager extends Engine{

    private HashMap<String, Model> loadedModels = new HashMap<String, Model>();
    
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
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(), this);
    }

    @Override
    public void onShutdown() {
        
    }
    
    public void addModel(String key, Model model){
        this.loadedModels.put(key, model);
    }
    
    public boolean hasModel(String id){
        return this.loadedModels.containsKey(id);
    }
    
    public Model getModel(String id){
        return this.loadedModels.get(id);
    }
    
}

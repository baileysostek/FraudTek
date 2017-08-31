/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptingEngine;

import Base.engine.Game;
import entity.EnumEntityType;
import java.io.IOException;
import java.util.LinkedList;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Bayjose
 */
public class ScriptingEngine {

    private Script[] scripts = new Script[]{};
    private LinkedList<Script> toAdd = new LinkedList<Script>();
    private LinkedList<Script> toRemove = new LinkedList<Script>();
    
    private final ScriptEngineManager manager = new ScriptEngineManager();
    private final ScriptEngine engine = manager.getEngineByName("JavaScript");
    private final Invocable inv = (Invocable) engine;
    
    public ScriptingEngine(){
        //Register Commands
        engine.put("out", System.out);
    }

    private void updateScripts(){
        //only run if need to
        if(toRemove.size()>0 || toAdd.size()>0){
            LinkedList<Script> newBodies = new LinkedList<Script>();
            //remove all lights flagged to remove
            for(int i = 0; i < this.scripts.length; i++){
                if(toRemove.contains(this.scripts[i])){
                    //Remove that body
                }else{
                    newBodies.add(this.scripts[i]);
                }
            }
            //clear the buffer
            toRemove.clear();
            
            //add all new lights
            for (Script body : toAdd) {
                newBodies.add(body);
            }
            //clear that buffer
            toAdd.clear();
            //build new light array
            Script[] out = new Script[newBodies.size()];
            for(int i = 0; i < newBodies.size(); i++){
                out[i] = newBodies.get(i);
            }
            //set it
            this.scripts = out;
        }
    }
    
    public void tick(){
        updateScripts();  
        for(Script script : scripts){
            try {
                script.tick(inv);
            } catch (ScriptException | NoSuchMethodException ex) {
                ex.printStackTrace();
                remove(script);
            }
        }
    }
    
    public void remove(Script object){
        toRemove.add(object);
    }
    
    public void add(Script object){
        if(object!=null){
            toAdd.add(object);
        }
    }
    
    public Script add(String name){
        Script script;
        try {
            script = new Script(engine, Game.Path+"/Scripting/"+name);
            script.init(inv);
        } catch (ScriptException | IOException | NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
        toAdd.add(script);
        return script;
    }

    public void killScripts(String name) {
        for(Script bod : scripts){
            if(bod.getFilePath().equals(name)){
                remove(bod);
            }
        }
    }
    
    public void killAllScripts() {
        for(Script bod : scripts){
            remove(bod);
        }
    }
    
    public Script findScript(String name, Script[] scripts){
        for(int i = 0; i < scripts.length; i++){
            if(scripts[i].getFilePath().replace(".js", "").equals(name)){
                return scripts[i];
            }
        }
        return null;
    }
    
    public Script[] getScripts(){
        return scripts;
    }
    
    public ScriptEngine getEngine(){
        return engine;
    }
}
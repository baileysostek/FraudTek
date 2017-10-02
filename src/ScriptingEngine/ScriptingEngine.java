/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptingEngine;

import Base.engine.Game;
import Base.util.DynamicCollection;
import java.io.IOException;
import javax.script.ScriptException;

/**
 *
 * @author Bayjose
 */
public class ScriptingEngine {

    private DynamicCollection<Script> scripts = new DynamicCollection<Script>() {
        @Override
        public void onAdd(Script object) {
            object.put("out", System.out);
        }

        @Override
        public void onRemove(Script object) {

        }
    };
    
    public ScriptingEngine(){
        //Register Commands
    }

    public void put(String name, Object object){
        for(Script script : scripts.getCollection(Script.class)){
            script.put(name, object);
        }
    }

    public void tick(){
        for(Script script : scripts.getCollection(Script.class)){
            try {
                script.tick();
            } catch (ScriptException | NoSuchMethodException ex) {
                ex.printStackTrace();
                remove(script);
            }
        }
    }
    
    public void remove(Script object){
        scripts.remove(object);
        scripts.synch();
    }
    
    public void add(Script object){
        if(object!=null){
            scripts.add(object);
            scripts.synch();
        }
    }
    
    public Script add(String name, Object... pars){
        Script script;
        try {
            script = new Script(Game.Path+"/Scripting/"+name);
            script.init(pars);
            scripts.add(script);
            System.out.println(script+" "+pars[0]);
            scripts.synch();
        } catch (ScriptException | IOException | NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
        return script;
    }

    public void runFunction(Script script, String name, Object... args){
        try {
            script.run(name, args);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public Script[] getScripts(){
        return scripts.getCollection(Script.class);
    }
}
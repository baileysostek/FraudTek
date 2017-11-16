/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptingEngine;

import Base.engine.Game;
import Base.util.Debouncer;
import Base.util.DynamicCollection;
import Base.util.Engine;
import camera.FPSCamera;
import graphics.gui.Gui;
import input.EnumMouseButton;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.HashMap;
import javax.script.ScriptException;

/**
 *
 * @author Bayjose
 */
public class ScriptingEngine {

    private HashMap<String, Object> refrences = new HashMap<String, Object>();
    private HashMap<String, Class> classes = new HashMap<String, Class>();

    private DynamicCollection<Script> scripts = new DynamicCollection<Script>() {
        @Override
        public void onAdd(Script object) {
            //Add all references
            for(String s : refrences.keySet()){
                object.put(s, refrences.get(s));
            }
            //Add all classes
            for (Class c:classes.values()){
                object.addClass(c);
            }
        }

        @Override
        public void onRemove(Script object) {

        }
    };
    
    public ScriptingEngine(DynamicCollection<Engine> engines){
        //Register Engines
        for(Engine e : engines.getCollection(Engine.class)){
            refrences.put(e.getName(), e);
        }
        //Register Commands
        refrences.put("WIDTH", Game.WIDTH);
        refrences.put("HEIGHT", Game.HEIGHT);

        //Class
        addClass(System.class);
        addClass(Vector2f.class);
        addClass(Vector3f.class);
        addClass(Gui.class);
        addClass(Debouncer.class);
        addClass(EnumMouseButton.class);
            //Camera classes
            addClass(FPSCamera.class);
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
            scripts.add(script);
            if(pars.length > 0) {
                System.out.println(script + " " + pars[0]);
            }
            scripts.synch();
            script.init(pars);
        } catch (ScriptException | IOException | NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
        return script;
    }

    public void addRefrence(String name, Object data){
        refrences.put(name, data);
    }

    public void addClass(Class className){
        System.out.println("Adding class:"+className.getSimpleName()+" class:"+className);
        classes.put(className.getSimpleName(), className);
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
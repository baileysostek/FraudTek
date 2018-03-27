/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptingEngine;

import base.engine.Game;
import base.util.*;
import base.engine.Engine;
import camera.DynamicCamera;
import camera.FPSCamera;
import editor.IntellisenseEngine;
import entity.Entity;
import entity.EntityModel;
import entity.EntityPlayer;
import entity.EntitySprite;
import entity.component.EnumComponentType;
import graphics.FBO;
import graphics.VAO;
import graphics.gui.Gui;
import input.EnumMouseButton;
import input.Keyboard;
import input.Mouse;
import input.MousePicker;
import math.Maths;
import models.ModelLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shaders.Shader;
import textures.MaterialManager;

import java.awt.event.KeyEvent;
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

        refrences.put("Log", Game.logManager);

        //Class
        addClass(Vector2f.class);
        addClass(Vector3f.class);
        addClass(Vector4f.class);
        addClass(Gui.class);
        addClass(Debouncer.class);
        addClass(DistanceCalculator.class);
        addClass(EnumMouseButton.class);
        addClass(EntityModel.class);
        addClass(EntityPlayer.class);
        addClass(EntitySprite.class);
        addClass(ModelLoader.class);
        addClass(Entity.class);

        //Input classes
        addClass(Keyboard.class);
        addClass(KeyEvent.class);
        addClass(Mouse.class);
        addClass(EnumMouseButton.class);

        //Rendering classes
        addClass(Shader.class);
        addClass(FBO.class);
        addClass(VAO.class);

            //Camera classes
            addClass(FPSCamera.class);
            addClass(DynamicCamera.class);

            //Utility classes
            addClass(Maths.class);
            addClass(EnumComponentType.class);

            //GL classes
            addClass(GL11.class);
            addClass(GL20.class);
            addClass(GL30.class);
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

    public void render(){
        for(Script script : scripts.getCollection(Script.class)){
            try {
                script.render();
            } catch (ScriptException | NoSuchMethodException | NullPointerException ex) {
                if(ex instanceof NullPointerException){
                    Game.logManager.println("NULL was returned from Script:"+script.getFilePath()+" Line:"+((NullPointerException) ex).getLocalizedMessage(), EnumErrorLevel.ERROR);
                }
                System.err.println("Script:"+script.getFilePath()+" has no member 'render()'");
                ex.printStackTrace();
                remove(script);
            }
        }
    }


    public void remove(Script object){
        scripts.remove(object);
        scripts.synch();
    }
    
//    public void add(Script object){
//        if(object!=null){
//            object.put("self", object);
//            scripts.add(object);
//            scripts.synch();
//        }
//    }
    
    public Script addScript(String name, Object... pars){
        Script script;
        try {
            script = new Script(Game.Path+"/Scripting/"+name);
            script.put("self", script);
            scripts.add(script);
            scripts.synch();
            script.init(pars);
        } catch (ScriptException | IOException | NoSuchMethodException ex) {
            ex.printStackTrace();
            Game.logManager.println("[ERROR}"+(ex.getLocalizedMessage().replace("<eval>", "Script:"+name)));
            return null;
        }
        return script;
    }

    public void addRefrence(String name, Object data){
        refrences.put(name, data);
    }

    public void addClass(Class className){
        Game.logManager.println("Adding class:"+className.getSimpleName()+" class:"+className);
        classes.put(className.getSimpleName(), className);
        IntellisenseEngine.cacheFile(className);
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

    public HashMap<String, Object> getRefrences(){
        return this.refrences;
    }

    public Script[] getScripts(){
        return scripts.getCollection(Script.class);
    }
}
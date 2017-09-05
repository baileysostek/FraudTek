/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import Base.util.DynamicCollection;
import Base.util.Engine;
import Base.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.awt.Graphics;
import javax.script.ScriptEngine;

import entity.component.ComponentMesh;
import entity.component.EnumComponentType;
import graphics.Renderer;
import java.util.HashMap;
import java.util.LinkedList;
import lighting.Light;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class EntityManager extends Engine{

    private HashMap<String, LinkedList<Entity>> sorted = new HashMap<String, LinkedList<Entity>>();
    
    private DynamicCollection<Entity> entities = new DynamicCollection<Entity>() {
        @Override
        public void onAdd(Entity entity) {
            entity.onAdded();
            if(entity.hasComponent(EnumComponentType.MESH)){
                if(sorted.containsKey(((ComponentMesh)entity.getComponent(EnumComponentType.MESH)).getModelID())){
                    sorted.get(((ComponentMesh)entity.getComponent(EnumComponentType.MESH)).getModelID()).add(entity);
                }else{
                    LinkedList<Entity> modelEntities = new LinkedList<Entity>();
                    modelEntities.add(entity);
                    sorted.put(((ComponentMesh)entity.getComponent(EnumComponentType.MESH)).getModelID(), modelEntities);
                }
            }
        }

        @Override
        public void onRemove(Entity object) {
            object.onRemoved();
        }
    };
    
    public EntityManager() {
        super("EntityManager");
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        entities.synch();
        for(Entity e : this.entities.getCollection(Entity.class)){
            e.tick();
        }
    }

    @Override
    public void render(Renderer r, StaticShader shader) {
        for(Entity e : this.entities.getCollection(Entity.class)){
            e.render(r, shader);
        }
    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(), this);
    }

    @Override
    public void onShutdown() {
        
    }
    
    public void addEntity(Entity e){
        this.entities.add(e);
    }
    
    public void remove(Entity e){
        this.entities.remove(e);
    }
    
    public int getLength(){
        return this.entities.getLength();
    }
    
    public Entity generate(EnumEntityType entity, Object inData)throws EntityNotRecognisedException{
        Gson gson = new Gson();
        JsonObject data = gson.fromJson(gson.toJson(inData), JsonObject.class);
        Entity out = null;
        
        Vector3f position = new Vector3f(0,0,0);
        if(data.has("position")){
            position.x = gson.fromJson(data.get("position").getAsJsonObject().get("x"), Float.class);
            position.y = gson.fromJson(data.get("position").getAsJsonObject().get("y"), Float.class);
            position.z = gson.fromJson(data.get("position").getAsJsonObject().get("z"), Float.class);
        } 
        
//        switch(entity){
//            
//        }
            
        if(out != null){
            //Check for rotation
            if(data.has("rotation")){
                Vector3f rotation = new Vector3f(0,0,0);
                if(data.get("rotation").getAsJsonObject().has("x")){
                    rotation.x = gson.fromJson(data.get("rotation").getAsJsonObject().get("x"), Float.class);
                }
                if(data.get("rotation").getAsJsonObject().has("y")){
                    rotation.y = gson.fromJson(data.get("rotation").getAsJsonObject().get("y"), Float.class);
                }
                if(data.get("rotation").getAsJsonObject().has("z")){
                    rotation.z = gson.fromJson(data.get("rotation").getAsJsonObject().get("z"), Float.class);
                }
                out.setRotX(rotation.x);
                out.setRotY(rotation.y);
                out.setRotZ(rotation.z);
            }
            
            //Check for ID 
            if(data.has("id")){
                out.setID(gson.fromJson(data.get("id"), String.class));
            }
            
            return out;
        }
        
        throw new EntityNotRecognisedException(entity, data);
    }
    
    public Entity[] getEntities(){
        return this.entities.getCollection(Entity.class);
    }

    public HashMap<String, LinkedList<Entity>> getSortedEntities(){
        return this.sorted;
    }

    public void generateSave(String path){
        String[] data = new String[]{};
        for(Entity e : getEntities()){

        }
        StringUtils.saveData(path, data);
    }
    
}

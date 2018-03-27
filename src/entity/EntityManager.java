/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import base.engine.Game;
import base.util.DynamicCollection;
import base.engine.Engine;
import base.util.LogManager;
import base.util.StringUtils;
import com.google.gson.*;

import javax.script.ScriptEngine;

import entity.component.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.joml.Vector3f;
import shaders.Shader;

/**
 *
 * @author Bailey
 */
public class EntityManager extends Engine{
    //Location of Entities
    private String path = "/Scripting/Entities/index.json";
    private HashMap<String, JsonObject> entity_index = new HashMap<String, JsonObject>();
    //A sorted list of entities in world, sorted by model, for batch rendering.
    private HashMap<String, LinkedList<Entity>> sorted = new HashMap<String, LinkedList<Entity>>();
    //The dynamic collection of entities that exist in the world.
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
        //Load data from index into List
        Gson gson = new Gson();
        JsonObject entities = gson.fromJson(StringUtils.unify(StringUtils.loadData(Game.Path+"/Scripting/Entities/index.json")), JsonObject.class);
        if(entities.has("entities")) {
            JsonArray jsonArray = entities.getAsJsonArray("entities");
            for (JsonElement element : jsonArray) {
                JsonObject object = element.getAsJsonObject();
                if (object.has("name")) {
                    System.out.println("Entity:"+object.get("name")+" src:"+object.get("file"));
                    entity_index.put(gson.fromJson(object.get("name"), String.class), gson.fromJson(StringUtils.unify(StringUtils.loadData(Game.Path+"/Scripting/Entities/"+gson.fromJson(object.get("file"), String.class))), JsonObject.class));
                }
            }
        }
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


    public void render(Shader shader) {
        for(Entity e : this.entities.getCollection(Entity.class)){
            e.render(shader);
        }
    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(), this);
    }

    @Override
    public void onShutdown() {
        
    }

    public Entity generate(Vector3f position, String e){
        if(entity_index.containsKey(e)) {
            Gson gson = new Gson();
            Entity add = new Entity(EnumEntityType.CUSTOM, "white", position, 0, 0, 0, 1);

//            ComponentMesh mesh = new ComponentMesh(add, ModelLoader.generateCube(1, 1, 1));
//            add.addComponent(mesh);
//
//            add.addComponent(new ComponentRender(add));

            JsonObject data = entity_index.get(e);

            if (data.has("positionOffset")) {
                add.setPosition(new Vector3f(
                        add.getPosition().x() + gson.fromJson(data.get("positionOffset").getAsJsonObject().get("x"), Float.class),
                        add.getPosition().y() + gson.fromJson(data.get("positionOffset").getAsJsonObject().get("y"), Float.class),
                        add.getPosition().z() + gson.fromJson(data.get("positionOffset").getAsJsonObject().get("z"), Float.class)));
            }

            if (data.has("rotation")) {
                add.setRotX(gson.fromJson(data.get("rotation").getAsJsonObject().get("x"), Float.class));
                add.setRotY(gson.fromJson(data.get("rotation").getAsJsonObject().get("y"), Float.class));
                add.setRotZ(gson.fromJson(data.get("rotation").getAsJsonObject().get("z"), Float.class));
            }
            if (data.has("scale")) {
                add.setScale(gson.fromJson(data.get("scale"), Float.class));
            }
            if (data.has("Components")) {
                Set<Map.Entry<String, JsonElement>> components = data.get("Components").getAsJsonObject().entrySet();
                for (Map.Entry<String, JsonElement> component : components) {
                    Component component_to_add = ComponentUtils.buildComponent(add, component.getKey(), data.get("Components").getAsJsonObject().get(component.getKey()).getAsJsonObject());
                    add.addComponent(component_to_add);
                }
            }
            return add;
        }
        Game.logManager.println("[ERROR] Entity:"+e+" is not a defined entity type. Check index.json for a list of entities.");
        return null;
    }

    public void addEntity(String e, Vector3f position){
        if(entity_index.containsKey(e)){
            Entity entity = generate(position, e);
            this.entities.add(entity);
        }
    }
    
    public void addEntity(Entity e){
        this.entities.add(e);
    }
    
    public void remove(Entity e){
        this.entities.remove(e);
    }

    public void synch(){
        entities.synch();
    }
    
    public int getLength(){
        return this.entities.getLength();
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

package textures;

import base.engine.Engine;

import javax.script.ScriptEngine;
import java.util.HashMap;

/**
 * Created by Bailey on 5/20/2018.
 */
public class MaterialManager extends Engine{
    private HashMap<String, Material> materials = new HashMap<>();

    public MaterialManager(){
        super("MaterialManager");
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {

    }

    @Override
    public void onShutdown() {

    }

    public boolean hasMaterial(String id){
        return this.materials.containsKey(id);
    }

    public Material getMaterial(String id){
        return this.materials.get(id);
    }

    public Material put(String id, Material material){
        this.materials.put(id, material);
        return material;
    }

    public Material clone(String id, String dest){
        Material clone = new Material(this.materials.get(id));
        this.materials.put(dest, clone);
        return clone;
    }
}

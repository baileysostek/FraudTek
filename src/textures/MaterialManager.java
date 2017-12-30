/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textures;

import Base.engine.Game;
import Base.util.DynamicCollection;
import Base.util.Engine;
import graphics.Renderer;
import java.util.HashMap;
import javax.script.ScriptEngine;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class MaterialManager extends Engine{

    private HashMap<String, Material> loadedMaterials = new HashMap<String, Material>();
    
    public MaterialManager() {
        super("MaterialManager");
        
//        Material r = new Material("rock", Game.spriteBinder.loadSprite("rock").getID(), Game.spriteBinder.loadSprite("rock_normal").getID(), Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white").getID());
//        this.addMaterial(r);
//
//        this.addMaterial(new Material("brick", Game.spriteBinder.loadSprite("brick").getID(), Game.spriteBinder.loadSprite("brick_normal").getID(), Game.spriteBinder.loadSprite("brick_specular").getID(), Game.spriteBinder.loadSprite("brick_reflect").getID()));
        this.addMaterial(new Material("cobblestone", Game.spriteBinder.loadSprite("cobblestone").getID(), Game.spriteBinder.loadSprite("cobblestone_normal").getID(), Game.spriteBinder.loadSprite("cobblestone_specular").getID(), Game.spriteBinder.loadSprite("cobblestone_reflect").getID()));
        this.addMaterial(new Material("white", Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white_normal").getID(), Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white").getID()));
//        this.addMaterial(new Material("tree", Game.spriteBinder.loadSprite("tree").getID(), Game.spriteBinder.loadSprite("tree_normal").getID(), Game.spriteBinder.loadSprite("tree_specular").getID(), Game.spriteBinder.loadSprite("tree_reflect").getID()));
//        this.addMaterial(new Material("hexStone", Game.spriteBinder.loadSprite("hexStone").getID(), Game.spriteBinder.loadSprite("hexStone_normal").getID(), Game.spriteBinder.loadSprite("hexStone_specular").getID(), Game.spriteBinder.loadSprite("hexStone_reflect").getID()));
//        this.addMaterial(new Material("stone", Game.spriteBinder.loadSprite("stone").getID(), Game.spriteBinder.loadSprite("stone_normal").getID(), Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white").getID()));
//        this.addMaterial(new Material("forest", Game.spriteBinder.loadSprite("forest").getID(), Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white").getID()));
        this.addMaterial(new Material("front", Game.spriteBinder.loadSprite("front").getID(), Game.spriteBinder.loadSprite("front_normal").getID(), Game.spriteBinder.loadSprite("front_specular").getID(), Game.spriteBinder.loadSprite("front_reflect").getID()));
//        this.addMaterial(new Material("house", Game.spriteBinder.loadSprite("house").getID(), Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white").getID()));
        this.addMaterial(new Material("grass", Game.spriteBinder.loadSprite("grass2").getID(), Game.spriteBinder.loadSprite("grass_normal").getID(), Game.spriteBinder.loadSprite("grass_specular").getID(), Game.spriteBinder.loadSprite("grass_reflect").getID()));
//        this.addMaterial(new Material("treasurechest", Game.spriteBinder.loadSprite("treasurechest").getID(), Game.spriteBinder.loadSprite("treasurechest_normal").getID(), Game.spriteBinder.loadSprite("treasurechest_specular").getID(), Game.spriteBinder.loadSprite("treasurechest_reflect").getID()));
//
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

    }

    @Override
    public void onShutdown() {

    }
    
    public void addMaterial(Material material){
        this.loadedMaterials.put(material.getName(), material);
    }
    
    public Material getMaterial(String id){
        if(loadedMaterials.containsKey(id)) {
            return this.loadedMaterials.get(id);
        }else{
            return new Material("custom", Integer.parseInt(id), Game.spriteBinder.loadSprite("white_normal").getID(), Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white").getID());
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textures;

import base.engine.Game;
import base.engine.Engine;
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

        this.addMaterial(new Material("brick", Game.spriteBinder.loadSprite("brick").getID(), Game.spriteBinder.loadSprite("brick_normal").getID(), Game.spriteBinder.loadSprite("brick_specular").getID(), Game.spriteBinder.loadSprite("brick_reflect").getID()));
        this.addMaterial(new Material("cobblestone", Game.spriteBinder.loadSprite("cobblestone").getID(), Game.spriteBinder.loadSprite("cobblestone_normal").getID(), Game.spriteBinder.loadSprite("cobblestone_specular").getID(), Game.spriteBinder.loadSprite("cobblestone_reflect").getID()));
        this.addMaterial(new Material("white", Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white_normal").getID(), Game.spriteBinder.loadSprite("white").getID(), Game.spriteBinder.loadSprite("white").getID()));
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
    
    public Material getMaterial(String id, int... params){
        if(loadedMaterials.containsKey(id)) {
            return this.loadedMaterials.get(id);
        }else{
            int[] outData = {
                    Game.spriteBinder.loadSprite("white_normal").getID(),
                    Game.spriteBinder.loadSprite("white").getID(),
                    Game.spriteBinder.loadSprite("white").getID()
            };

            if(params.length > 0){
                for(int i = 0; i < params.length && i < outData.length; i++){
                    outData[i] = params[i];
                }
            }

            return new Material("custom", Integer.parseInt(id), outData[0], outData[1], outData[2]);
        }
    }
    
}

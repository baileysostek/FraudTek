/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import Base.util.DynamicCollection;
import Base.util.Engine;
import javax.script.ScriptEngine;
import org.lwjgl.opengl.GL11;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class TextureManager extends Engine{

    private DynamicCollection<Integer> textures = new DynamicCollection<Integer>() {
        @Override
        public void onAdd(Integer object) {

        }

        @Override
        public void onRemove(Integer object) {
            
        }
    };
    
    public TextureManager() {
        super("TextureManager");
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
        engine.put(this.getName(), this);
    }

    @Override
    public void onShutdown() {
        for(Integer i : this.textures.getCollection(Integer.class)){
            System.out.println("Deleting Texture:"+i);
            GL11.glDeleteTextures(i);
        }
    }
    
    public int genTexture(){
        int texture = GL11.glGenTextures();
        this.textures.add(texture);
        textures.synch();
        return texture;
    }
    
}

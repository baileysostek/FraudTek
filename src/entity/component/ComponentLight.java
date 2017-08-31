/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import Base.engine.Game;
import entity.Entity;
import graphics.Renderer;
import lighting.Light;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class ComponentLight extends Component{

    private Light light;
    
    public ComponentLight(Entity e) {
        super(EnumComponentType.LIGHT, e);
        light = new Light(e.getPosition(), new Vector3f(1, 1, 1));
    }
    
    @Override
    public void onAdd(){
        Game.lightingEngine.addLight(light);
    }
    
    @Override
    public void onRemove(){
        Game.lightingEngine.removeLight(light);
    }

    @Override
    public void tick() {
        light.setPosition(e.getPosition());
    }

    @Override
    public void render(Renderer r, StaticShader shader) {
        
    }
    
}

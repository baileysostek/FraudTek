/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import Base.engine.Game;
import entity.Attribute;
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
    private Attribute<Vector3f> lightColor;
    
    public ComponentLight(Entity e, Vector3f color) {
        super(EnumComponentType.LIGHT, e);
        light = new Light(e.getPosition(), color, new Vector3f(0.1f, 0.1f, 0.1f));
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

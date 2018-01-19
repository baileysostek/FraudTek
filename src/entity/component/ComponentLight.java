/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import base.engine.Game;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    private Attribute<Vector3f> lightColor = new Attribute<Vector3f>("lightColor", new Vector3f(1, 1, 1));
    private Attribute<Vector3f> offset = new Attribute<Vector3f>("offset", new Vector3f(0, 0, 0));

    public ComponentLight(Entity e, Vector3f color) {
        super(EnumComponentType.LIGHT, e);
        lightColor.setData(color);
        lightColor = super.addAttribute(lightColor);
        this.lightColor.setIndex(this.lightColor.getIndex()+1);
        light = new Light(e.getPosition(), lightColor.getData(), new Vector3f(0.1f, 0.1f, 0.1f));
    }

    public ComponentLight(Entity e, JsonObject data) {
        super(EnumComponentType.LIGHT, e);
        lightColor = super.addAttribute(lightColor);
        offset = super.addAttribute(offset);
        this.lightColor.setIndex(this.lightColor.getIndex()+1);
        this.offset.setIndex(this.lightColor.getIndex()+1);

        Gson gson = new Gson();
        float x = offset.getData().x();
        float y = offset.getData().y();
        float z = offset.getData().z();
        float r = lightColor.getData().x();
        float g = lightColor.getData().y();
        float b = lightColor.getData().z();


        if(data.has("x")){
            x = gson.fromJson(data.get("x"), Float.class);
        }
        if(data.has("y")){
            y = gson.fromJson(data.get("y"), Float.class);
        }
        if(data.has("z")){
            z = gson.fromJson(data.get("z"), Float.class);
        }
        offset.setData(new Vector3f(x, y, z));

        if(data.has("r")){
            x = gson.fromJson(data.get("r"), Float.class);
        }
        if(data.has("g")){
            y = gson.fromJson(data.get("g"), Float.class);
        }
        if(data.has("b")){
            z = gson.fromJson(data.get("b"), Float.class);
        }
        lightColor.setData(new Vector3f(x, y, z));

        light = new Light(new Vector3f(e.getPosition()).add(offset.getData()), lightColor.getData(), new Vector3f(0.1f, 0.1f, 0.1f));
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
        light.setColor(lightColor.getData());
        light.setPosition(new Vector3f(e.getPosition()).add(offset.getData()));
    }

    @Override
    public void render(Renderer r, StaticShader shader) {
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import base.controller.JavaController;
import base.engine.Game;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class ComponentController extends Component{

    private Attribute<Integer> index = new Attribute<Integer>("controllerIndex", 0);
    private Attribute<JavaController> controller = new Attribute<JavaController>("controller", null);

    public ComponentController(Entity e, JsonObject data) {
        super(EnumComponentType.CONTROLLER, e);

        Gson gson = new Gson();

        int controllerIndex = 0;

        if(data.has("index")){
            controllerIndex = gson.fromJson(data.get("index"), Integer.class);
        }

        if(!super.e.hasAttribute(controller.getID())){
            if(super.e.hasAttribute(index.getID())){
                index = super.addAttribute(index);
            }else{
                index.setData(controllerIndex);
                index = super.addAttribute(index);
            }
            controller.setData(Game.controllerManager.getContoller(index.getData()));
            controller = super.addAttribute(controller);
        }else{
            controller = super.addAttribute(controller);
        }
    }

    public ComponentController(Entity e, int controllerIndex) {
        super(EnumComponentType.CONTROLLER, e);
        if(!super.e.hasAttribute(controller.getID())){
            if(super.e.hasAttribute(index.getID())){
                index = super.addAttribute(index);   
            }else{
                index.setData(controllerIndex);
                index = super.addAttribute(index);
            }
            controller.setData(Game.controllerManager.getContoller(index.getData()));
            controller = super.addAttribute(controller);
        }else{
            controller = super.addAttribute(controller);
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }
    
}

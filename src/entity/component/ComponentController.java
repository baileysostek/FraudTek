/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import Base.Controller.JavaController;
import Base.engine.Game;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class ComponentController extends Component{

    private Attribute<Integer> index = new Attribute<Integer>("controllerIndex:", 0);
    private Attribute<JavaController> controller = new Attribute<JavaController>("controller:", null);
    
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

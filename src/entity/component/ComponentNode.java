/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class ComponentNode extends Component{

    private Attribute<Boolean> powered = new Attribute<Boolean>("powered", false);
    
    public ComponentNode(Entity e, int index) {
        super(EnumComponentType.NODE, e);
        powered.setIndex(index);
        powered = super.addAttribute(powered);
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }
    
}

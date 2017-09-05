/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import Base.util.DynamicCollection;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public abstract class Component {
    private final EnumComponentType type;
    protected Entity e;

    public Component(EnumComponentType type, Entity e){
        this.type = type;
        this.e = e;
    }

    public EnumComponentType getType(){
        return type;
    }
    
    public void onAdd(){
        return;
    }
    
    public void onRemove(){
        return;
    }
    
    public abstract void tick();
    public abstract void render(Renderer r, StaticShader shader);
    
    public String getSaveData(){
        return"";
    }
    
    public Attribute addAttribute(Attribute a){
        if(!e.hasAttribute(a.getID())){
                e.addAttribute(a);
                return a;
        }else{
            return e.getAttribute(a.getID());
        }
    }
}

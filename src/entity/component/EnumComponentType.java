/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

/**
 *
 * @author Bailey
 */
public enum EnumComponentType {
    MOVEMENT(Component.class),
    GRAVITY(ComponentGravity.class),
    COLLIDER(ComponentCollision.class),
    FPS_CONTROLLER(ComponentFPSController.class),
    NODE(Component.class),
    LIGHT(ComponentLight.class),
    CONTROLLER(ComponentController.class),
    ANIMATION(Component.class),
    RENDER(ComponentRender.class),
    MESH(ComponentMesh.class),
    SCRIPT(ComponentScript.class),
    INTERACT(ComponentInteract.class),
    ;

    protected Class refrence;
    EnumComponentType(Class refrence){
        this.refrence = refrence;
    }
    public Class getRefrence(){
        return this.refrence;
    }
    
}

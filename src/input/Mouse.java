/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 *
 * @author Bailey
 */
public class Mouse extends GLFWMouseButtonCallback{

    private static boolean[] mouseKeys = new boolean[16];
    
    @Override
    public void invoke(long window, int button, int action, int mods) {
//        System.out.println("Button:"+button+" action:"+action);
        if(action == 1){
            mouseKeys[button] = true;
        }else{
            mouseKeys[button] = false;
        }
    }
    
    public static boolean pressed(EnumMouseButton button){
        return mouseKeys[button.getID()];
    }
    
}

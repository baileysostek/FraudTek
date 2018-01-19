/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import org.joml.Vector3f;

/**
 *
 * @author Bailey
 */
public class DynamicCamera extends Camera{

    public DynamicCamera(){

    }

    public DynamicCamera(Vector3f pos, Vector3f rot){
        super.setPosition(pos);
        super.setRotation(rot);
    }

    @Override
    public void tick() {

    }
}

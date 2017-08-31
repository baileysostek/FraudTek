/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

/**
 *
 * @author Bailey
 */
public enum EnumMouseButton {
    LEFT(0),
    RIGHT(1),
    CENTER(2),
    UP(4),
    DOWN(3);
    
    protected int id;
    EnumMouseButton(int id){
        this.id = id;
    }
    
    public int getID(){
        return this.id;
    }
    
}

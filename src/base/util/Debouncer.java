/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base.util;

/**
 *
 * @author Bailey
 */
public class Debouncer {
    boolean lastRise = false;
    boolean lastFall = false;
    
    public Debouncer(boolean initialValue){
        this.lastRise = initialValue;
        this.lastFall = initialValue;
    }
    
    public boolean risingAction(boolean B){
        if(lastRise == false && B == true){
            lastRise = B;
            return true;
        }
        lastRise = B;
        return false;
    }
    
    public boolean fallingAction(boolean B){
        if(lastFall == true && B == false){
            lastFall = B;
            return true;
        }
        lastFall = B;
        return false;
    }
    
}

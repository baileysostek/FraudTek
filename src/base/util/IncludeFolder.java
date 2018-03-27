/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base.util;

import base.engine.Game;
import java.io.File;

/**
 *
 * @author Bailey
 */
public class IncludeFolder {
    private final String name;
    private boolean exists;
    
    public IncludeFolder(String name){
        this.name = "/"+name+"/";
    }
    
    public void generateFolder(){
        File directoryName = new File(Game.Path+this.name);
        Game.logManager.println("Looking for "+ this.name + " Folder:"+Game.Path+this.name);
        
        if(!directoryName.exists()){
            boolean result = false;
                Game.logManager.println("Cannot find "+ this.name + " folder... Creating:"+Game.Path+this.name);
            try{
                directoryName.mkdir();
                result = true;
            } 
            catch(SecurityException se){
                Game.logManager.println("Failure...");
                Game.logManager.println("File Permissions do not allow the directory:"+Game.Path+this.name+" to be created."); 
            }        
            if(result) {    
                Game.logManager.println("Success...");
            }
        }
    }
    
    public boolean exists(){
        return new File(Game.Path+this.name).exists();
    }
    
    public String getName(){
        return this.name;
    }
    
}

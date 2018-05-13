 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textures;

 import base.engine.Game;

 /**
 *
 * @author Bailey
 */
public class Material {
    private String name;

//    private int albedoID;
//    private int heightID;
//    private int nomralID;
//    private int roughnessID;
//    private int ambientID;

    private int albedoID;
    private int normalID;
    private int heightID;
    private int specularID;
    private int roughnessID; 
   
    private boolean hasTransparency = false;
    
    public Material(String name){
        this.name = name;
        this.albedoID = Game.spriteBinder.loadSprite("Materials/"+name+"/albedo").getID();
    }
    
    public int getTextureID(){
        return this.albedoID;
    }
    
    public String getName(){
        return this.name;
    }
}

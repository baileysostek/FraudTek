 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textures;

 import base.engine.Game;
 import graphics.Sprite;

 /**
 *
 * @author Bailey
 */
public class Material {
    private String name;

    private int albedoID;       //Color
    private int aoID;           //Shadow
    private int normalID;       //Normal
    private int displacementID; //pixelOffset
    private int reflectionID;   //Reflectiv
    private int roughnessID;    //gloss
    private int emissiveID;     //emissive Light color
    private int emissiveMaskID; //mask for the emissive color


     private boolean hasTransparency = false;
    
    public Material(String name){
        this.name = name;
        this.albedoID       = Game.spriteBinder.loadSprite("Materials/"+name+"/albedo").getID();
        this.aoID           = Game.spriteBinder.loadSprite("Materials/"+name+"/ao").getID();
        this.normalID       = Game.spriteBinder.loadSprite("Materials/"+name+"/normal").getID();
        this.displacementID = Game.spriteBinder.loadSprite("Materials/"+name+"/height").getID();
        this.reflectionID   = Game.spriteBinder.loadSprite("Materials/"+name+"/roughness").getID();
        this.roughnessID    = Game.spriteBinder.loadSprite("Materials/"+name+"/roughness").getID();
        this.emissiveID     = Game.spriteBinder.loadSprite("Materials/"+name+"/emissive").getID();
        this.emissiveMaskID = Game.spriteBinder.loadSprite("Materials/"+name+"/emissive_mask").getID();
    }

     public Material(Material material){
         this.name = name;
         this.albedoID       = material.getAlbedoID();
         this.aoID           = material.getAOID();
         this.normalID       = material.getNormalID();
         this.displacementID = material.getDisplacementID();
         this.reflectionID   = material.getReflectionID();
         this.roughnessID    = material.getRoughnessID();
         this.emissiveID     = material.getEmissiveID();
         this.emissiveMaskID = material.getEmissiveMaskID();
     }

    public void overrideAlbedo(Sprite sprite){
        this.albedoID = sprite.getID();
    }
    
    public int getAlbedoID(){
        return this.albedoID;
    }

    public int getAOID(){
        return this.aoID;
    }

     public int getNormalID(){
         return this.normalID;
     }

     public int getDisplacementID(){
         return this.displacementID;
     }

     public int getReflectionID(){
         return this.reflectionID;
     }

     public int getRoughnessID(){
         return this.roughnessID;
     }

     public int getEmissiveID(){
         return this.emissiveID;
     }

     public int getEmissiveMaskID(){
         return this.emissiveMaskID;
     }

     public String getName(){
        return this.name;
    }
}

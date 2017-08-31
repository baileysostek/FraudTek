 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textures;

/**
 *
 * @author Bailey
 */
public class Material {
    
    private String name;
    
    private int textureID;
    private int normalID;
    private int heightID;
    private int specularID;
    private int roughnessID; 
   
    private boolean hasTransparency = false;
    
    public Material(String name, int textureID, int normalID, int specularID, int roughnessID){
        this.name = name;
        this.textureID = textureID;
        this.normalID = normalID;
        this.specularID = specularID;
        this.roughnessID = roughnessID;
    }
    
    public void setHasTransparency(boolean transparency){
        this.hasTransparency = transparency;
    }
    
    public boolean hasTransparecny(){
        return this.hasTransparency;
    }
    
    public int getTextureID(){
        return this.textureID;
    }
    
    public int getNormalID(){
        return this.normalID;
    }
    
    public int getSpecularID(){
        return this.specularID;
    }
    
    public int getRougnessID(){
        return this.roughnessID;
    }
    
    public String getName(){
        return this.name;
    }
}

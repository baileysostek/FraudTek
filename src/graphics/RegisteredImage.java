/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Bayjose
 */
public class RegisteredImage {
    String id;
    Image image;
    boolean broken = false;
    public RegisteredImage(String id){
        this.id = id;
        try{
            ImageIcon img = new ImageIcon(this.getClass().getResource("/Images/"+id));
            image = img.getImage();
        }catch(NullPointerException e){
            System.err.println("Image not Found:/Images/"+id);
            ImageIcon img = new ImageIcon(this.getClass().getResource("/Images/fileNotFound.png"));
            image = img.getImage(); 
            broken = true;
        }
    }
    
    public Image checkKey(String key){
        if(key.equals(id)){
            return image;
        }
        return null;
    }
    
    public Image getImage(){
        return this.image;
    }
}

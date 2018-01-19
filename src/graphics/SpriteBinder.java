/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import base.engine.Game;
import base.engine.Engine;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.script.ScriptEngine;
import javax.swing.ImageIcon;

/**
 *
 * @author Bailey
 */
public class SpriteBinder extends Engine{

    HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    public SpriteBinder() {
        super("SpriteBinder");
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {
        engine.put(super.getName(), this);
    }

    @Override
    public void onShutdown() {

    }
    
    public Sprite loadSprite(String image){
        if(sprites.containsKey(image)){
            return sprites.get(image);
        }
        
        ImageIcon imgcon = new ImageIcon(Game.Path+"/res/Images/"+image+".png");
        Image img = imgcon.getImage();
        if(img.getWidth(null) <= 0 || img.getHeight(null) <= 0){
            System.err.println("Image:"+Game.Path+"/res/Images/"+image+".png"+" dose not exist.");
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        
        Pixel[] pixels = new Pixel[bimage.getWidth() * bimage.getHeight()];
        int[] alpha = new int[bimage.getWidth() * bimage.getHeight()];
        for(int j = 0; j<bimage.getHeight(); j++){
            for(int i = 0; i<bimage.getWidth(); i++){
                pixels[(i + (j * bimage.getWidth()))] = new Pixel(bimage.getRGB(i, j));
            }
        }
        
        Sprite out = new Sprite(bimage.getWidth(), bimage.getHeight(), pixels);
        sprites.put(image, out);
        return out;
    }
    
   
}


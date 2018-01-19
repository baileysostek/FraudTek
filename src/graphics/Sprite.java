/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import base.engine.Game;
import java.awt.Color;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 *
 * @author Bailey
 */
public class Sprite { 
    public final int width;
    public final int height;
    public final int textureID;
    public Pixel[] pixels;

    public Sprite(int pWidth, int pHeight, Pixel[] pixels){
        this.width = pWidth;
        this.height = pHeight;
        
        if(pixels == null){
            pixels = new Pixel[pWidth * pHeight];
            for(int i = 0; i<pixels.length; i++){
                pixels[i] = new Pixel(PixelUtils.makeColor(new int[]{0, 0, 0, 0}));
            }
        }
        this.pixels = pixels;
        
        //auto generates a texture for this sprite, this texture is automatically cleaned up on engine shutdown.
        this.textureID = Game.textureManager.genTexture();
        
        synchBuffer();
    }
    
    public Sprite(int pWidth, int pHeight, Color color){
        this.width = pWidth;
        this.height = pHeight;
        
        
        pixels = new Pixel[pWidth * pHeight];
        int Intcolor = PixelUtils.makeColor(PixelUtils.getRGBA(color));
        for(int i = 0; i<pixels.length; i++){
            pixels[i] = new Pixel(Intcolor);
        }
 
        this.textureID = Game.textureManager.genTexture();
        
        synchBuffer();
    }
    
    public int[] writeToIntArray(){
        int[] imageData = new int[width * height];
        
        for(int j = 0; j< height; j++){
            for(int i = 0; i< width; i++){
                imageData[i+(j * width)] = pixels[i+(j * width)].getColor();
            }
        }
        
        return imageData;
    }
    
    public void overlay(Sprite display){
        if(display.width == this.width && display.height == this.height){
            for(int j = 0; j< height; j++){
                for(int i = 0; i< width; i++){
                    if(display.pixels[i+(j * width)].getColor()<=16777215){
                        pixels[i+(j * width)].setColor(display.pixels[i+(j * width)].getColor());
                    }
                }
            }
            this.synchBuffer();
        }
    }

    public int getPixelColor(int x, int y){
        if(x>=0 && x<this.width){
            if(y>=0 && y<this.height){
                return this.pixels[x+(y * this.width)].getColor();
            } 
        }
        return 0;
    }
    
    public Sprite getSubImage(float x, float y, float s_width, float s_height){
        Sprite out = new Sprite((int)Math.floor(s_width), (int)Math.floor(s_height), Color.WHITE);
        for(int j = 0; j < s_height; j++){
            for(int i = 0; i < s_width; i++){
                out.pixels[(int)(i+(j * s_width))] = pixels[((int)(x+i))+((int)((((int)y) + j) * width))];
            }
        }
        return out;
    }

    public void setPixelColor(int x, int y, int color){
        if(x>=0 && x<this.width){
            if(y>=0 && y<this.height){
                this.pixels[x+(y * this.width)].setColor(color);
            } 
        }
        synchBuffer();
    }
    
    public void synchBuffer(){
        int[] pixels = writeToIntArray();
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4); //4 for RGBA, 3 for RGB
        
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); //Bind texture ID
        
        //Setup wrap mode
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        
        //Send texel data to OpenGL
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
    }

    public int getID() {
        return this.textureID;
    }
}

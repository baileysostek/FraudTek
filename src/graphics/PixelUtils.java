/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;

/**
 *
 * @author Bailey
 */
public class PixelUtils {
    
    private static Pixel[] pixelData = new Pixel[16 * 16];
    
    public PixelUtils(){
        for (int i = 0; i < pixelData.length; i++) {
            pixelData[i] = new Pixel(PixelUtils.makeColor(new int[]{0, 0, 0, 255}));
        }
    }
    
    public static int[] getRGBA(int color){
        int[] out = new int[4];
        //true here for has alpha
        Color c = new Color(color, true);
        
        int b = c.getBlue();
        int g = c.getGreen();
        int r = c.getRed();
        int a = c.getAlpha();
        
        out[0] = r;
        out[1] = g;
        out[2] = b;
        out[3] = a; 
        
        return out; 
    }
    
    public static Pixel[] getBlackPixels(){
        return pixelData.clone();
    }
    
    public static int averageColors(int color1, int color2){
        int[] col1 = getRGBA(color1);
        int[] col2 = getRGBA(color2);
        
        if(col1[0] == 0 && col1[1] == 0 && col1[2] == 0 && col1[3] == 0){
            return makeColor(col2);
        }
         
        if(col2[0] == 0 && col2[1] == 0 && col2[2] == 0 && col2[3] == 0){
            return makeColor(col1);
        }
                
        for(int i = 0; i < col1.length-1; i++){
            col1[i] = col1[i] + col2[i];
        }
        col1 = normalize(col1);
        col1[3] = Math.max(col1[3], col2[3]);
        return makeColor(col1);
    }
    
    public static int scaleColor(int color1, float scale){
        int[] col1 = getRGBA(color1);
        
        if(scale<0){
            scale = 0;
        }
        
        if(scale>1){
            scale = 1;
        }
      
        for(int i = 0; i < col1.length-1; i++){
            col1[i] = (int)(col1[i] * scale);
        }

        return makeColor(col1);
    }
    
    public static int[] getRGBA(Color c){
        int[] out = new int[4];
        
        int b = c.getBlue();
        int g = c.getGreen();
        int r = c.getRed();
        int a = c.getAlpha();
        
        out[0] = r;
        out[1] = g;
        out[2] = b;
        out[3] = a; 
        
        return out; 
    }
    
    public static int makeColor(int[] color){        
        int b = color[2] * 1;
        int g = color[1] * 256;
        int r = color[0] * 65536;
        int a = color[3] * 16777216;
        
        return r+g+b+a; 
    }
    
    public static int[] normalize(int[] color){
        float largest = 0.0f;
        int[] thisColor = color;
        for(int i = 0; i<thisColor.length; i++){
            if(thisColor[i]>largest){
                largest = thisColor[i];
            }
        }
        for(int i = 0; i<thisColor.length; i++){
            thisColor[i] = (int)((thisColor[i]/largest)*255);
        }
        return thisColor;
    }
    
    public static float grayScaleColor(int decimalColor){
        int[] rgba = getRGBA(decimalColor);
        float out = (((float)rgba[0] + (float)rgba[1] + (float)rgba[2])/3.0f)/255.0f;
        return out;
    }

}

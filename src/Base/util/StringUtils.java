/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base.util;

import Base.engine.Game;
import java.io.File;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;


/**
 *
 * @author Bayjose
 */
public class StringUtils {
    
    private static String path = "";
    private static Random r = new Random();
    private static LoadedFile lastFile = new LoadedFile("", new String[]{});
   
    public static String[] addLine(String[] base, String add){
        String[] temp = new String[base.length+1];
        for(int i=0; i<base.length; i++){
            temp[i] = base[i];
        }
        temp[base.length]=add;
        return temp;
    }

    public static String[] loadData(String path){
        LinkedList<String> data = new LinkedList<String>();
        try {
            File script = new File(path);
            Scanner in = new Scanner(script);
            do{
                data.add(in.nextLine());
            }while(in.hasNext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] outData = new String[data.size()];
        for(int i=0; i<outData.length; i++){
            outData[i] = data.get(i);
        }
        return outData;
    }

    public static void saveData(String path, String[] data){
        try {
            PrintWriter p = new PrintWriter(new File(Game.Path+path));
            for(int i = 0; i<data.length; i++){
                p.println(data[i]);
            }
            p.close();
        } catch (Exception e) {
            try{
                PrintWriter p2 = new PrintWriter(new File(Game.Path+path));
                for (int i = 0; i < data.length; i++) {
                    p2.println(data[i]);
                }
                p2.close();
            }catch(Exception e2){
//                e2.printStackTrace();
            }
        }
    }
    
    public static String getRelativePath(Class refrence){
        String absPath = "";
        try {
            absPath = refrence.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            System.out.println(absPath);
            absPath = absPath.replace("/build/classes/", "");
            //IntelliJ
            absPath = absPath.replace("/out/production/Edaxerum/", "");

            String[] split = absPath.split("/");
            String out = "";
            for(int i = 0; i < split.length; i++){
                if(split[i].contains(".jar")){
                    split[i]="";
                }
                out = out+"/"+split[i];
            }
            while(out.contains("//")){
                out = out.replaceAll("//", "/");
            }
            absPath = out;
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            absPath = "";
        }
        return absPath;
    }

    public static String removeEXEandJAR(String path){
        //replace the .exe for relative paths
        if(path.contains(".exe")||path.contains(".jar")){
            String[] outArray = path.split("/");
            for(int i = 0; i<outArray.length; i++){
                loop:{
                    if(outArray[i].contains(".exe")){
                        outArray[i]="";
                        break loop;
                    }
                    outArray[i] += "/";
                }
            }
            path = unify(outArray);
        }
        return path;
    }
    
    public static int countMatches(String data, String string) {
        int index = 0;
        for(int i=0; i<data.length(); i++){
            if((data.charAt(i)+"").equals(string)){
                index++;
            }
        }
        return index;
    }
    
    public static String removeFrontSpacing(String in){
        if(in == null){
            return "";
        }
        if(in.isEmpty()){
            return "";
        }
        String out = in.replaceAll("\t", "");
        while(out.startsWith(" ")){
            out = out.replaceFirst(" ", "");
        }
        return out+"";
    }
    
    public static String[] Encrypt(String seed, String[] data){
        Random rand = new Random(seed.hashCode());
        String[] out = new String[]{};
        for(int i = 0; i< data.length; i++){
            String tmp = "";
            for(int j = 0; j< data[i].length(); j++){
                tmp = tmp+(char)((int)data[i].charAt(j) + (rand.nextInt(1024)-512))+"";
            }
            out = StringUtils.addLine(out, tmp);
            System.out.println(tmp);
        }
        return out;
    }
    
    public static String[] Decrypt(String seed, String[] data){
        Random rand = new Random(seed.hashCode());
        String[] out = new String[]{};
        for(int i = 0; i< data.length; i++){
            String tmp = "";
            for(int j = 0; j< data[i].length(); j++){
                tmp = tmp+(char)((int)data[i].charAt(j) - (rand.nextInt(1024)-512))+"";
            }
            out = StringUtils.addLine(out, tmp);
            System.out.println(tmp);
        }
        return out;
    }
    
    public static boolean isNumber(String num){
        try{
            //try Float
            float f = Float.parseFloat(num);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
    
    public static String unify(String[] in){
        String out = "";
        for(int i = 0; i < in.length; i++){
            out = out + in[i];
        }
        return out;
    }
    
}

class LoadedFile{
    public String id;
    public String[] data;
    
    public LoadedFile(String id, String[] data){
        this.id = id;
        this.data = data;
    }
}
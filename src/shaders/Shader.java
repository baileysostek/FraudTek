package shaders;

import Base.engine.Game;
import Base.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by Bailey on 12/2/2017.
 */
public class Shader{
    JsonObject data;
    private JsonShader program;

    public Shader(String name){

        String shaderName = "";

        Gson gson = new Gson();
        data = gson.fromJson(StringUtils.unify(StringUtils.loadData(Game.Path+"/Shaders/"+name+".json")), JsonObject.class);
        if(data.has("name")){
            shaderName = gson.fromJson(data.get("name"), String.class);
        }

        //Test if files exist
        boolean isValid = true;

        File vertex = new File(Game.Path+"/Shaders/"+shaderName+"VertexShader.glsl");
        File fragment = new File(Game.Path+"/Shaders/"+shaderName+"FragmentShader.glsl");
        isValid = vertex.exists() && fragment.exists();

        System.out.println("Is Valid:"+isValid+" Data:"+data);

        if(isValid) {
            program = new JsonShader(shaderName, data);
        }else{
            //Auto Generate the file
            if(!vertex.exists()){
                StringUtils.saveData("/Shaders/"+shaderName+"VertexShader.glsl", buildVertex());
            }
            if(!fragment.exists()){
                StringUtils.saveData("/Shaders/"+shaderName+"FragmentShader.glsl", buildFragment());
            }
        }
    }

    public void start(){
        program.start();
    }

    public void stop(){
        program.stop();
    }

    public ShaderProgram getShader(){
        return program;
    }


    public String[] buildVertex(){
        //Get Correct Data level
        JsonObject vertexData = data.getAsJsonObject("vertex");
        //Put version data in the out file
        Gson gson = new Gson();
        String[] out = new String[]{"#version "+gson.fromJson(data.get("version"), String.class)};
        LinkedList<String> lines = new LinkedList<String>();
        //Link this components entity to the attribute entity.
        lines.addLast("");
        //load 'in' data
        JsonArray inData = vertexData.getAsJsonArray("in");
        for(JsonElement element : inData){
            String array = "";
            if(element.getAsJsonObject().has("array")){
                array = "["+gson.fromJson(element.getAsJsonObject().get("array"), Integer.class)+"]";
            }
            lines.addLast("in "+gson.fromJson(element.getAsJsonObject().get("type"), String.class)+" "+gson.fromJson(element.getAsJsonObject().get("name"), String.class)+array+";");
        }
        lines.addLast("");
        //load 'out' data
        JsonArray outData = vertexData.getAsJsonArray("out");
        for(JsonElement element : outData){
            String array = "";
            if(element.getAsJsonObject().has("array")){
                array = "["+gson.fromJson(element.getAsJsonObject().get("array"), Integer.class)+"]";
            }
            lines.addLast("out "+gson.fromJson(element.getAsJsonObject().get("type"), String.class)+" "+gson.fromJson(element.getAsJsonObject().get("name"), String.class)+array+";");
        }
        lines.addLast("");
        //load 'uniform' data
        JsonArray uniformData = vertexData.getAsJsonArray("uniform");
        for(JsonElement element : uniformData){
            String array = "";
            if(element.getAsJsonObject().has("array")){
                array = "["+gson.fromJson(element.getAsJsonObject().get("array"), Integer.class)+"]";
            }
            lines.addLast("uniform "+gson.fromJson(element.getAsJsonObject().get("type"), String.class)+" "+gson.fromJson(element.getAsJsonObject().get("name"), String.class)+array+";");
        }
        lines.addLast("");
        lines.addLast("void main(void){");
        lines.addLast("");
        lines.addLast("}");

        //Synch the lines to out
        int index = 0;
        for(String s : lines){
            out = StringUtils.addLine(out, lines.get(index));
            index++;
        }
        return out;
    }

    public String[] buildFragment(){
        //Get Correct Data level
        JsonObject fragmentData = data.getAsJsonObject("fragment");
        //Put version data in the out file
        Gson gson = new Gson();
        String[] out = new String[]{"#version "+gson.fromJson(data.get("version"), String.class)};
        LinkedList<String> lines = new LinkedList<String>();
        //Link this components entity to the attribute entity.
        lines.addLast("");
        //load 'in' data
        try {
            JsonArray inData = fragmentData.getAsJsonArray("in");
            for(JsonElement element : inData){
                String array = "";
                if(element.getAsJsonObject().has("array")){
                    array = "["+gson.fromJson(element.getAsJsonObject().get("array"), Integer.class)+"]";
                }
                lines.addLast("in "+gson.fromJson(element.getAsJsonObject().get("type"), String.class)+" "+gson.fromJson(element.getAsJsonObject().get("name"), String.class)+array+";");
            }
        }catch(ClassCastException e){
            JsonObject vertexData = data.getAsJsonObject("vertex");
            JsonArray outData = vertexData.getAsJsonArray("out");
            for(JsonElement element : outData){
                String array = "";
                if(element.getAsJsonObject().has("array")){
                    array = "["+gson.fromJson(element.getAsJsonObject().get("array"), Integer.class)+"]";
                }
                lines.addLast("in "+gson.fromJson(element.getAsJsonObject().get("type"), String.class)+" "+gson.fromJson(element.getAsJsonObject().get("name"), String.class)+array+";");
            }
        }
        lines.addLast("");
        //load 'out' data
        JsonArray outData = fragmentData.getAsJsonArray("out");
        for(JsonElement element : outData){
            String array = "";
            if(element.getAsJsonObject().has("array")){
                array = "["+gson.fromJson(element.getAsJsonObject().get("array"), Integer.class)+"]";
            }
            lines.addLast("out "+gson.fromJson(element.getAsJsonObject().get("type"), String.class)+" "+gson.fromJson(element.getAsJsonObject().get("name"), String.class)+array+";");
        }
        lines.addLast("");
        //load 'uniform' data
        JsonArray uniformData = fragmentData.getAsJsonArray("uniform");
        for(JsonElement element : uniformData){
            String array = "";
            if(element.getAsJsonObject().has("array")){
                array = "["+gson.fromJson(element.getAsJsonObject().get("array"), Integer.class)+"]";
            }
            lines.addLast("uniform "+gson.fromJson(element.getAsJsonObject().get("type"), String.class)+" "+gson.fromJson(element.getAsJsonObject().get("name"), String.class)+array+";");
        }
        lines.addLast("");
        lines.addLast("void main(void){");
        lines.addLast("");
        lines.addLast("}");

        //Sync the lines to out
        int index = 0;
        for(String s : lines){
            out = StringUtils.addLine(out, lines.get(index));
            index++;
        }
        return out;
    }
}

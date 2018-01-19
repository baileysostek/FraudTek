package shaders;

import ScriptingEngine.Script;
import base.engine.Game;
import base.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Attribute;
import entity.Entity;
import entity.component.ComponentMesh;
import entity.component.EnumComponentType;
import jdk.nashorn.api.scripting.ScriptUtils;
import math.Maths;
import models.RawModel;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Bailey on 12/2/2017.
 */
public class Shader{
    private JsonObject data;

    private HashMap<String, String> attributeTypes = new HashMap<>();
    private HashMap<String, Integer> uniformPointers = new HashMap<>();
    private HashMap<String, String> uniformTypes = new HashMap<>();

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private int textureCache = 0;

    private Script script;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public Shader(String name){
        //First thing that is done, the javascript file is loaded.
        try {
            script = new Script(Game.Path+"/Shaders/"+name+"/"+name+".js");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Shader files are compiled
        vertexShaderID = loadShaderFile(Game.Path+"/Shaders/"+name+"/"+name+"VertexShader.glsl", GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShaderFile(Game.Path+"/Shaders/"+name+"/"+name+"FragmentShader.glsl",GL20.GL_FRAGMENT_SHADER);

        //Program id is generated for this shader program
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);


        Gson gson = new Gson();

        //We need to bind the attributes of this specific program.
        try {
            //need to cast from script object to the expected data type
            Object[] attributes = (script.run("getAttributes").values()).toArray();
            for (int i = 0; i < attributes.length; i++){
                JsonObject data = gson.fromJson(gson.toJson(attributes[i]), JsonObject.class);
                attributeTypes.put(gson.fromJson(data.get("name"), String.class), gson.fromJson(data.get("type"), String.class));
                GL20.glBindAttribLocation(programID, i, gson.fromJson(data.get("name"), String.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        //We need to bind the uniforms of this specific program.
        try {
            //need to cast from script object to the expected data type
            Object[] uniforms = (script.run("getUniforms").values()).toArray();
            for (int i = 0; i < uniforms.length; i++){
                JsonObject data = gson.fromJson(gson.toJson(uniforms[i]), JsonObject.class);
                String uniformName = gson.fromJson(data.get("name"), String.class);
                String uniformType = gson.fromJson(data.get("type"), String.class);
                if(!data.has("array")) {
                    uniformPointers.put(uniformName, GL20.glGetUniformLocation(programID, uniformName));
                    uniformTypes.put(uniformName, uniformType);
                }else{
                    int size = gson.fromJson(data.get("array"), Integer.class);
                    for(int j = 0; j < size; j++){
                        uniformPointers.put(uniformName+"["+i+"]", GL20.glGetUniformLocation(programID, uniformName+"["+i+"]"));
                        uniformTypes.put(uniformName+"["+i+"]", uniformType);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Init the Projection Matrix
        start();
        loadData("projectionMatrix", Maths.getProjectionMatrix());
        stop();

        this.data = data;
        try {
            script.run("init");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Load the json data and test for the existance of the shader
        data = gson.fromJson(StringUtils.unify(StringUtils.loadData(Game.Path+"/Shaders/"+name+"/"+name+".json")), JsonObject.class);

        //Test if files exist
        boolean isValid = true;

        File vertex = new File(Game.Path+"/Shaders/"+name+"/"+name+"VertexShader.glsl");
        File fragment = new File(Game.Path+"/Shaders/"+name+"/"+name+"FragmentShader.glsl");
        isValid = vertex.exists() && fragment.exists();

        if(isValid) {

        }else{
            //Auto Generate the file
            if(!vertex.exists()){
                StringUtils.saveData("/Shaders/"+name+"/"+name+"VertexShader.glsl", buildVertex());
            }
            if(!fragment.exists()){
                StringUtils.saveData("/Shaders/"+name+"/"+name+"FragmentShader.glsl", buildFragment());
            }
        }
    }

    public void start(){
        GL20.glUseProgram(programID);
    }

    public void bindVAO(Entity entity){
        if(entity.hasComponent(EnumComponentType.MESH)) {
            GL30.glBindVertexArray(((ComponentMesh)(entity.getComponent(EnumComponentType.MESH))).getModel().getVaoID());
            for (int i = 0; i < attributeTypes.size(); i++) {
                GL20.glEnableVertexAttribArray(i);
            }
        }
    }

    public void render(Entity entity){
        if(entity.hasComponent(EnumComponentType.MESH)) {
            //reset cache
            textureCache = 0;
            //Load transform
            Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());

            loadData("transformationMatrix", transformationMatrix);

            //actual render
            GL11.glDrawElements(GL11.GL_TRIANGLES, ((ComponentMesh)(entity.getComponent(EnumComponentType.MESH))).getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        }
    }

    public void unBindVAO(){
        for(int i = 0; i < attributeTypes.size(); i++){
            GL20.glEnableVertexAttribArray(i);
        }
    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    public void loadData(String location, Object data){
        //Get the type
        if(uniformTypes.containsKey(location)) {
            String type = uniformTypes.get(location);
            if(type.equals("float")){
                GL20.glUniform1f(uniformPointers.get(location), (float)data);
            }
            if(type.equals("sampler2D")){
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureCache);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)(Float.parseFloat(data+"")));
                GL20.glUniform1i(uniformPointers.get(location), textureCache);
                GL33.glBindSampler(textureCache, uniformPointers.get(location));
                textureCache+=2;
            }
            if(type.equals("vec3")){
                Vector3f parsedData = (Vector3f)data;
                GL20.glUniform3f(uniformPointers.get(location), parsedData.x, parsedData.y, parsedData.z);
            }
            if(type.equals("mat4")){
                Matrix4f matrix = (Matrix4f)data;
                matrixBuffer = matrix.get(matrixBuffer);
                GL20.glUniformMatrix4fv(uniformPointers.get(location), false, matrixBuffer);
            }
        }
    }

    private static int loadShaderFile(String file, int type){
        String[] shaderData = StringUtils.loadData(file, "\n");
        String shaderDataCompressed = StringUtils.unify(shaderData);

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderDataCompressed);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID));
            System.err.println("Shader:"+file+" could not be compiled.");
            System.exit(-1);
        }
        return shaderID;
    }

    //TODO premote to shaderUtils class
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

package shaders;

import ScriptingEngine.Script;
import base.engine.Game;
import base.util.EnumErrorLevel;
import base.util.IncludeFolder;
import base.util.LogManager;
import base.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Entity;
import entity.component.ComponentMesh;
import entity.component.EnumComponentType;
import graphics.VAO;
import graphics.gui.Gui;
import math.Maths;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Created by Bailey on 12/2/2017.
 */
public class Shader{
    private HashMap<String, String> attributeTypes = new HashMap<>();
    private HashMap<String, Integer> uniformPointers = new HashMap<>();
    private HashMap<String, String> uniformTypes = new HashMap<>();
    private HashMap<String, String> uniformLocations = new HashMap<>();
    private HashMap<String, String> passAttributes = new HashMap<>();

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private int cachedVAOID = 0;

    private int renderType = GL11.GL_TRIANGLES;

    private int textureCache = 0;

    private Script script;

    private String shaderName = "";

    private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public Shader(String name){
        //Check that the directory exists
        IncludeFolder folder = new IncludeFolder("Shaders/"+name+"/");
        if(!folder.exists()){
            folder.generateFolder();
        }

        //First thing that is done, the javascript file is loaded.
        try {
            script = new Script(Game.Path+"/Shaders/"+name+"/"+name+".js");
        } catch (Exception e) {
            StringUtils.saveData("/Shaders/"+name+"/"+name+".js", ShaderUtils.buildJS());
            try {
                script = new Script(Game.Path+"/Shaders/"+name+"/"+name+".js");
            } catch (Exception e1){
                e1.printStackTrace();
            }
        }

        //Shader files are compiled
        vertexShaderID = compileShaderFile(Game.Path+"/Shaders/"+name+"/"+name+"VertexShader.glsl", GL20.GL_VERTEX_SHADER);
        fragmentShaderID = compileShaderFile(Game.Path+"/Shaders/"+name+"/"+name+"FragmentShader.glsl",GL20.GL_FRAGMENT_SHADER);

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
                String uniformLocation = gson.fromJson(data.get("location"), String.class);
                if(!data.has("array")) {
                    uniformPointers.put(uniformName, GL20.glGetUniformLocation(programID, uniformName));
                    uniformTypes.put(uniformName, uniformType);
                    uniformLocations.put(uniformName, uniformLocation);
                }else{
                    int size = gson.fromJson(data.get("array"), Integer.class);
                    for(int j = 0; j < size; j++){
                        uniformPointers.put(uniformName+"["+j+"]", GL20.glGetUniformLocation(programID, uniformName+"["+j+"]"));
                        uniformTypes.put(uniformName+"["+j+"]", uniformType);
                        uniformLocations.put(uniformName+"["+j+"]", uniformLocation);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Determine the pass attributes, for building shaders
        try {
            //need to cast from script object to the expected data type
            Object[] uniforms = (script.run("getPassAttributes").values()).toArray();
            for (int i = 0; i < uniforms.length; i++){
                JsonObject data = gson.fromJson(gson.toJson(uniforms[i]), JsonObject.class);
                String passName = gson.fromJson(data.get("name"), String.class);
                String passType = gson.fromJson(data.get("type"), String.class);
                if(!data.has("array")) {
                    passAttributes.put(passName, passType);
                }else{
                    int size = gson.fromJson(data.get("array"), Integer.class);
                    for(int j = 0; j < size; j++){
                        passAttributes.put(passName+"["+j+"]", passType);
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

        //Test if files exist
        boolean isValid = true;

        File vertex = new File(Game.Path+"/Shaders/"+name+"/"+name+"VertexShader.glsl");
        File fragment = new File(Game.Path+"/Shaders/"+name+"/"+name+"FragmentShader.glsl");
        isValid = vertex.exists() && fragment.exists();

        if(isValid) {

        }else{
            //Auto Generate the file
            if(!vertex.exists()){
                StringUtils.saveData("/Shaders/"+name+"/"+name+"VertexShader.glsl", ShaderUtils.buildVertex("400 core", attributeTypes, passAttributes, uniformTypes, uniformLocations));
            }
            if(!fragment.exists()){
                StringUtils.saveData("/Shaders/"+name+"/"+name+"FragmentShader.glsl", ShaderUtils.buildFragment("400 core", passAttributes, uniformTypes, uniformLocations));
            }
        }

        try {
            //Add properties to this script
            script.addClass(GL11.class);
            Game.scriptingEngine.IncludeFilesToScript(script);
            script.init(this);
        } catch (Exception e) {
            e.printStackTrace();
            Game.logManager.println("Exception on line", EnumErrorLevel.ERROR);
        }

        shaderName = name;

    }

    public void start(){
        GL20.glUseProgram(programID);
    }

    public void bindVAOFromEntity(Entity entity){
        if(entity.hasComponent(EnumComponentType.MESH)) {
            cachedVAOID = ((ComponentMesh)(entity.getComponent(EnumComponentType.MESH))).getModel().getVaoID();
            GL30.glBindVertexArray(cachedVAOID);
            for (int i = 0; i < attributeTypes.size(); i++) {
                GL20.glEnableVertexAttribArray(i);
            }
        }
    }

    public void bindVAO(VAO vao){
        cachedVAOID = vao.getID();
        GL30.glBindVertexArray(cachedVAOID);
        for (int i = 0; i < attributeTypes.size(); i++) {
            GL20.glEnableVertexAttribArray(i);
        }
    }

    public void bindVAOFromID(int vaoID){
        cachedVAOID = vaoID;
        GL30.glBindVertexArray(cachedVAOID);
        for (int i = 0; i < attributeTypes.size(); i++) {
            GL20.glEnableVertexAttribArray(i);
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
            GL11.glDrawElements(renderType, ((ComponentMesh)(entity.getComponent(EnumComponentType.MESH))).getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        }else{
            Game.logManager.println("Entity:"+entity+" dose not have a Mesh component.", EnumErrorLevel.WARNING);
        }
    }

    public void render(Vector3f position, int size, float x, float y, float z){
        //reset cache
        textureCache = 0;
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(position, x, y, z, 1);
        loadData("transformationMatrix", transformationMatrix);
        GL11.glDrawElements(renderType, size, GL11.GL_UNSIGNED_INT, 0);
    }

    public void render(Vector3f position, int size, float x, float y, float z, float scale){
        //reset cache
        textureCache = 0;
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(position, x, y, z, scale);
        loadData("transformationMatrix", transformationMatrix);
        GL11.glDrawElements(renderType, size, GL11.GL_UNSIGNED_INT, 0);
    }

    public void unBindVAO(){
        for(int i = 0; i < attributeTypes.size(); i++){
            GL20.glDisableVertexAttribArray(i);
        }

    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    public void loadData(String location, Object data){
        if(programID != GL20.GL_CURRENT_PROGRAM){
//            Game.logManager.println("Loading data into inactive Uniform:"+location+" in the Shader:"+shaderName, EnumErrorLevel.ERROR);
        }
        //Get the type
        if (uniformTypes.containsKey(location)) {
            String type = uniformTypes.get(location);
            if (type.equals("float")) {
                GL20.glUniform1f(uniformPointers.get(location), (float) data);
                return;
            }
            if (type.equals("sampler2D")) {
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureCache);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int) (Float.parseFloat(data + "")));
                GL20.glUniform1i(uniformPointers.get(location), textureCache);
                GL33.glBindSampler(textureCache, uniformPointers.get(location));
                textureCache += 2;
                return;
            }
            if (type.equals("vec3")) {
                Vector3f parsedData = (Vector3f) data;
                GL20.glUniform3f(uniformPointers.get(location), parsedData.x, parsedData.y, parsedData.z);
                return;
            }
            if (type.equals("vec4")) {
                Vector4f parsedData = (Vector4f) data;
                GL20.glUniform4f(uniformPointers.get(location), parsedData.x, parsedData.y, parsedData.z, parsedData.w);
                return;
            }
            if (type.equals("mat4")) {
                Matrix4f matrix = (Matrix4f) data;
                matrixBuffer = matrix.get(matrixBuffer);
                GL20.glUniformMatrix4fv(uniformPointers.get(location), false, matrixBuffer);
                return;
            }
            Game.logManager.println("Undefined Uniform Type:"+type+" @Location:"+location+" in the Shader:"+shaderName, EnumErrorLevel.ERROR);
        }
    }

    private int compileShaderFile(String file, int type){
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

    private void generateShaderFiles(){
        StringUtils.saveData("/Shaders/"+shaderName+"/"+shaderName+"VertexShader.glsl", ShaderUtils.buildVertex("400 core", attributeTypes, passAttributes, uniformTypes, uniformLocations));
        StringUtils.saveData("/Shaders/" + shaderName + "/" + shaderName + "FragmentShader.glsl", ShaderUtils.buildFragment("400 core", passAttributes, uniformTypes, uniformLocations));
    }

    //This getter allows for functions defined within a shader to be addressed through references to that shader.
    public void run(String method, Object... args){
        try {
            this.script.run(method, args, this.script);
        } catch (ScriptException e) {
            e.printStackTrace();
            Game.logManager.println(e.getMessage().replaceAll("<eval>", this.script.getFilePath()), EnumErrorLevel.ERROR);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Object var(String varName){
        return script.var(varName);
    }

    public void setRenderType(int type){
        renderType = type;
    }

}

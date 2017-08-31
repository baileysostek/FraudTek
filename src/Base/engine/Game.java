/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base.engine;

import Base.Controller.ControllerManager;
import entity.EntityModel;
import Base.util.Debouncer;
import Base.util.DynamicCollection;
import Base.util.Engine;
import Base.util.IncludeFolder;
import Base.util.OBJLoader;
import Base.util.StringUtils;
import ScriptingEngine.ScriptingEngine;
import steam.SteamManager;
import camera.CameraManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Entity;
import entity.EntityManager;
import entity.EntityPlayer;
import entity.EntityTable;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import lighting.LightingEngine;
import models.ModelLoader;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import graphics.Loader;
import graphics.Renderer;
import graphics.SpriteBinder;
import graphics.TextureManager;
import input.EnumMouseButton;
import input.Keyboard;
import input.Mouse;
import input.MousePicker;
import java.util.HashMap;
import lighting.Light;
import models.ModelManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.ovr.OVRGL;
import shaders.StaticShader;
import textures.MaterialManager;
import world.Chunk;
import world.World;

public class Game {
    
    public static int WIDTH = 1920;
    public static int HEIGHT = 1080;    
    private static String NAME = "Game";
    private static boolean FULLSCREEN = false;
    
    public static float MouseX = 0;
    public static float MouseY = 0;
    
    public static String Path = "";
    private static final Class refrence = Engine.class;
    
    // This prevents our window from crashing later on.
    private static GLFWKeyCallback keyCallback;
    private static GLFWMouseButtonCallback mouseButtonCallback;
    
    private static long window;
    public static Loader loader;
    private static StaticShader shader;
    private static Renderer renderer;

    public static CameraManager cameraManager;
    
    public static MousePicker mouse;
    
    private static Debouncer mouseClick = new Debouncer(false);
    
    private static ScriptingEngine scriptingEngine;
    
    //Engines
    private static DynamicCollection<Engine> engines = new DynamicCollection<Engine>() {
        @Override
        public void onAdd(Engine object) {

        }

        @Override
        public void onRemove(Engine object) {

        }
    };
    
    private static HashMap<String, IncludeFolder> srcFolders = new HashMap<String, IncludeFolder>();
    
    public static EntityManager entityManager;
    public static LightingEngine lightingEngine;
    private static SteamManager steamManager;
    public static SpriteBinder spriteBinder;
    public static TextureManager textureManager;
    public static ModelManager modelManager;
    public static MaterialManager materialManager;
    public static ControllerManager controllerManager;
    //VR Headsets
    
    public static Entity player;
    
    private static Entity rotate;
            
    
    public static void main(String[] args){
        init();
        run();
        shutdown();
        cleanUp();
        glfwTerminate();
    }
    
    private static void init(){
        if(!glfwInit()){
            throw new IllegalStateException("Failed to initialize GLFW");
        }
        
        //set reference
        Game.Path = StringUtils.removeEXEandJAR(StringUtils.getRelativePath(refrence));
        
        //set natives path
        try{
            addDir((Game.Path+"/natives"));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        Gson gson = new Gson();
        JsonObject launchOptions = gson.fromJson(StringUtils.unify(StringUtils.loadData(Game.Path+"/Scripting/launch.json")), JsonObject.class);
        if(launchOptions.has("width")){
            WIDTH = gson.fromJson(launchOptions.get("width"), Integer.class);
        }
        if(launchOptions.has("height")){
            HEIGHT = gson.fromJson(launchOptions.get("height"), Integer.class);
        }
        if(launchOptions.has("name")){
            NAME = gson.fromJson(launchOptions.get("name"), String.class);
        }
        if(launchOptions.has("fullscreen")){
            FULLSCREEN = gson.fromJson(launchOptions.get("fullscreen"), Boolean.class);
        }
        
        if(!FULLSCREEN){
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            window = glfwCreateWindow(WIDTH, HEIGHT, NAME, 0, 0);
        }else{
            //set to native resolutiuon
            WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
            HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
            window = glfwCreateWindow(WIDTH, HEIGHT, NAME, glfwGetPrimaryMonitor(), 0);
        }
        if(window == 0){
            throw new IllegalStateException("Failed to create Window");
        }
        
        //Center the winodw
        if(!FULLSCREEN){
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (videoMode.width() - WIDTH)/2, (videoMode.height() - HEIGHT)/2);
        }
  
        glfwSetWindowTitle(window, NAME);
        
        //Shows then selects the window
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        
        // Sets our keycallback to equal our newly created Input class()
        glfwSetKeyCallback(window, keyCallback = new Keyboard());
        
        GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback = new Mouse());
        
        //Initialize OpenGL
        GL.createCapabilities();
        
        //init folder
        System.out.println("SourceFolders");
        JsonArray jsonArray = gson.fromJson(StringUtils.unify(StringUtils.loadData(Game.Path+"/Scripting/managedFolders.json")), JsonArray.class);
        for(JsonElement element : jsonArray){
            JsonObject object = element.getAsJsonObject();
            if(object.has("name")){
                String name = gson.fromJson(object.get("name"), String.class);
                IncludeFolder src = new IncludeFolder(name);
                src.generateFolder();
                srcFolders.put(name, src);
            }
        }
        
        loader = new Loader();
        shader = new StaticShader(Path);
        renderer = new Renderer(shader);
        lightingEngine = new LightingEngine();
        engines.add(lightingEngine);
        cameraManager = new CameraManager();
        engines.add(cameraManager);
        mouse = new MousePicker(renderer.getProjectionMatrix());
        textureManager = new TextureManager();
        engines.add(textureManager);
        entityManager = new EntityManager();
        engines.add(entityManager);
        steamManager = new SteamManager();
        engines.add(steamManager);
        spriteBinder = new SpriteBinder();
        engines.add(spriteBinder);
        modelManager = new ModelManager();
        engines.add(modelManager);
        materialManager = new MaterialManager();
        engines.add(materialManager);
        controllerManager = new ControllerManager();
        engines.add(controllerManager);
        
        engines.synch();
        scriptingEngine = new ScriptingEngine();
        //Register for scripting
        for(Engine e : engines.getCollection(Engine.class)){
            e.registerForScripting(scriptingEngine.getEngine());
        }
//        scriptingEngine.add(gson.fromJson(launchOptions.get("mainScript"), String.class));
          
        
//        room = new Room("save.js");
        player = new EntityPlayer(new Vector3f(2,10,2));
        entityManager.addEntity(player);

//        entityManager.addEntity(new EntityModel(ModelLoader.generateTerrain(120, 120, 240),  "brick", new Vector3f(0,0,0), 0,0,0,1));
        Entity water = new EntityModel(ModelLoader.generateQuad(12, 12),  "tree", new Vector3f(0,0 ,0), 90,0,0,1);
        entityManager.addEntity(water);

        
//        entityManager.addEntity(new EntityModel(ModelLoader.generateCube(10, 10, 1), new Vector3f(0,6,0), 90,0,0,1));
//        entityManager.addEntity(new EntityModel(ModelLoader.generateCube(1, 10, 6), new Vector3f(5,3,0), 90,0,0,1));
//        entityManager.addEntity(new EntityModel(ModelLoader.generateCube(1, 10, 6), new Vector3f(-5,3,0), 90,0,0,1));
//        entityManager.addEntity(new EntityModel(ModelLoader.generateCube(10, 1, 6), new Vector3f(0,3,5), 90,0,0,1));
        rotate = new EntityModel(ModelLoader.generateQuad(1, 1), "brick", new Vector3f(0, 1, 0), 0, 0, 0, 1);
        entityManager.addEntity(rotate);
        
//        world = new World(new Vector3f(0, 0, 0), "height");
        
//        entityManager.addEntity(new EntityModel(ModelLoader.generateQuad(1, 1), "stone", new Vector3f(0,0,0), 90,0,0,1));
//        entityManager.addEntity(new EntityModel(ModelLoader.loadModel("gem"), Game.spriteBinder.loadSprite("brick").getID(), new Vector3f(-3,-0f,-3), 0,45,0,1));
//        door = new EntityModel(ModelLoader.generateCube(10, 1, 6), new Vector3f(0,3,-5), 90,0,0,1);
//        entityManager.addEntity(door);

//
//        int width = 160; 
//        int height = 16;
//        int deapth = 16;
//
//        for(int i = 0; i < width; i++){
//           for(int j = 0; j < height; j++){
//               for(int k = 0; k < deapth; k++){
//                   entityManager.addEntity(new EntityModel(ModelLoader.generateCube(1, 1, 1), "brick", new Vector3f(i, j, k), 0, 0, 0, 1));
//               }   
//            } 
//        }

//        lightingEngine.addLight(new Light(new Vector3f(-1, 0, -1), new Vector3f(1, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f)));
//        lightingEngine.addLight(new Light(new Vector3f(1, 0, -1), new Vector3f(0, 1, 0), new Vector3f(0.1f, 0.1f, 0.1f)));
//        lightingEngine.addLight(new Light(new Vector3f(0, 0, 1), new Vector3f(0, 0, 1), new Vector3f(0.1f, 0.1f, 0.1f)));
          lightingEngine.addLight(new Light(new Vector3f(0, 10, 0), new Vector3f(1, 1, 1)));

//        entityManager.addEntity(new EntityModel(new TexturedModel(OBJLoader.loadObjModel("mesh", loader), new Material(1)), new Vector3f(0,-8,0), 0,0,0,1));
//        
//        test = new EntityPanel(new Vector3f(0,1.2f,-2),3,3);
//        entityManager.addEntity(test);

//        build();
    }
    
    private static void run(){
        //Game Loop
        long last = System.currentTimeMillis();
        long delta = 0;
        int ticks = 0;
        
        while(!glfwWindowShouldClose(window)){
            long now = System.currentTimeMillis();
            delta+=(now-last);
            glfwPollEvents();
            tick();
            render();
            if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GL_TRUE){
                glfwSetWindowShouldClose(window, true);
            }
        }	
    }
    
    private static void tick(){
        engines.synch();
        mouse.tick();

        scriptingEngine.tick();
        
        rotate.rotate(0.0f, 0.3f, 0.0f);
        
        for(Engine e : engines.getCollection(Engine.class)){
            e.tick();
        }
        
        if(mouseClick.risingAction(Mouse.pressed(EnumMouseButton.LEFT))){
            mousePressed();
        }
        
        if(mouseClick.fallingAction(Mouse.pressed(EnumMouseButton.LEFT))){
            mouseReleased();
        }
    }
    
    private static void mousePressed(){
    
    }
    
    private static void mouseReleased(){
        
    }
    
    private static void render(){
        renderer.prepare();
        shader.start();
        lightingEngine.loadLights(shader);
        shader.loadViewMatrix(cameraManager.getCam());
//        world.render(renderer, shader);
        renderer.render(shader);
        shader.stop();
        glfwSwapBuffers(window);
    }
    
    private static void shutdown(){
        for(Engine e : engines.getCollection(Engine.class)){
            e.onShutdown();
        }
    }
    
    private static void cleanUp(){
        shader.cleanUp();
        loader.cleanUp();
    }
    
    public static long getWindowPointer(){
        return window;
    }
    
    public static void addDir(String s) throws IOException {
        try {
            // This enables the java.library.path to be modified at runtime
            // From a Sun engineer at http://forums.sun.com/thread.jspa?threadID=707176
            //
            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            String[] paths = (String[])field.get(null);
            for (int i = 0; i < paths.length; i++) {
                if (s.equals(paths[i])) {
                    return;
                }
            }
            String[] tmp = new String[paths.length+1];
            System.arraycopy(paths,0,tmp,0,paths.length);
            tmp[paths.length] = s;
            field.set(null,tmp);
            System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + s);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    } 
    
}

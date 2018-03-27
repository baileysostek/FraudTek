/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base.engine;

import base.util.LogManager;

import javax.script.ScriptEngine;

/**
 *
 * @author Bailey
 */
public abstract class Engine {

    private final String engineName;
    private static boolean initialized = false;

    private static Engine self;

    public Engine(String engine){
        engineName = engine;
        Initialize();
        initialized = true;
    }
    
    public void Initialize(){
        try{
            if(!engineName.equals("LogManager")) {
                Game.logManager.print("Initializing Engine:" + engineName + "...");
            }
            init();
            if(!engineName.equals("LogManager")){
                Game.logManager.println("Success.");
            }
        }catch(Exception e){
            e.printStackTrace();
            if(!engineName.equals("LogManager")) {
                Game.logManager.println("Failure.");
            }
        }
    }

    public boolean isInitialized(){
        return this.initialized;
    }

    public String getName(){
        return this.engineName;
    }

    public abstract void init();
    public abstract void tick();
    public abstract void registerForScripting(ScriptEngine engine);
    public abstract void onShutdown();
//    protected static Engine getSelf(){
//
//        return null;
//    }
//
//    public static Engine getInstance(){
//        if(!initialized){
//            self = getSelf();
//        }
//        return self;
//    }
}

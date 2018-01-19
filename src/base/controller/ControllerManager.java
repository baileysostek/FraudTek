/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base.controller;

import base.util.DynamicCollection;
import base.engine.Engine;
import javax.script.ScriptEngine;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * @author Bailey
 */
public class ControllerManager extends Engine{

    private DynamicCollection<JavaController> controllers;
    
    private final int maxTicks = 5*60;
        private int currentTicks = maxTicks;
    
    public ControllerManager() {
        super("Controllers");
    }

    @Override
    public void init() {
        controllers = new DynamicCollection<JavaController>() {
            @Override
            public void onAdd(JavaController object) {
                //Play noise
            }

            @Override
            public void onRemove(JavaController object) {
                //Play noise
            }
        };
        
        pollControllers();
        
        System.out.println("Connected Controllers:"+controllers.getLength());
    }

    @Override
    public void tick() {
        //every x seconds poll the controllers
        if(currentTicks>0){
            currentTicks--;
        }else{
            currentTicks = maxTicks;
            pollControllers();
        }
        
        controllers.synch();
        for(JavaController controller: controllers.getCollection(JavaController.class)){
            if(controller.isConnected()){
                //updates controller values
                controller.poll();
            }else{
                controllers.remove(controller);
            }
        }
    }

    
    public void pollControllers(){
        Controller[] ca;
        ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for (Controller ca1 : ca) {
//            System.out.println("Connected device:"+ca1.getName()+" type:"+ca1.getType());
            if (ca1.getType().equals(Controller.Type.STICK)||ca1.getType().equals(Controller.Type.GAMEPAD)) {
                loop:
                {
                    boolean canAdd = true;
                    for (JavaController controller : controllers.getCollection(JavaController.class)) {
                        if (controller.equalsController(ca1)) {
                            canAdd = false;
                            break loop;
                        }
                    }
                    if (canAdd) {
                        controllers.add(new JavaController(ca1));
                    }
                }
            }   
        }

        controllers.synch();

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {

    }

    @Override
    public void onShutdown() {

    }
    
    public JavaController getContoller(int controllerIndex){
        if(controllerIndex < this.controllers.getLength()){
            int index = 0;
            for(JavaController controller : controllers.getCollection(JavaController.class)){
                if(index == controllerIndex){
                    return controller;
                }
                index++;
            }
        }
        return null;
    }
}

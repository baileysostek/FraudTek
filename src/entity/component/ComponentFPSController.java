/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import Base.Controller.EnumButtonType;
import Base.Controller.JavaController;
import Base.engine.Game;
import camera.Camera;
import camera.FPSCamera;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import input.Keyboard;
import java.awt.event.KeyEvent;
import lighting.Light;
import math.Maths;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class ComponentFPSController extends Component{

    private Camera camera;
    private Vector3f cameraOffset = new Vector3f(0, 0.75f ,0);
    
    private Attribute<Float> walkSpeed = new Attribute<Float>("walkSpeed", 0.1f);
    private Attribute<Float> runSpeed = new Attribute<Float>("runSpeed", 0.3f);
    private Attribute<Float> jumpVelocity = new Attribute<Float>("jumpVelocity", 0.2f);
    private Attribute<Boolean> onGround = new Attribute<Boolean>("onGround", false);
    
    private Attribute<JavaController> controller = new Attribute<JavaController>("controller:", null);
    
    public ComponentFPSController(Entity e) {
        super(EnumComponentType.FPS_CONTROLLER, e);
        camera = new FPSCamera();
        walkSpeed = super.addAttribute(walkSpeed);
        runSpeed = super.addAttribute(runSpeed);
        jumpVelocity = super.addAttribute(jumpVelocity);
        onGround = super.addAttribute(onGround);
        controller = super.addAttribute(controller);
    }

    @Override
    public void onAdd(){
        Game.cameraManager.setCamera(camera);
    }
    
    @Override
    public void tick() {
        if(controller.getData()!=null){
            camera.getRotation().add(new Vector3f(controller.getData().getLeftThumbStick()).mul(2, 0, 0)).add(new Vector3f(controller.getData().getRightThumbStick()).mul(1, 1, 0));
            float travelSpeed = walkSpeed.getData();
            if(controller.getData().getLeftThumbStick().z>0){
                travelSpeed = runSpeed.getData();
            }
            if(onGround.getData()){
                Vector3f dir = Game.mouse.getCurrentRay();
                e.addAcceleration(new Vector3f(dir).mul(travelSpeed, 0, travelSpeed).mul(-controller.getData().getLeftThumbStick().y));
            }
        }else{
            camera.setPosition(Maths.newInstance(e.getPosition()).add(e.getVelocity()).add(cameraOffset).sub(new Vector3f(Game.mouse.getCurrentRay()).mul(3)));
            if(Keyboard.isKeyDown(KeyEvent.VK_W)){
                if(onGround.getData()){
                    Vector3f dir = Game.mouse.getCurrentRay();
                    e.addAcceleration(Maths.newInstance(dir).mul(walkSpeed.getData(), 0, walkSpeed.getData()).mul(4));
                }
            }
            if(Keyboard.isKeyDown(KeyEvent.VK_SPACE)){
                if(onGround.getData()){
                    e.addAcceleration(0, this.jumpVelocity.getData(), 0);
                }
            }
        }
        camera.setPosition(Maths.newInstance(e.getPosition()).add(e.getVelocity()).add(cameraOffset).sub(new Vector3f(Game.mouse.getCurrentRay()).mul(3)));
    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }
    
}

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
    private Vector3f cameraOffset = new Vector3f(0, 0.35f ,0);
    
    private Attribute<Float> walkSpeed = new Attribute<Float>("walkSpeed", 0.05f);
    private Attribute<Float> runSpeed = new Attribute<Float>("runSpeed", 0.1f);
    private Attribute<Float> jumpVelocity = new Attribute<Float>("jumpVelocity", 0.15f);
    private Attribute<Boolean> onGround = new Attribute<Boolean>("onGround", true);
    
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
            camera.getRotation().set((Maths.inverseVector(new Vector3f(controller.getData().getRightThumbStick())).mul(45, 45, 0)).add(new Vector3f(180, 45, 0)));
            float travelSpeed = walkSpeed.getData();
            if(controller.getData().getLeftThumbStick().z>0){
                travelSpeed = runSpeed.getData();
            }
            if(onGround.getData()){
                e.addAcceleration(new Vector3f(-controller.getData().getLeftThumbStick().x, 0, -controller.getData().getLeftThumbStick().y).mul(travelSpeed, 0, travelSpeed));
            }
            if(onGround.getData()) {
                if (controller.getData().getButton(EnumButtonType.A) > 0) {
                    e.addAcceleration(new Vector3f(0, jumpVelocity.getData(), 0));
                }
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
        System.out.println(onGround.getData());
        camera.setPosition(Maths.newInstance(e.getPosition()).add(e.getVelocity()).add(cameraOffset).sub(new Vector3f(Game.mouse.getCurrentRay()).mul(3 * ((1.0f - controller.getData().getButton(EnumButtonType.RIGHT_TRIGGER))/2.0f))));
    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }
    
}

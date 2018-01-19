/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import input.Keyboard;
import java.awt.event.KeyEvent;
import java.nio.DoubleBuffer;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import base.engine.Game;
import static base.engine.Game.HEIGHT;
import static base.engine.Game.WIDTH;
import base.util.Debouncer;
import graphics.Renderer;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class FPSCamera extends Camera{
    private double prevX = 0;
    private double prevY = 0;
    private Debouncer mouseRight = new Debouncer(false);

    public FPSCamera(){}

    public FPSCamera(Vector3f position, Vector3f rotation){
        this.setPosition(position);
        this.setRotation(rotation);
    }

    @Override
    public void tick() {
//        if(!mouseRight.risingAction(Mouse.pressed(EnumMouseButton.RIGHT))){
//            glfwSetCursorPos(Game.getWindowPointer(), (WIDTH/2), (HEIGHT/2));
//        }
//        if(Mouse.pressed(EnumMouseButton.RIGHT)){
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

            glfwGetCursorPos(Game.getWindowPointer(), x, y);
            x.rewind();
            y.rewind();

            double newX = x.get();
            double newY = y.get();

            Game.MouseX = (float) newX;
            Game.MouseY = (float) newY;

            double deltaX = newX - (WIDTH/2);
            double deltaY = newY - (HEIGHT/2);

            prevX = newX;
            prevY = newY;

            glfwSetCursorPos(Game.getWindowPointer(), (WIDTH/2), (HEIGHT/2));

            super.setRotation(super.getRotation().add(new Vector3f((float)(deltaX/30),(float)(deltaY/30),0)));
//        }
//
        if(Keyboard.isKeyDown(KeyEvent.VK_W)){
            this.position.add(Game.mouse.getCurrentRay().mul(speed, speed, speed));
        }
        if(Keyboard.isKeyDown(KeyEvent.VK_S)){
            this.position.add(Game.mouse.getCurrentRay().mul(-speed, -speed, -speed));
        }
        if(Keyboard.isKeyDown(KeyEvent.VK_A)){
            super.position.z-=((float)Math.cos(Math.toRadians(rotation.x-90)))*speed;
            super.position.x-=((float)Math.sin(Math.toRadians(rotation.x-90)))*speed*-1;
        }
        if(Keyboard.isKeyDown(KeyEvent.VK_D)){
            super.position.z-=((float)Math.cos(Math.toRadians(rotation.x+90)))*speed;
            super.position.x-=((float)Math.sin(Math.toRadians(rotation.x+90)))*speed*-1;
        }
        if(Keyboard.isKeyDown(KeyEvent.VK_E)){
            super.position.y+=speed;
        }
        if(Keyboard.isKeyDown(KeyEvent.VK_Q)){
            super.position.y-=speed;
        }
    }

    @Override
    public void render(Renderer renderer, StaticShader shader) {
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import Base.engine.Game;
import graphics.Renderer;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import shaders.StaticShader;

import java.nio.DoubleBuffer;

import static Base.engine.Game.HEIGHT;
import static Base.engine.Game.WIDTH;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

/**
 *
 * @author Bailey
 */
public class DynamicCamera extends Camera{

    public DynamicCamera(){

    }

    public DynamicCamera(Vector3f pos, Vector3f rot){
        super.setPosition(pos);
        super.setRotation(rot);
    }

    @Override
    public void tick() {

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

        glfwSetCursorPos(Game.getWindowPointer(), (WIDTH/2), (HEIGHT/2));
        super.setRotation(super.getRotation().add(new Vector3f((float)(deltaX/30),(float)(deltaY/30),0)));
    }

    @Override
    public void render(Renderer renderer, StaticShader shader) {

    }
}

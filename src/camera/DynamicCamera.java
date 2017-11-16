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

    }

    @Override
    public void render(Renderer renderer, StaticShader shader) {

    }
}

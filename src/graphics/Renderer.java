/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import input.Keyboard;
import java.awt.event.KeyEvent;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;


/**
 *
 * @author Bailey
 */
public class Renderer {

    public Renderer(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GL11.glClear(GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);

        if (Keyboard.isKeyDown(KeyEvent.VK_R)) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }

        if (!Keyboard.isKeyDown(KeyEvent.VK_R)) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }
}

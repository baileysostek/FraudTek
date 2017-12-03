package graphics;

import Base.engine.Game;
import camera.Camera;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

/**
 * Created by Bailey on 11/17/2017.
 */
public class FBO {
    int id;
    int textureID;
    int depthTexture;
    int depthBuffer;

    Camera view = null;

    public FBO(){
        //Check that FBO's are enabled on this system
        if(GL.getCapabilities().GL_EXT_framebuffer_object){
            id = GL30.glGenFramebuffers();
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);

            createTextureAttachment();
            createDepthTextureAttachment();
            createDeapthBufferAttachment();
        }
    }

    public void bindFrameBuffer(){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glClear(GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void unbindFrameBuffer(){
        GL11.glFlush();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    private int createTextureAttachment(){
        textureID = Game.textureManager.genTexture();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, Game.WIDTH, Game.HEIGHT, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, textureID, 0);
        return textureID;
    }

    private int createDepthTextureAttachment(){
        depthTexture = Game.textureManager.genTexture();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, Game.WIDTH, Game.HEIGHT, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, depthTexture, 0);

        return depthTexture;
    }

    private int createDeapthBufferAttachment(){
        depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, Game.WIDTH, Game.HEIGHT);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }

    public void cleanUp(){
        GL30.glDeleteFramebuffers(id);
        GL30.glDeleteRenderbuffers(depthBuffer);
    }

    public void setView(Camera cam){
        this.view = cam;
    }

    public int getFBOID(){
        return id;
    }

    public int getTextureID(){
        return textureID;
    }

    public int getDepthTexture(){
        return this.depthBuffer;
    }



}

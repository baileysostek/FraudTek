package graphics.gui;

import Base.engine.Game;
import org.joml.Matrix4f;
import shaders.ShaderProgram;

public class GuiShader extends ShaderProgram{

    private static final String VERTEX_FILE = "/Shaders/uiVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/Shaders/uiFragmentShader.glsl";

    private int location_transformationMatrix;

    public GuiShader() {
        super(Game.Path+VERTEX_FILE, Game.Path+FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }




}

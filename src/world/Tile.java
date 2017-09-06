package world;

import Base.engine.Game;
import graphics.Renderer;
import models.ModelLoader;
import org.joml.Vector3f;
import org.joml.Vector3i;
import shaders.StaticShader;

/**
 * Created by Bailey on 9/1/2017.
 */
public class Tile {

    private String materialID = "grass";
    private Vector3f position;
    private Vector3f rotation = new Vector3f(90, 0, 0);
    private String model;

    public Tile(float x, float y, float z){
        position = new Vector3f(x, y, z);
        model = ModelLoader.generateQuad(1, 1);
    }

    public void render(Renderer r, StaticShader s){
        r.render(Game.modelManager.getModel(model), Game.materialManager.getMaterial(materialID), position,rotation, 1, s);
    }

    public void setModel(String model){
        if(model.equals("cube")){
            this.model = ModelLoader.generateCube(1, 1, 1);
        }
        if(model.equals("45")){
            this.model = ModelLoader.generateQuad45(1, 1);
            this.rotation.x = 0;
        }
    }

    public void setMaterial(String material){
        materialID = material;
    }

    public Vector3f getRotation(){
        return this.rotation;
    }

    public Vector3f getPosition(){
        return this.position;
    }

    public String getModel(){
        return model;
    }

    public String getMaterialID(){
        return materialID;
    }
}

package entity.component;

import Base.engine.Game;
import entity.Entity;
import graphics.Renderer;
import models.RawModel;
import shaders.StaticShader;

/**
 * Created by Bailey on 9/1/2017.
 */
public class ComponentMesh extends Component{

    private String meshID;

    public ComponentMesh(Entity e, String meshID) {
        super(EnumComponentType.MESH, e);
        this.meshID = meshID;
    }

    public RawModel getModel(){
        return Game.modelManager.getModel(meshID);
    }

    public String getModelID(){
        return this.meshID;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }
}

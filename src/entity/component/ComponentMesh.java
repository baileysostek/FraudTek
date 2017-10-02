package entity.component;

import Base.engine.Game;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import models.ModelLoader;
import models.RawModel;
import shaders.StaticShader;

/**
 * Created by Bailey on 9/1/2017.
 */
public class ComponentMesh extends Component{

    private String meshID;

    public ComponentMesh(Entity e, JsonObject mesh) {
        super(EnumComponentType.MESH, e);
        Gson gson = new Gson();
        float x = 0;
        float y = 0;
        float z = 0;
        if(mesh.has("x")){
            x = gson.fromJson(mesh.get("x"), Float.class);
        }
        if(mesh.has("y")){
            y = gson.fromJson(mesh.get("y"), Float.class);
        }
        if(mesh.has("z")){
            z = gson.fromJson(mesh.get("z"), Float.class);
        }
        String type = "cube";
        if(mesh.has("model")){
            switch (gson.fromJson(mesh.get("model"), String.class)){
                case "quad":
                    meshID = (ModelLoader.generateQuad(x, y));
                case "cube":
                    meshID = (ModelLoader.generateCube(x, y, z));
                default:
                    String data = gson.fromJson(mesh.get("model"), String.class);
                    if(data.contains(".obj")){
                        data = data.replace(".obj", "");
                    }
                    meshID = ModelLoader.loadModel(data);
            }
        }else{
            meshID = (ModelLoader.generateCube(x, y, z));
        }

        //Link this object to an attribute
        if(mesh.has("link")){
            Attribute<ComponentMesh> link = new Attribute(gson.fromJson(mesh.get("link"), String.class), this);
            e.addAttribute(link);
        }
    }

    public ComponentMesh(Entity e, String mesh) {
        super(EnumComponentType.MESH, e);
        meshID = mesh;
    }

    public RawModel getModel(){
        return Game.modelManager.getModel(meshID);
    }

    public void setMesh(String mesh){
        this.meshID = mesh;
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

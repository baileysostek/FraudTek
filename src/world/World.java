package world;

import Base.engine.Game;
import Base.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Entity;
import entity.EntityModel;
import entity.component.ComponentCollision;
import entity.component.ComponentMesh;
import graphics.Renderer;
import models.ModelLoader;
import org.joml.Vector3f;
import org.joml.Vector3i;
import shaders.StaticShader;

import java.util.LinkedList;

/**
 * Created by Bailey on 9/1/2017.
 */
public class World {
    private final int width;
    private final int depth;
    private final int height;

    private Tile[] tiles;

    public World(String save){
        Gson gson = new Gson();
        JsonObject worldOption = gson.fromJson(StringUtils.unify(StringUtils.loadData(Game.Path+"/saves/"+save+".json")), JsonObject.class);

        this.width = gson.fromJson(worldOption.get("width"), Integer.class);
        this.height = gson.fromJson(worldOption.get("height"), Integer.class);
        this.depth = gson.fromJson(worldOption.get("depth"), Integer.class);

        tiles = new Tile[width * height * depth];
        for(int k = 0; k < height; k++){ //Levels deep from top view
            for(int j = 0; j < depth; j++){//Levels north/south
                for(int i = 0; i < width; i++){//Levels East West
                    tiles[i+(j * width) + (k * width * depth)] = null;
                }
            }
        }

        for(JsonElement element : worldOption.getAsJsonArray("world")){
            JsonObject obj = element.getAsJsonObject();
            int i = gson.fromJson(obj.get("x"), Integer.class);
            int j = gson.fromJson(obj.get("y"), Integer.class);
            int k = gson.fromJson(obj.get("z"), Integer.class);
            //Create object
            tiles[i+(j * width) + (k * width * depth)] = new Tile(i-(width/2)+0.5f, k, j-(depth/2)+0.5f);
            tiles[i+(j * width) + (k * width * depth)].setMaterial(gson.fromJson(obj.get("material"), String.class));
            tiles[i+(j * width) + (k * width * depth)].setModel(gson.fromJson(obj.get("model"), String.class));
        }

        EntityModel floor = new EntityModel(ModelLoader.generateQuad(width, depth), "white", new Vector3f(0, 0f, 0), 90, 0, 0, 1);
        floor.removeAttribute("render");
        Game.entityManager.addEntity(floor);

    }

    public World(int width, int height, int depth){
        this.width = width;
        this.height = height;
        this.depth = depth;
        tiles = new Tile[width * height * depth];
        for(int k = 0; k < height; k++){
            for(int j = 0; j < depth; j++){
                for(int i = 0; i < width; i++){
                    if(j == 0){
                        tiles[i+(j * width) + (k * width * depth)] = new Tile(((float)i)-(((float)width)/2.0f)+0.5f, j, ((float)k)-(((float)height)/2.0f)+0.5f);
                    }else{
                        tiles[i+(j * width) + (k * width * depth)] = null;
                    }
                }
            }
        }

        EntityModel floor = new EntityModel(ModelLoader.generateQuad(width, height), "white", new Vector3f(0, 0f, 0), 90, 0, 0, 1);
        floor.removeAttribute("render");
        Game.entityManager.addEntity(floor);
    }

    public void render(Renderer r, StaticShader s){
        //for now render all
        for(Tile t : tiles){
            if(t!=null) {
                if(Game.cameraManager.isTransitioning()) {
                    t.render(r, s);
                }else{
                    if (t.getPosition().z < Game.cameraManager.getCam().getPosition().z) {
                        t.render(r, s);
                        t.setMaterial("grass");
                    }
                }
            }
        }
    }

    public boolean inBounds(float x, float y, float z){
        if(x >= 0 && x < width){
            if(y >= 0 && y < height){
                if(z >= 0 && z < depth){
                    return true;
                }
            }
        }
        return false;
    }

    public LinkedList<ComponentCollision> getLocalizedCollisions(Vector3f pos){
        float x = pos.x;
        float y = (float) Math.floor(pos.y);
        float z = pos.z;

        LinkedList<ComponentCollision> out = new LinkedList<ComponentCollision>();

        Vector3f[] toCheck = new Vector3f[]{
                new Vector3f(x,y,z),//center
                new Vector3f(x-1,y,z),//left
                new Vector3f(x+1,y,z),//right
                new Vector3f(x,y-1,z),//back
                new Vector3f(x,y+1,z),//front
                new Vector3f(x,y,z-1),//bottom
                new Vector3f(x,y,z+1),//top
        };

        for(Vector3f input : toCheck){
            Vector3i index = new Vector3i(Math.round((input.x - 0.5f) + ((float)width/2.0f)), Math.round(input.y), Math.round((input.z - 0.5f) + ((float)depth/2.0f)));
            if(inBounds(index.x, index.y, index.z)){
                Tile tile = tiles[index.x + (index.z * width) + (index.y * width * depth)];
                if (tile != null) {
                    tile.setMaterial("brick");
                    EntityModel tileEntity = new EntityModel(tile.getModel(), tile.getMaterialID(), tile.getPosition(), tile.getRotation().x, tile.getRotation().y, tile.getRotation().z, 1);
                    ComponentCollision collision = new ComponentCollision(tileEntity, new ComponentMesh(tileEntity, tile.getModel()));
                    out.add(collision);
                }
            }
        }
        return out;
    }
}

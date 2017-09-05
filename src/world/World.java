package world;

import Base.engine.Game;
import entity.EntityModel;
import graphics.Renderer;
import models.ModelLoader;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 * Created by Bailey on 9/1/2017.
 */
public class World {
    private final int width;
    private final int depth;
    private final int height;

    private Tile[] tiles;

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
                t.render(r, s);
            }
        }
    }
}

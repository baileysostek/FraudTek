package world;

import graphics.Renderer;
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
                    tiles[i+(j * width) + (k * width * depth)] = new Tile(i, j, k);
                }
            }
        }

    }

    public void render(Renderer r, StaticShader s){
        //for now render all
        for(Tile t : tiles){
            t.render(r, s);
        }
    }
}

 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import Base.engine.Game;
import graphics.Renderer;
import graphics.Sprite;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class World {
    //load data from file
    int width = 16;
    int height = 16;
    private Chunk[] chunks;
    
    Vector3f origin = new Vector3f(0,0,0);
    
    public World(Vector3f origin, String file){
        this.origin = origin;
        chunks = new Chunk[width * height];
        
        Sprite s = Game.spriteBinder.loadSprite(file);
        
        int chunkWidthPX = (int)Math.floor((float)(s.width)/(float)width);
        int chunkHeightPX = (int)Math.floor((float)(s.height)/(float)height);
        
        for(int j = 0; j < height; j++){
            for(int i = 0; i < width; i++){
                chunks[i+(j * width)] = new Chunk(i, j, s.getSubImage((i * chunkWidthPX), (j * chunkHeightPX), chunkWidthPX, chunkHeightPX));
            }
        }
    }
    
    public void render(Renderer r, StaticShader s){
        for(Chunk c : chunks){
            c.render(r, s);
        }
    }
}

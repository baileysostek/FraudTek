package world;

import base.engine.Game;
import base.util.Debouncer;
import base.util.DistanceCalculator;
import base.util.DynamicCollection;
import base.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.EntityModel;
import entity.component.ComponentCollision;
import entity.component.ComponentMesh;
import graphics.Renderer;
import input.EnumMouseButton;
import input.Mouse;
import lighting.Light;
import org.joml.Vector3f;
import org.joml.Vector3i;
import shaders.StaticShader;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by Bailey on 9/1/2017.
 */
public class World {
    private final int xAxis;
    private final int yAxis;
    private final int zAxis;

    private Tile[] tiles;

    private Debouncer mouse = new Debouncer(false);
    private Debouncer right = new Debouncer(false);

    private Vector3f tangent = new Vector3f();
    private Vector3f bitangnet = new Vector3f();
    private Vector3f normal = new Vector3f();

    public World(String save){
        Gson gson = new Gson();
        JsonObject worldOption = gson.fromJson(StringUtils.unify(StringUtils.loadData(Game.Path+"/saves/"+save+".json")), JsonObject.class);

        this.xAxis = gson.fromJson(worldOption.get("width"), Integer.class);
        this.yAxis = gson.fromJson(worldOption.get("height"), Integer.class);
        this.zAxis = gson.fromJson(worldOption.get("depth"), Integer.class);

        tiles = new Tile[xAxis * yAxis * zAxis];
        for(int k = 0; k < yAxis; k++){ //Levels deep from top view
            for(int j = 0; j < zAxis; j++){//Levels north/south
                for(int i = 0; i < xAxis; i++){//Levels East West
                    tiles[i+(j * xAxis) + (k * xAxis * zAxis)] = null;
//                    if(k == 0) {
//                        System.out.println("{");
//                        System.out.println("    \"x\":" + i + ",");
//                        System.out.println("    \"y\":" + k + ",");
//                        System.out.println("    \"z\":" + j + ",");
//                        System.out.println("\"material\":\"grass\",");
//                        System.out.println("\"model\":\"flat\"");
//                        System.out.println("},");
//                    }
                }
            }
        }

        for(JsonElement element : worldOption.getAsJsonArray("world")){
            JsonObject obj = element.getAsJsonObject();
            int i = gson.fromJson(obj.get("x"), Integer.class);
            int j = gson.fromJson(obj.get("z"), Integer.class);
            int k = gson.fromJson(obj.get("y"), Integer.class);
            //Create object
            tiles[i+(j * xAxis) + (k * xAxis * zAxis)] = new Tile(i-(xAxis/2)+0.5f, k, j-(zAxis/2)+0.5f);
            tiles[i+(j * xAxis) + (k * xAxis * zAxis)].setMaterial(gson.fromJson(obj.get("material"), String.class));
            tiles[i+(j * xAxis) + (k * xAxis * zAxis)].setModel(gson.fromJson(obj.get("model"), String.class));
        }
    }

    public Tile getIntersection(Vector3f ray, Vector3f origin){
        DynamicCollection<Tile> hits = new DynamicCollection<Tile>() {
            @Override
            public void onAdd(Tile object) {

            }

            @Override
            public void onRemove(Tile object) {

            }
        };

        for(Tile tile : tiles) {
            if (tile != null) {
                ComponentCollision collision = tile.getCollision();
                Vector3f hitPosition = new Vector3f();
                if (collision.rayHitsMesh(new Vector3f(ray).mul(12), new Vector3f(origin), hitPosition)) {
                    hits.add(tile);
                }
            }
        }
        hits.synch();

        if(hits.getLength()>0) {
            Tile[] hitTiles = hits.getCollection(Tile.class);

            Arrays.sort(hitTiles, new Comparator<Tile>() {
                @Override
                public int compare(Tile tile1, Tile tile2) {
                    return (int) (DistanceCalculator.distance(origin, tile1.getPosition()) - DistanceCalculator.distance(origin, tile2.getPosition()));
                }
            });

            ComponentCollision collision = hitTiles[0].getCollision();
            Vector3f hitPosition = new Vector3f();
            if (collision.rayHitsMesh(new Vector3f(ray).mul(12), new Vector3f(origin), hitPosition)) {
                tangent     = collision.getBufferTangent();
                bitangnet   = collision.getBufferBitangent();
                normal      = collision.getBufferNormal();
            }

            return hitTiles[0];
        }

        return null;
    }

    public void render(Renderer r, StaticShader s){
        //for now render all
        for(Tile t : tiles){
            if(t!=null) {
                if(Game.cameraManager.isTransitioning()) {
                    t.render(r, s);
                }else{
//                    if (t.getPosition().z < Game.cameraManager.getCam().getPosition().z) {
                        t.render(r, s);
//                        t.setMaterial("grass");
//                    }
                }
            }
        }
    }

    public boolean inBounds(float x, float y, float z){
        if(x >= 0 && x < xAxis){
            if(y >= 0 && y < yAxis){
                if(z >= 0 && z < zAxis){
                    return true;
                }
            }
        }
        return false;
    }

    public Tile getTile(int i, int j, int k){
        if(inBounds(i, j, k)) {
            return tiles[i + (k * xAxis) + (j * xAxis * zAxis)];
        }
        return null;
    }

    public void setTile(int i, int j, int k, Tile t){
        if(inBounds(i, j, k)) {
            tiles[i + (k * xAxis) + (j * xAxis * zAxis)] = t;
        }
    }

    public void tick(){
        Tile t = getIntersection(Game.mouse.getCurrentRay(), Game.cameraManager.getCam().getPosition());
        if(t!=null){
            if(mouse.risingAction(Mouse.pressed(EnumMouseButton.LEFT))){
                Vector3f addition = new Vector3f(tangent.x, normal.x, bitangnet.x);
                System.out.println("Addition:"+addition);
                int i = (int)addition.x + (int) (t.getPosition().x + (((float)xAxis)/2.0f)-0.5f);
                int j = (int)addition.y + (int) t.getPosition().y;
                int k = (int)addition.z + (int) (t.getPosition().z + (((float)zAxis)/2.0f)-0.5f);
                if(inBounds(i, j, k)){
                    setTile(i, j, k, new Tile(((float)i)-(((float)xAxis)/2.0f)+0.5f, j, ((float)k)-(((float)zAxis)/2.0f)+0.5f));
                    getTile(i, j, k).setModel("cube");
                    getTile(i, j, k).setMaterial("cobblestone");
                }
            }
            if(right.risingAction(Mouse.pressed(EnumMouseButton.RIGHT))){
                int i = (int) t.getPosition().x;
                int j = (int) t.getPosition().y;
                int k = (int) t.getPosition().z;
                Game.lightingEngine.addLight(new Light(new Vector3f(i, j, k), new Vector3f((float)Math.random(),(float)Math.random(),(float)Math.random()), new Vector3f(0.1f, 0.1f, 0.1f)));
            }
        }
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
            Vector3i index = new Vector3i(Math.round((input.x - 0.5f) + ((float)xAxis/2.0f)), Math.round(input.y), Math.round((input.z - 0.5f) + ((float)zAxis/2.0f)));
            if(inBounds(index.x, index.y, index.z)){
                Tile tile = tiles[index.x + (index.z * xAxis) + (index.y * xAxis * zAxis)];
                if (tile != null) {
//                    tile.setMaterial("brick");
                    EntityModel tileEntity = new EntityModel(tile.getModel(), tile.getMaterialID(), tile.getPosition(), tile.getRotation().x, tile.getRotation().y, tile.getRotation().z, 1);
                    ComponentCollision collision = new ComponentCollision(tileEntity, new ComponentMesh(tileEntity, tile.getModel()));
                    out.add(collision);
                }
            }
        }
        return out;
    }

    public void save(String fileName){
        System.out.print("Saving...");
        try {
            Gson gson = new Gson();

            JsonObject outData = new JsonObject();
            outData.addProperty("width", this.xAxis);
            outData.addProperty("height", this.yAxis);
            outData.addProperty("depth", this.zAxis);

            JsonArray world = new JsonArray();
            for (Tile t : tiles) {
                if (t != null) {
                    int x = (int) (t.getPosition().x + (((float) xAxis) / 2.0f) - 0.5f);
                    int y = (int) t.getPosition().y;
                    int z = (int) (t.getPosition().z + (((float) zAxis) / 2.0f) - 0.5f);
                    JsonObject tile = new JsonObject();
                    tile.addProperty("x", x);
                    tile.addProperty("y", y);
                    tile.addProperty("z", z);
                    tile.addProperty("material", t.getMaterialID());
                    tile.addProperty("model", t.getTextualModel());
                    world.add(tile);
                }
            }

            outData.add("world", world);

            StringUtils.saveData("/saves/" + fileName + ".json", new String[]{gson.toJson(outData)});
            System.out.println("Success.");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failure.");
        }
    }

    public Vector3f getLookAxis(Vector3f in){
        Vector3f test = new Vector3f(in).normalize();
        if(Math.abs(test.x) > Math.abs(test.y) && Math.abs(test.x) > Math.abs(test.z)){
            //X is biggest
            return new Vector3f(test.x/Math.abs(test.x),0,0);
        }
        if(Math.abs(test.y) > Math.abs(test.x) && Math.abs(test.y) > Math.abs(test.z)){
            //Y is biggest
            return new Vector3f(0,test.y/Math.abs(test.y),0);
        }
        if(Math.abs(test.z) >= Math.abs(test.y) && Math.abs(test.z) >= Math.abs(test.x)){
            //Z is biggest
            return new Vector3f(0,0,test.z/Math.abs(test.z));
        }

        return new Vector3f(0,0,0);
    }
}

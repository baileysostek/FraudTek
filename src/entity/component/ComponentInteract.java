package entity.component;

import base.engine.Game;
import base.util.DistanceCalculator;
import com.google.gson.JsonObject;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import shaders.Shader;

/**
 * Created by Bailey on 9/15/2017.
 */
public class ComponentInteract extends Component{

    private Attribute<Float> distance = new Attribute<Float>("distance_to_interact", 2.0f);
    private Function callback = new Function("onIteract");

    public ComponentInteract(Entity e) {
        super(EnumComponentType.INTERACT, e);
        distance = super.addAttribute(distance);
        super.addFunction(callback);
    }

    public ComponentInteract(Entity e, JsonObject data) {
        super(EnumComponentType.INTERACT, e);
        distance = super.addAttribute(distance);
        super.addFunction(callback);
        distance.setDataFromJson(data);
    }

    @Override
    public void tick() {
        if(Game.player!=null) {
            if (DistanceCalculator.distance(Game.player.getPosition(), e.getPosition()) <= distance.getData()) {
                callback.run();
            }
        }
    }

    @Override
    public void render(Shader shader) {

    }
}

package entity.component;

import entity.Entity;
import graphics.Animation.Animation;
import graphics.Renderer;
import shaders.Shader;

/**
 * Created by Bailey on 8/31/2017.
 */
public class ComponentAnimation extends Component{
    private Animation animation;
    public ComponentAnimation(Entity e) {
        super(EnumComponentType.ANIMATION, e);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Shader shader) {

    }
}

package entity.component;

import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import shaders.StaticShader;

/**
 * Created by Bailey on 9/1/2017.
 */
public class ComponentRender extends Component{

    Attribute<Boolean> shouldRender = new Attribute<Boolean>("render", true);

    public ComponentRender(Entity e) {
        super(EnumComponentType.RENDER, e);
        shouldRender = super.addAttribute(shouldRender);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }
}

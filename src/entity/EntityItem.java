package entity;

import Base.engine.Game;
import Base.util.DistanceCalculator;
import entity.component.*;
import graphics.Renderer;
import models.ModelLoader;
import models.ModelManager;
import models.RawModel;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 * Created by Bailey on 9/18/2017.
 */
public class EntityItem extends Entity{

    private Attribute<String> name = new Attribute<>("name", "item_name");
    private Attribute<String> sprite = new Attribute<>("sprite", "");

    private String quad;

    public EntityItem(Vector3f position) {
        super(EnumEntityType.ITEM, "white", position, -45, 0, 0, 1);
        constructor();
    }

    public EntityItem(String in_name, Vector3f position) {
        super(EnumEntityType.ITEM, "white", position, -45, 0, 0, 1);
        name.setData("/Items/"+in_name);
        constructor();
    }

    private void constructor(){
        quad = ModelLoader.generateQuad(0.5f, 0.5f);
        //Attributes
        super.addAttribute(name);
        super.addAttribute(sprite);
        //Components
        ComponentMesh mesh = new ComponentMesh(this, quad);
        super.addComponent(mesh);
        super.addComponent(new ComponentCollision(this, mesh));
        super.addComponent(new ComponentGravity(this));
        //Functions
        ComponentInteract interaction = new ComponentInteract(this);
        Function leftClick = new Function("leftClick");
        Function rightClick = new Function("rightClick");
//        interaction.addFunction(leftClick);
//        interaction.addFunction(rightClick);
        super.addComponent(interaction);
        //Scripting needs to come last
        super.addComponent(new ComponentScript(this, name.getData()));
    }

    @Override
    public void render(Renderer r, StaticShader shader) {
        r.render(Game.modelManager.getModel(quad), sprite.getData(), getPosition(), new Vector3f(getRotX(), getRotY(), getRotZ()), getScale(), shader);
    }
}

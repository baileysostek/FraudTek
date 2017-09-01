package graphics.Animation;

import Base.util.Engine;
import graphics.Renderer;
import shaders.StaticShader;

import javax.script.ScriptEngine;

/**
 * Created by Bailey on 8/31/2017.
 */
public class AnimationManager extends Engine{

    private AnimationManager() {
        super("AnimationManager");
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Renderer renderer, StaticShader shader) {

    }

    @Override
    public void registerForScripting(ScriptEngine engine) {

    }

    @Override
    public void onShutdown() {

    }

//    @Override
    public Engine getInstance(){
        if(!super.isInitialized()){
            new AnimationManager();
        }
        return this;
    }
}

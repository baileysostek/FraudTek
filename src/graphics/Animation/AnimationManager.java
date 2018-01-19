package graphics.Animation;

import base.engine.Engine;
import graphics.Renderer;

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

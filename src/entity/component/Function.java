package entity.component;

import Base.engine.Game;
import ScriptingEngine.Script;

/**
 * Created by Bailey on 9/15/2017.
 */
public class Function {
    private final String name;
    private Script script;

    public Function(String name){
        this.name = name;
    }

    public void setScript(Script script){
        this.script = script;
    }

    public Script getScript(){
        return this.script;
    }

    public String getFunctionName(){
        return this.name;
    }

    public void run(){
        if(script!=null){
            Game.scriptingEngine.runFunction(script, name);
        }
    }
}

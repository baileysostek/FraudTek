package ScriptingEngine;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * Created by Bailey on 5/13/2018.
 */
public class Cast<type>{
    private ScriptObjectMirror in;

    public Cast(ScriptObjectMirror in){
        this.in = in;
    }

    public type castTo(){
        return (type)in;
    }
}

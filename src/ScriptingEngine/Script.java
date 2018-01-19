/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptingEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Bailey
 */
public class Script {

    private String filePath;
    private final ScriptEngineManager manager = new ScriptEngineManager();
    private final ScriptEngine engine = manager.getEngineByName("JavaScript");
    private final Invocable inv = (Invocable) engine;
    
    public Script(String filePath) throws ScriptException, IOException{
        this.filePath = filePath;
        if(!filePath.endsWith(".js")){
            filePath += ".js";
        }
        engine.eval(Files.newBufferedReader(Paths.get(filePath.replaceFirst("/", "")), StandardCharsets.UTF_8));
    }
    
    public String getFilePath(){
        return this.filePath;
    }
    
    public void init(Object... pars) throws ScriptException, NoSuchMethodException{
        inv.invokeFunction("init", pars);
    }
    
    public void tick() throws ScriptException, NoSuchMethodException{
        inv.invokeFunction("tick");
    }

    public void render() throws ScriptException, NoSuchMethodException{
        inv.invokeFunction("render");
    }

    public jdk.nashorn.api.scripting.ScriptObjectMirror run(String method, Object... args) throws ScriptException, NoSuchMethodException{
        return (jdk.nashorn.api.scripting.ScriptObjectMirror)inv.invokeFunction(method, args);
    }

    public void put(String name, Object object){
        engine.put(name, object);
    }

    public void addClass(Class c){
        try {
            engine.eval("var "+c.getSimpleName()+" = Java.type(\""+c.getCanonicalName()+"\");");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}

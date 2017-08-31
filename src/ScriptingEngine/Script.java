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
import javax.script.ScriptException;

/**
 *
 * @author Bailey
 */
public class Script {
    private String filePath;
    
    public Script(ScriptEngine e, String filePath) throws ScriptException, IOException{
        this.filePath = filePath;
        e.eval(Files.newBufferedReader(Paths.get(filePath.replaceFirst("/", "")), StandardCharsets.UTF_8));
    }
    
    public String getFilePath(){
        return this.filePath;
    }
    
    public void init(Invocable  inv) throws ScriptException, NoSuchMethodException{
        inv.invokeFunction("init");
    }
    
    public void tick(Invocable  inv) throws ScriptException, NoSuchMethodException{
        inv.invokeFunction("tick");
    }
}

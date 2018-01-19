package editor;

import base.engine.Game;
import base.util.Debouncer;
import base.engine.Engine;
import base.util.StringUtils;
import graphics.Renderer;

import javax.script.ScriptEngine;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Bailey on 11/27/2017.
 */
public class IntellisenseEngine extends Engine {

    private static HashMap<Class, Method[]> cache = new HashMap<Class, Method[]>();

    private final int key = KeyEvent.VK_PERIOD;
    private Debouncer intellisenseKeyDebouncer = new Debouncer(false);

    public IntellisenseEngine() {
        super("IntellisenseEngine");
    }

    public static Method[] cacheFile(Class refrence){
        LinkedList<Method> methods = new LinkedList<Method>();

        for(Method m : refrence.getDeclaredMethods()){
            methods.add(m);
        }

        Method[] out = new Method[methods.size()];

        int index = 0;
        for(Method s : methods){
            out[index] = s;
            index++;
        }

        cache.put(refrence, out);

        return out;
    }

    //This method generates and HTML web page for your project in the web folder.
    public static void generateHTML(){
        LinkedList<String> outData = new LinkedList<String>();

        HashMap<String, Object> refrences = Game.scriptingEngine.getRefrences();
        for(String s : refrences.keySet()){
            outData.add("<h1>"+s+"</h1>");
            for(Method m: refrences.get(s).getClass().getMethods()){
                String MethodData = m.getName()+"(";
                for(Parameter p : m.getParameters()){
                    MethodData+= p.getType().getSimpleName()+" "+p.getName()+",";
                }
                if(MethodData.endsWith(",")){
                    MethodData = MethodData.substring(0, MethodData.length()-1);
                }
                outData.add(MethodData+")<br>");
            }
            outData.add("");
        }

        for(Class c: cache.keySet()){
            outData.add("<h1>"+c.getSimpleName()+"</h1>");
            for(Method m: cache.get(c)){
                String MethodData = m.getName()+"(";
                for(Parameter p : m.getParameters()){
                    MethodData+= p.getType().getSimpleName()+" "+p.getName()+",";
                }
                if(MethodData.endsWith(",")){
                    MethodData = MethodData.substring(0, MethodData.length()-1);
                }
                outData.add(MethodData+")<br>");
            }
            outData.add("");
        }

        String[] out = new String[outData.size()];
        for(int i = 0; i < outData.size(); i++){
            out[i] = outData.get(i);
        }

        StringUtils.saveData(Game.getFolder("Documentation").getName()+"/index.html", out);
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
}

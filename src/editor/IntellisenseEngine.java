package editor;

import Base.util.Debouncer;
import Base.util.Engine;
import graphics.Renderer;
import input.Keyboard;
import shaders.StaticShader;

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
            System.out.println(refrence.getCanonicalName()+"."+out[index]);
            index++;
        }

        return out;
    }

    private static void registerClass(Class name){
        cache.put(name, cacheFile(name));
    }


    @Override
    public void init() {

    }

    @Override
    public void tick() {
        if(intellisenseKeyDebouncer.risingAction(Keyboard.isKeyDown(key))){
            System.out.println("Test");
        }
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
}

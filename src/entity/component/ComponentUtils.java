package entity.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Entity;

import java.lang.reflect.Constructor;

/**
 * Created by Bailey on 9/22/2017.
 */
public class ComponentUtils {

    public static Component buildComponent(Entity entity, String name, JsonObject json){
        for(EnumComponentType value : EnumComponentType.values()){
            if(value.getRefrence().getSimpleName().contains(name)){
                try {
                    Constructor<?> ctor = value.getRefrence().getConstructor(Entity.class, JsonObject.class);
                    Object object = ctor.newInstance(new Object[]{entity, json});
                    return (Component)object;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

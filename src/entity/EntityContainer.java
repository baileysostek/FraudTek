package entity;

import entity.component.ComponentMesh;
import entity.component.EnumComponentType;
import models.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Bailey on 5/20/2018.
 */
public class EntityContainer{

    private ArrayList<Entity> entities = new ArrayList<>();
    private Comparator comparator;

    public EntityContainer(Comparator comparator){
        this.comparator = comparator;
    }


    public int getLength(){
        return this.entities.size();
    }

    public Entity push(Entity entity){
        this.entities.add(entity);
        return this.entities.get(this.entities.size()-1);
    }

    public void sort(){
        Collections.sort(entities, comparator);
    }

    public Entity get(int index){
        return this.entities.get(index);
    }
}

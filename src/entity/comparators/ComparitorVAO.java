package entity.comparators;

import entity.Entity;
import entity.component.ComponentMesh;
import entity.component.EnumComponentType;
import models.Model;

import java.util.Comparator;

/**
 * Created by Bailey on 5/20/2018.
 */
public class ComparitorVAO implements Comparator<Entity>{
    @Override
    public int compare(Entity o1, Entity o2) {
        Model model1 = ((ComponentMesh)o1.getComponent(EnumComponentType.MESH)).getModel();
        Model model2 = ((ComponentMesh)o2.getComponent(EnumComponentType.MESH)).getModel();
        return model1.getVaoID() - model2.getVaoID();
    }
}

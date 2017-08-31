/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import Base.engine.Game;
import Base.util.DistanceCalculator;
import Base.util.DynamicCollection;
import com.google.gson.JsonObject;
import entity.component.Component;
import org.joml.Vector3f;
import graphics.Renderer;
import graphics.Sprite;
import math.Maths;
import models.ModelLoader;
import org.joml.Intersectionf;
import org.joml.Matrix4f;
import shaders.StaticShader;
import textures.Material;

/**
 *
 * @author Bailey
 */
public abstract class Entity {
    private String model;
    private Vector3f position;
    private Vector3f velocity = new Vector3f(0,0,0);;
    private Vector3f acceleration = new Vector3f(0,0,0);
    private float rotX, rotY, rotZ;
    private float scale;
    private EnumEntityType type;
    
    //Linked data
    private Entity linked = null;
    private Vector3f offset = new Vector3f(0,0,0);
    private Vector3f offset_rot = new Vector3f(0,0,0);
    
    private String id;
    
    private String materialID = "";
    
    protected DynamicCollection<Attribute> attributes = new DynamicCollection<Attribute>() {
        @Override
        public void onAdd(Attribute object) {

        }

        @Override
        public void onRemove(Attribute object) {

        }
    };
    
    protected DynamicCollection<Component> components;
    
    public Entity(EnumEntityType type, String model, String material, Vector3f position, float rotX, float rotY, float rotZ, float scale){
        this.type = type;
        this.model = model;
        this.materialID = material;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
        this.id = "id:"+Math.random();

        components  = new DynamicCollection<Component>() {
            @Override
            public void onAdd(Component object) {
                object.onAdd();
            }

            @Override
            public void onRemove(Component object) {
                object.onRemove();
            }
        };
    }
    
    public void tick(){
        //Synches components and attributes, soon to be observer
        attributes.synch();
        components.synch();
         
        for(Component c : components.getCollection(Component.class)){
            c.tick();
        }
        
        //Acceleration, Velocity, Position
        this.velocity.add(this.acceleration);
        this.position.add(this.velocity);

        
        if(linked!=null){
            this.setRotX(linked.getRotX()+offset_rot.x());
            this.setRotY(linked.getRotY()+offset_rot.y());
            this.setRotZ(linked.getRotZ()+offset_rot.z());

            Matrix4f projections = Maths.createTransformationMatrix(new Vector3f(0,0,0), rotX, rotY, rotZ, scale);
            Vector3f translation = new Vector3f(linked.getPosition());
            Vector3f p = new Vector3f(offset).mul(2/scale);
            projections.transformDirection(p);
            p.add(translation);
            
            this.setPosition(p);
        }
        
        update();
        
        //reset acceleration
        this.acceleration.x = 0;
        this.acceleration.y = 0;
        this.acceleration.z = 0;
    }
    
    public void link(Entity link){
        this.linked = link;
    }
    
    public void setOffsetToLinked(Vector3f offset){
        this.offset = offset;
    }
    
    public abstract void update();
    public abstract void render(Renderer r, StaticShader shader);
    
    public void onAdded(){
        return;
    }
    
    public void onRemoved(){
        return;
    }
    
    public void setID(String id){
        this.id = id;
    }
    
    public String getID(){
        return this.id;
    }
    
    
    public void translate(float dx, float dy, float dz){
        this.position.x+=dx;
        this.position.y+=dy;
        this.position.z+=dz;
    }
    
    public void rotate(float dx, float dy, float dz){
        this.rotX+=dx;
        this.rotY+=dy;
        this.rotZ+=dz;
    }
    
    public String getModelID() {
        return model;
    }

    public void setModel(String id) {
        this.model = id;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    
    public void setMaterial(String materialID){
        this.materialID = materialID;
    }
    
    public Material getMaterial(){
        return Game.materialManager.getMaterial(this.materialID);
    }
    
    public boolean collides(Entity other){
       Vector3f[] box2 = other.getAABB();
       Vector3f[] box = this.getAABB();
       
       
       Vector3f[] bigger = new Vector3f[]{};
       Vector3f[] smaller = new Vector3f[]{};
       boolean box2IsSmaller = false;
       
       if(DistanceCalculator.distance(box2[0], box2[1])<DistanceCalculator.distance(box[0], box[1])){
           smaller = box2;
           bigger = box;
           box2IsSmaller = true;
       }else{
           smaller = box;
           bigger = box2;
       }
       
       Vector3f[] tests = new Vector3f[]{
           new Vector3f(smaller[0].x, smaller[0].y, smaller[0].z),
           new Vector3f(smaller[1].x, smaller[0].y, smaller[0].z),
           new Vector3f(smaller[1].x, smaller[1].y, smaller[0].z),
           new Vector3f(smaller[0].x, smaller[1].y, smaller[0].z),
           new Vector3f(smaller[0].x, smaller[0].y, smaller[1].z),
           new Vector3f(smaller[1].x, smaller[0].y, smaller[1].z),
           new Vector3f(smaller[1].x, smaller[1].y, smaller[1].z),
           new Vector3f(smaller[0].x, smaller[1].y, smaller[1].z),
           new Vector3f(0,0,0)//[8] This one is overriden in the next check, it is the center point of either this, or other.
       };
       
       if(box2IsSmaller){
           tests[8] = Maths.newInstance(other.offset).add(other.position);
       }else{
           tests[8] = Maths.newInstance(this.offset).add(this.position);
       }
       
       //first check if the origin point is inside the box.
       for(Vector3f point : tests){
            if(box2IsSmaller){
                if(Maths.pointInside(Maths.newInstance(point).add(other.offset).add(other.position), box, this.getPosition())){
                    return true;
                }
            }else{
                if(Maths.pointInside(Maths.newInstance(point).add(this.position).add(this.offset), box2, other.getPosition())){
                    return true;
                }
            }
       }
        return false;
    }
    
    public float collides(Vector3f r, Vector3f origin){
        //https://gamedev.stackexchange.com/questions/18436/most-efficient-aabb-vs-ray-collision-algorithms
        Vector3f[] box = this.getAABB();
        Vector3f lb = box[0].add(this.getPosition());
        Vector3f rt = box[1].add(this.getPosition());
        float t = 0;
        Vector3f dirfrac = new Vector3f(0,0,0);
        // r.dir is unit direction vector of ray
        dirfrac.x = 1.0f / r.x;
        dirfrac.y = 1.0f / r.y;
        dirfrac.z = 1.0f / r.z;
        // lb is the corner of AABB with minimal coordinates - left bottom, rt is maximal corner
        // r.org is origin of ray
        float t1 = (lb.x - origin.x)*dirfrac.x;
        float t2 = (rt.x - origin.x)*dirfrac.x;
        float t3 = (lb.y - origin.y)*dirfrac.y;
        float t4 = (rt.y - origin.y)*dirfrac.y;
        float t5 = (lb.z - origin.z)*dirfrac.z;
        float t6 = (rt.z - origin.z)*dirfrac.z;

        float tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        float tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        // if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        if (tmax < 0)
        {
            t = tmax;
            return Float.NEGATIVE_INFINITY;
        }

        // if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax)
        {
            t = tmax;
            return Float.NEGATIVE_INFINITY;
        }

        t = tmin;
        return t;
    }
    
    public boolean rayHitsMesh(Vector3f ray, Vector3f origin, Vector3f pos){
        float[] positions = Game.modelManager.getModel(model).getPositions();
        int[] indicies = Game.modelManager.getModel(model).getIndicies();
        
        Matrix4f projections = Maths.createTransformationMatrix(new Vector3f(0,0,0), rotX, rotY, rotZ, scale);
        
        Vector3f translation = new Vector3f(this.position.x, this.position.y, this.position.z);
        
        //3*3 = 3 dirs * 3 verticies per triangle
        for(int i = 0; i < indicies.length/3; i++){
            loop:{
                //For each verticie in the mesh, Get 3 points which define the plane
                Vector3f p1 = new Vector3f(positions[(indicies[(i * 3)+0] * 3) + 0], positions[(indicies[(i * 3)+0] * 3) + 1], positions[(indicies[(i * 3)+0] * 3) + 2]);
                Vector3f p2 = new Vector3f(positions[(indicies[(i * 3)+1] * 3) + 0], positions[(indicies[(i * 3)+1] * 3) + 1], positions[(indicies[(i * 3)+1] * 3) + 2]);
                Vector3f p3 = new Vector3f(positions[(indicies[(i * 3)+2] * 3) + 0], positions[(indicies[(i * 3)+2] * 3) + 1], positions[(indicies[(i * 3)+2] * 3) + 2]);
                //Use matrix maths to rotate our plane in 3D space
                projections.transformDirection(p1);
                projections.transformDirection(p2);
                projections.transformDirection(p3);
                //Translate the plane in 3d Space
                p1.add(translation);
                p2.add(translation);
                p3.add(translation);

                if(Intersectionf.intersectLineSegmentTriangle(origin, new Vector3f(origin).add(ray), p1, p2, p3, 0.000001f, pos)){
                    return true;
                }
                
            }
        }
        return false;
    } 
    
    public Vector3f[] getAABB(){
        Vector3f min = new Vector3f(Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE);
        Vector3f max = new Vector3f(Float.MIN_VALUE,Float.MIN_VALUE,Float.MIN_VALUE);
        
        float[] positions = Game.modelManager.getModel(model).getPositions();
        
        Matrix4f projections = Maths.createTransformationMatrix(new Vector3f(0,0,0), rotX, rotY, rotZ, scale);
        
        for(int i = 0; i < positions.length/3; i++){
            Vector3f vertex = new Vector3f(positions[i*3 + 0], positions[i*3 + 1], positions[i*3 + 2]);
                    
            projections.transformDirection(vertex);
            
            if(vertex.x()>max.x()){
                max.x = vertex.x();
            }
            if(vertex.y()>max.y()){
                max.y = vertex.y();
            }
            if(vertex.z()>max.z()){
                max.z = vertex.z();
            }
            if(vertex.x()<min.x()){
                min.x = vertex.x();
            }
            if(vertex.y()<min.y()){
                min.y = vertex.y();
            }
            if(vertex.z()<min.z()){
                min.z = vertex.z();
            }
        }
        
        Vector3f[] out = new Vector3f[]{min, max};
        
        return out;
    }
    
    public boolean hasAttribute(String id){
        for(Attribute a: this.attributes.getCollection(Attribute.class)){
            if(a.getID().equals(id)){
                return true;
            }
        }
        return false;
    }
    
    public Attribute getAttribute(String id){
        for(Attribute a: this.attributes.getCollection(Attribute.class)){
            if(a.getID().equals(id)){
                return a;
            }
        }
        return null;
    }
    
    public void populateAttributes(JsonObject data){
        
    }
    
    public void addAttribute(Attribute attribute){
        this.attributes.add(attribute);
        attributes.synch();
    }
    
    public void addComponent(Component component){
        this.components.add(component);
        this.components.synch();
    }
    
    public void addAcceleration(float x, float y, float z){
        this.acceleration.x += x;
        this.acceleration.y += y;
        this.acceleration.z += z;
    }
    
    public void addAcceleration(Vector3f acceleration){
        this.acceleration.x += acceleration.x();
        this.acceleration.y += acceleration.y();
        this.acceleration.z += acceleration.z();
    }
        
    public void setAcceleration(Vector3f acceleration){
        this.acceleration = acceleration;
    }
    
    public void setAcceleration(float x, float y, float z){
        this.acceleration.x = x;
        this.acceleration.y = y;
        this.acceleration.z = z;
    }
    
    public Vector3f getAcceleration(){
        return this.acceleration;
    }
    
    public void setVelocity(Vector3f velocity){
        this.velocity = velocity;
    }
    
    public void setVelocity(float x, float y, float z){
        this.velocity.x = x;
        this.velocity.y = y;
        this.velocity.z = z;
    }
    
    public Vector3f getVelocity(){
        return this.velocity;
    }
    
    public float getHeight(){
        Vector3f[] size = this.getAABB();
        return size[1].y - size[0].y;
    }
    
    public float getWidth(){
        Vector3f[] size = this.getAABB();
        return size[1].x - size[0].x;
    }
    
    public float getDepth(){
        Vector3f[] size = this.getAABB();
        return size[1].z - size[0].z;
    }

    public Vector3f getOffsetToLinked() {
        return this.offset;
    }
}

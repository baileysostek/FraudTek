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
import entity.component.EnumComponentType;
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
    private int textureID = 0;
    
    protected DynamicCollection<Attribute> attributes = new DynamicCollection<Attribute>() {
        @Override
        public void onAdd(Attribute object) {

        }

        @Override
        public void onRemove(Attribute object) {

        }
    };
    
    protected DynamicCollection<Component> components;
    
    public Entity(EnumEntityType type, String material, Vector3f position, float rotX, float rotY, float rotZ, float scale){
        this.type = type;
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

    public void setTexture(int textureID){
        this.materialID = "null";
        this.textureID = textureID;
    }
    
    public Material getMaterial(){
        return Game.materialManager.getMaterial(this.materialID);
    }

    public boolean hasMaterial(){
        return Game.materialManager.getMaterial(this.materialID)!=null;
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
    
    public void addAttribute(Attribute attribute){
        this.attributes.add(attribute);
        attributes.synch();
    }

    public void removeAttribute(String id){
        if(this.hasAttribute(id)){
            this.attributes.remove(getAttribute(id));
            this.attributes.synch();
        }
    }
    
    public void addComponent(Component component){
        this.components.add(component);
        this.components.synch();
    }

    public boolean hasComponent(EnumComponentType type){
        for(Component c : components.getCollection(Component.class)){
            if(c.getType().equals(type)){
                return true;
            }
        }
        return false;
    }

    public Component getComponent(EnumComponentType type){
        for(Component c : components.getCollection(Component.class)){
            if(c.getType().equals(type)){
                return c;
            }
        }
        return null;
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

    public Vector3f getOffsetToLinked() {
        return this.offset;
    }

    public int getTextureID() {
        return textureID;
    }
}

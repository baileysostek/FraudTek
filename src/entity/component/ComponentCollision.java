/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.component;

import Base.engine.Game;
import Base.util.Callback;
import Base.util.DistanceCalculator;
import entity.Attribute;
import entity.Entity;
import graphics.Renderer;
import math.Maths;
import models.RawModel;
import org.joml.Intersectionf;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import shaders.StaticShader;

/**
 *
 * @author Bailey
 */
public class ComponentCollision extends Component{

    private ComponentMesh mesh;
    private Attribute<Callback> callbackAttribute = new Attribute<>("callback", null);
    private Attribute<Boolean> onGround = new Attribute<Boolean>("onGround", false);

    public ComponentCollision(Entity e, ComponentMesh mesh) {
        super(EnumComponentType.COLLIDER, e);
        this.onGround = super.addAttribute(onGround);
        this.callbackAttribute = super.addAttribute(callbackAttribute);
        this.callbackAttribute.setIndex(this.callbackAttribute.getIndex()+1);
        this.mesh = mesh;
    }

    public void setCallback(Callback callback){
        this.callbackAttribute.setData(callback);
    }
    
    @Override
    public void tick() {

    }

    @Override
    public void render(Renderer r, StaticShader shader) {

    }

    public boolean collides(Entity other){
        if(!other.hasComponent(EnumComponentType.COLLIDER)){
            return false;
        }
        Vector3f[] box2 = ((ComponentCollision)(other.getComponent(EnumComponentType.COLLIDER))).getAABB();
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
            tests[8] = Maths.newInstance(other.getOffsetToLinked()).add(other.getPosition());
        }else{
            tests[8] = Maths.newInstance(e.getOffsetToLinked()).add(e.getPosition());
        }

        //first check if the origin point is inside the box.
        for(Vector3f point : tests){
            if(box2IsSmaller){
                if(Maths.pointInside(Maths.newInstance(point).add(other.getOffsetToLinked()).add(other.getPosition()), box, e.getPosition())){
                    return true;
                }
            }else{
                if(Maths.pointInside(Maths.newInstance(point).add(e.getPosition()).add(e.getOffsetToLinked()), box2, other.getPosition())){
                    return true;
                }
            }
        }
        return false;
    }

    public float collides(Vector3f r, Vector3f origin){
        //https://gamedev.stackexchange.com/questions/18436/most-efficient-aabb-vs-ray-collision-algorithms
        Vector3f[] box = this.getAABB();
        Vector3f lb = box[0].add(e.getPosition());
        Vector3f rt = box[1].add(e.getPosition());
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
        float[] positions = Game.modelManager.getModel(mesh.getModelID()).getPositions();
        int[] indicies = Game.modelManager.getModel(mesh.getModelID()).getIndicies();

        Matrix4f projections = Maths.createTransformationMatrix(new Vector3f(0,0,0), e.getRotX(), e.getRotY(), e.getRotZ(), e.getScale());

        Vector3f translation = new Vector3f(e.getPosition().x, e.getPosition().y, e.getPosition().z);

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

        float[] positions = Game.modelManager.getModel(mesh.getModelID()).getPositions();

        Matrix4f projections = Maths.createTransformationMatrix(new Vector3f(0,0,0), e.getRotX(), e.getRotY(), e.getRotZ(), e.getScale());

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
    
}

package particles;

import base.engine.Engine;
import base.engine.Game;
import camera.CameraManager;
import entity.Entity;
import entity.EntityContainer;
import entity.comparators.ComparitorVAO;
import math.Maths;
import models.Model;
import models.ModelLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import shaders.Shader;

import javax.script.ScriptEngine;

/**
 * Created by Bailey on 5/20/2018.
 */
public class ParticleEngine extends Engine{

    private Shader shader;
    private EntityContainer particleEmitters;
    private Model quad;

    public ParticleEngine() {
        super("ParticleEngine");
    }

    @Override
    public void init() {

    }

    public void postInit(){
        shader = new Shader("particle");
        particleEmitters = new EntityContainer(new ComparitorVAO());
//        quad = Game.modelManager.getModel(ModelLoader.loadModel("sphere4"));
        quad = Game.modelManager.getModel(ModelLoader.loadModel("quad2"));
    }

    @Override
    public void tick() {
        for(int i = 0; i < particleEmitters.getLength(); i++){
            particleEmitters.get(i).tick();
        }
    }

    public void render() {
        shader.start();
        shader.loadData("viewMatrix", Maths.createViewMatrix(Game.cameraManager.getCam()));
        shader.loadData("cameraRot", Maths.getCameraRot(Game.cameraManager.getCam()));
        shader.run("loadLights", Game.lightingEngine.getLights());
        shader.bindVAOFromID(quad.getVaoID());
        for(int i = 0; i < particleEmitters.getLength(); i++){
            EntityParticleEmitter entity = (EntityParticleEmitter)particleEmitters.get(i);
            shader.loadData("albedoMap", entity.getSprite().getID());
            float dx = (1.0f / entity.columns);
            float dy = (1.0f / entity.rows);
            shader.loadData("textureScale", new Vector2f(dx, dy));
            for(Particle p : entity.getParticles()){
                Vector3f offset = new Vector3f(90,0,0);
                shader.loadData("textureIndex", new Vector2f((int)(entity.columns * (1.0f - p.getRemainingLifetime())), 0));
                shader.loadData("rotationMatrix", Maths.getEntityRotWithOffset(entity, offset));
                shader.render(p.position, quad.getVertexCount(), offset.x(), offset.y(), offset.z(), (1.0f / 32.0f) * Math.max(entity.sprite.width / entity.columns, entity.sprite.height / entity.rows));
            }
        }
        shader.unBindVAO();
        shader.stop();
    }

    public Entity push(EntityParticleEmitter particleEmitter){
        return add(particleEmitter);
    }

    public Entity add(EntityParticleEmitter particleEmitter){
        return this.particleEmitters.push(this.particleEmitters.push(particleEmitter));
    }

    @Override
    public void registerForScripting(ScriptEngine engine) {

    }

    @Override
    public void onShutdown() {

    }
}

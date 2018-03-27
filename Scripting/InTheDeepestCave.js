/**
 *
 * Created by Bailey on 2/24/2018.
 */

var shader;
var entities = [];

var light;

function init(){
    shader = new Shader("dynamic_lighting");
    CameraManager.setCamera(new FPSCamera());

    light = LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));

    entities.push(new EntityModel(ModelLoader.generateQuad(3, 3), "white", new Vector3f(0, 0, 0), 0, 0, 0, 1));
    entities.push(new EntityModel(ModelLoader.generateQuad(3, 3), "white", new Vector3f(1, 0, 0), 0, 0, 0, 1));
    entities.push(new EntityModel(ModelLoader.generateQuad(3, 3), "white", new Vector3f(2, 0, 0), 0, 0, 0, 1));
}

function tick(){
    // entities[0].rotate(1, 0, 0);
}

function render(){
    shader.start();
    shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    shader.loadData("cameraRot", Maths.getCameraRot(CameraManager.getCam()));

    shader.run("loadLights", light);

    for(var i = 0; i < entities.length; i++){
        shader.bindVAOFromEntity(entities[i]);
        shader.loadData("textureSampler", SpriteBinder.loadSprite("cave/card_back").getID());
        shader.render(entities[i]);
        shader.unBindVAO();
    }

    shader.stop();
}
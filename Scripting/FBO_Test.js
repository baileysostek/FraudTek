/**
 *
 * Created by Bailey on 2/24/2018.
 */

var shader;
var vectorShader;
var entities = [];

var light;

var frameBufferObject;
var fbo_view;

function init(){
    shader = new Shader("dynamic_lighting");
    vectorShader = new Shader("vector");
    CameraManager.setCamera(new FPSCamera());

    light = LightingEngine.addLight(new Vector3f(2, 2, 2), new Vector3f(1, 1, 1));

    entities.push(new EntityModel(ModelLoader.loadModel("sphere"), "white", new Vector3f(0, 0, 0), 0, 0, 0, 1));

    frameBufferObject = new FBO();

    fbo_view = new EntityModel(ModelLoader.generateQuad(3, 3), "white", new Vector3f(2, 0, 0), 0, 0, 0, 1);
}

function tick(){
    // entities[0].rotate(1, 0, 0);
}

function render(){
    var tmp_sprite = SpriteBinder.loadSprite("white");

    shader.start();
    shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    shader.loadData("cameraRot", Maths.getCameraRot(CameraManager.getCam()));

    shader.run("loadLights", light);

    for(var i = 0; i < entities.length; i++){
        shader.bindVAOFromEntity(entities[i]);
        shader.loadData("textureSampler", tmp_sprite.getID());
        shader.render(entities[i]);
        shader.unBindVAO();
    }

    shader.stop();


    frameBufferObject.bindFrameBuffer();
    vectorShader.start();
    vectorShader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    //Buffer only once in the future, per model
    vao = new VAO();
    var normals = entities[0].getComponent(EnumComponentType.MESH).getModel().getNormals();
    var normalList = [];
    for (var j = 0; j < normals.length / 3; j++) {
        var faceCenter = entities[0].getComponent(EnumComponentType.MESH).getModel().getFaceCenterpoint(j);
        var scale = entities[0].getScale();
        normalList[j * 6 + 0] = faceCenter[0] * scale;
        normalList[j * 6 + 1] = faceCenter[1] * scale;
        normalList[j * 6 + 2] = faceCenter[2] * scale;
        normalList[j * 6 + 3] = normals[j * 3 + 0] + faceCenter[0] * scale;
        normalList[j * 6 + 4] = normals[j * 3 + 1] + faceCenter[1] * scale;
        normalList[j * 6 + 5] = normals[j * 3 + 2] + faceCenter[2] * scale;
    }

    var indicieList = [];
    for (var j = 0; j < normalList.length / 3; j++) {
        indicieList[j] = j;
    }
    VAOManager.bindIndiciesBuffer(indicieList);

    vao.addVBO(3, normalList);
    VAOManager.unbindVAO();

    vectorShader.bindVAO(vao);
    vectorShader.render(entities[0].getPosition(), vao.getVBOLength(0) / 3, entities[0].getRotX(), entities[0].getRotY(), entities[0].getRotZ());
    vectorShader.unBindVAO();

    vectorShader.stop();
    frameBufferObject.unbindFrameBuffer();


    shader.start();
        shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
        shader.loadData("cameraRot", Maths.getCameraRot(CameraManager.getCam()));

        shader.run("loadLights", light);

        shader.bindVAOFromEntity(fbo_view);
        shader.loadData("textureSampler", frameBufferObject.getTextureID());
        shader.render(fbo_view);
        shader.unBindVAO();
    shader.stop();

}
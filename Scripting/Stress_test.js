/**
 *
 * Created by Bailey on 2/24/2018.
 */

var shader;
var entities = [];
var dragons = [];

var vectorShader;
var VectorRenderer;

var controller;

function init(){
    vectorShader = new Shader("vector");
    shader = new Shader("dynamic_lighting");
    CameraManager.setCamera(new FPSCamera());

    LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));
    // LightingEngine.addLight(new Vector3f(0, 1, 0), new Vector3f(1, 1, 1));
    // LightingEngine.addLight(new Vector3f(0, 1, 1.5), new Vector3f(1, 0, 0));
    // LightingEngine.addLight(new Vector3f(-1.5, 1, -2), new Vector3f(0, 1, 0));
    // LightingEngine.addLight(new Vector3f(0, 3, 5), new Vector3f(1, 1, 1));
    // LightingEngine.addLight(new Vector3f(-4, 3, -5), new Vector3f(1, 0, 0));
    // LightingEngine.addLight(new Vector3f(4, 3, -5), new Vector3f(0, 1, 0));

    // entities.push(new EntityModel(ModelLoader.generateQuad(12, 12), "white", new Vector3f(0, -1, 0), 90, 0, 0, 1));
    // entities.push(new EntityModel(ModelLoader.generateQuad(2, 2), "white", new Vector3f(0, 0, 0), 0, 0, 0, 1));
    entities.push(new EntityModel(ModelLoader.loadModel("sphere4"), "white", new Vector3f(-6, 0, 0), 0, 0, 0, 1));
    entities.push(new EntityModel(ModelLoader.loadModel("plane2"), "white", new Vector3f(0, 0, 6), 0, 0, 0, 5));
    entities.push(new EntityModel(ModelLoader.loadModel("cube2"), "white", new Vector3f(0, 0, 0), 0, 0, 0, 1));
    entities.push(new EntityModel(ModelLoader.loadModel("dragon"), "white", new Vector3f(6, 0, 0), 0, 0, 0, 1));
    // entities.push(new EntityModel(ModelLoader.loadModel("apple"), "white", new Vector3f(10, 0, 0), 0, 0, 0, 15));

    VectorRenderer = ScriptingEngine.addScript("VectorRenderer");

    var size = 32;
    var scale = 2;
    for(var i = 0; i < size; i++){
        for(var j = 0; j < size; j++){
            for(var k = 0; k < size; k++){
                dragons.push(new EntityModel(ModelLoader.loadModel("sphere5"), "white", new Vector3f(i * scale - (scale * size / 2), j * scale - (scale * size / 2), k * scale - (scale * size / 2)), 0, 0, 0, 1));
            }
        }
    }

    if(ControllerManager.getControllerCount() > 0){
        controller = ControllerManager.getContoller(0);
    }

}

function tick(){
    if(Keyboard.isKeyDown(KeyEvent.VK_T)) {
        entities[0].rotate(0, 0, 1);
    }

    if(controller != undefined){
        var vecLocation = (controller.getLeftThumbStick()).mul(new Vector3f(-90, -90, 0));
        entities[0].setRotation(vecLocation);
    }
}

function render(){
    shader.start();
    shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    shader.loadData("cameraRot", Maths.getCameraRot(CameraManager.getCam()));

    shader.run("loadLights", LightingEngine.getLights());
    for(var i = 0; i < entities.length; i++){
        shader.loadData("rotationMatrix", Maths.getEntityRot(entities[i]));
        var model = entities[i].getComponent(EnumComponentType.MESH).getModel();
        shader.bindVAOFromID(model.getVaoID());
        shader.loadData("textureSampler", entities[i].getTextureID());
        shader.render(entities[i]);
        shader.unBindVAO();
    }
    var model = dragons[0].getComponent(EnumComponentType.MESH).getModel();
    shader.bindVAOFromID(model.getVaoID());
    for(var i = 0; i < dragons.length; i++){
        shader.loadData("rotationMatrix", Maths.getEntityRot(dragons[i]));
        shader.loadData("textureSampler", dragons[i].getTextureID());
        shader.render(dragons[i]);
    }
    shader.unBindVAO();
    shader.stop();

    if(Keyboard.isKeyDown(KeyEvent.VK_1)){
        vectorShader.start();
        vectorShader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
        for (var i = 0; i < entities.length; i++) {
            //Buffer only once in the future, per model
            vao = new VAO();
            var normals = entities[i].getComponent(EnumComponentType.MESH).getModel().getNormals();
            var normalList = [];
            for (var j = 0; j < normals.length / 3; j++) {
                var faceCenter = entities[i].getComponent(EnumComponentType.MESH).getModel().getFaceCenterpoint(j);
                var scale = entities[i].getScale();
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
            vectorShader.loadData("textureSampler", entities[i].getTextureID());
            vectorShader.render(entities[i].getPosition(), vao.getVBOLength(0) / 3, entities[i].getRotX(), entities[i].getRotY(), entities[i].getRotZ());
            vectorShader.unBindVAO();
        }
        vectorShader.stop();
    }
}
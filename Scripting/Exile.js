/**
 *
 * Created by Bailey on 2/24/2018.
 */

var shader;
var entities = [];

var vectorShader;
var VectorRenderer;

var oceanShader;
var ocean;

var bounce;
var index = 0;

var reflectionQuad;
var refractionQuad;

function init(){
    vectorShader = new Shader("vector");
    shader = new Shader("dynamic_lighting");
    oceanShader = new Shader("ocean");
    CameraManager.setCamera(new FPSCamera());

    bounce = new Debouncer(false);

    LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));

    LightingEngine.addLight(new Vector3f(0, 3, 5), new Vector3f(1, 1, 1));


    ocean = new EntityModel(ModelLoader.generateQuad(512, 512), "white", new Vector3f(0, -1, 0), 90, 0, 0, 1);
    entities.push(new EntityModel(ModelLoader.generateQuad(12, 12), "brick", new Vector3f(0, 0, 0), 90, 0, 0, 1));
    entities.push(new EntityModel(ModelLoader.loadModel("dragon"), "brick", new Vector3f(0, 2, 2), 0, 0, 0, 1));

    reflectionQuad = new EntityModel(ModelLoader.generateQuad(2, 2), "white", new Vector3f(-2, 2,-1), 0, 0, 0, 1);
    refractionQuad = new EntityModel(ModelLoader.generateQuad(2, 2), "white", new Vector3f(2, 2, -1), 0, 0, 0, 1);

    VectorRenderer = ScriptingEngine.addScript("VectorRenderer");
}

function tick(){

}

function render(){
    renderScene(CameraManager.getCam());

    var reflection = oceanShader.var("reflection");
    reflection.bindFrameBuffer();
    renderScene(CameraManager.getCam());
    reflection.unbindFrameBuffer();

    var refraction = oceanShader.var("refraction");
    refraction.bindFrameBuffer();
    renderScene(CameraManager.getCam());
    refraction.unbindFrameBuffer();

    shader.start();
    shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    shader.run("loadLights", LightingEngine.getLights());
    var model = reflectionQuad.getComponent(EnumComponentType.MESH).getModel();
    shader.bindVAOFromID(model.getVaoID());
    shader.loadData("textureSampler", reflection.getTextureID());
    shader.render(reflectionQuad);
    shader.unBindVAO();
    model = refractionQuad.getComponent(EnumComponentType.MESH).getModel();
    shader.bindVAOFromID(model.getVaoID());
    shader.loadData("textureSampler", refraction.getTextureID());
    shader.render(refractionQuad);
    shader.unBindVAO();
    shader.stop();

    oceanShader.start();
    oceanShader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    var model = ocean.getComponent(EnumComponentType.MESH).getModel();
    oceanShader.bindVAOFromID(model.getVaoID());
    oceanShader.loadData("textureSampler",refraction.getTextureID());
    oceanShader.render(ocean);
    oceanShader.unBindVAO();
    oceanShader.stop();
}

function renderScene(camera) {
    shader.start();
    shader.loadData("viewMatrix", Maths.createViewMatrix(camera));

    shader.run("loadLights", LightingEngine.getLights());
    for(var i = 0; i < entities.length; i++){
        var model = entities[i].getComponent(EnumComponentType.MESH).getModel();
        var positions = model.getVerticies();
        // for(var j = 0; j< positions.length/3; j++){
        //     Log.println("Vertex["+j+"]["+positions[j * 3 + 0]+","+positions[j * 3 + 1]+","+positions[j * 3 + 2]+"]");
        // }
        shader.bindVAOFromID(model.getVaoID());
        shader.loadData("textureSampler", entities[i].getTextureID());
        shader.render(entities[i]);
        shader.unBindVAO();
    }
    shader.stop();
}
var deapthObjects = [];

var shader;

function init(){
    shader = new Shader("bloom");
    shader.start();
    shader.loadData("projectionMatrix", Renderer.getProjectionMatrix());
    shader.stop();

    CameraManager.setCamera(new FPSCamera());

    LightingEngine.addWorldLight(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

    //LightingEngine.addWorldLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));

    deapthObjects.push(new EntityModel(ModelLoader.generateQuad(12, 12), "white", new Vector3f(0, 0, 0), 90, 0, 0, 1));
    deapthObjects.push(new EntityModel(ModelLoader.generateCube(1, 1, 1), "white", new Vector3f(0, 2, 0), 90, 0, 0, 1));

    // for(var i = 0; i < deapthObjects.length; i++){
    //     EntityManager.addEntity(deapthObjects[i]);
    // }


    //Set up an on-screen GUI rendering the FBO we generate
    // var buffer = new Gui(Renderer.getFBO().getTextureID(), new Vector2f(-0.5, 0.5), new Vector2f(0.5, 0.5));
    // guis.add(buffer);

}

//Tick function is called (Game.FPS) times per second.
function tick(){

}

function render(){
    shader.start();

    shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));

    for(var i = 0; i < deapthObjects.length; i++){
        shader.loadData("textureSampler", Math.random() * 12);
        shader.bindVAO(deapthObjects[i]);
        shader.render(deapthObjects[i]);
        shader.unBindVAO();
    }

    shader.stop();
}

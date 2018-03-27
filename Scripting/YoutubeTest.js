/**
 * Created by Bailey on 12/13/2017.
 */
var shader;

var entities = [];

function init(){
    shader = new Shader("test");

    CameraManager.setCamera(new FPSCamera());

    entities[entities.length] = new EntityModel(ModelLoader.generateCube(1, 1, 1), "white", new Vector3f(0, 0, 0), 0, 0, 0, 1);
}

//Tick function is called (Game.FPS) times per second.
function tick(){

}

function render(){
    shader.start();

    shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));

    shader.loadData("color", new Vector3f(0, 1, 0));

    for(var i = 0; i < entities.length; i++){
        shader.bindVAO(entities[i]);
        shader.render(entities[i]);
        shader.unBindVAO();
    }

    shader.stop();
}


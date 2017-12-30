/**
 * Created by Bailey on 12/13/2017.
 */

var robot;

function init(){
    CameraManager.setCamera(new FPSCamera());

    LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));

    EntityManager.addEntity(EntityManager.generate(new Vector3f(0, 0, 0), "room"));

    EntityManager.addEntity(EntityManager.generate(new Vector3f(14, 0, 0), "room"));

    EntityManager.addEntity(EntityManager.generate(new Vector3f(28, 0, 0), "room"));

    EntityManager.addEntity(new EntitySprite(new Vector3f(2, .5, 1), "right", -15, 0, 0));
}

//Tick function is called (Game.FPS) times per second.
function tick(){
    // robot.rotate(0, 0.1, 0);
}

function render(){

}

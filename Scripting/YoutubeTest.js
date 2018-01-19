/**
 * Created by Bailey on 12/13/2017.
 */

var robot;

function init(){
    CameraManager.setCamera(new FPSCamera());

    LightingEngine.addWorldLight(new Vector3f(0, 12, 12), new Vector3f(1, 1, 1));

    EntityManager.addEntity(EntityManager.generate(new Vector3f(0, 0, 0), "room"));
    //
    // EntityManager.addEntity(EntityManager.generate(new Vector3f(14, 0, 0), "room"));
    //
    // EntityManager.addEntity(EntityManager.generate(new Vector3f(28, 0, 0), "room"));

    EntityManager.addEntity(new EntitySprite(new Vector3f(2, .5, 1), "right", -15, 0, 0));

    //Init background
    ScriptingEngine.add("background", "layers/mountains", -50, 8, 10, 0);
    ScriptingEngine.add("background", "layers/clouds_BG", -80, 12, 50, 180);
    ScriptingEngine.add("background", "layers/parallax-mountain-foreground-trees", 5, 4, 20, 0);

    ScriptingEngine.add("background", "floor", 0, 2, -3, 0).run("setRot", 90);

    //Floor with paralax rails

    //Sky
    var background3 = new EntitySprite(new Vector3f(0, 0, -300), "layers/sky_lightened", 0, 0, 0);
    background3.setScale(100);
    EntityManager.addEntity(background3);
}

//Tick function is called (Game.FPS) times per second.
function tick(){

}

function render(){

}

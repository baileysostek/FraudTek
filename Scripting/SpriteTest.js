/**
 *
 * Created by Bailey on 2/24/2018.
 */

var shader;
var entities = [];

var vectorShader;
var VectorRenderer;

var controller;

var light;

var index  = 1;
var left_btn;
var right_btn;

function init(){
    vectorShader = new Shader("vector");
    shader = new Shader("dynamic_lighting");
    CameraManager.setCamera(new FPSCamera());

    light = LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));

    // light = LightingEngine.addLight(new Vector3f(-3, 4, 2), new Vector3f(1, 0, 0));

    //Materials
    var floor = new EntityModel(ModelLoader.generateQuad(12, 12), "brick", new Vector3f(0, -1, 0), 90, 0, 0, 1);
    floor.setMaterial("Lava");

    entities.push(floor);
    // entities.push(new EntityModel(ModelLoader.generateQuad(2, 2), "white", new Vector3f(0, 0, 0), 0, 0, 0, 1));
    entities.push(new EntitySprite(new Vector3f(0, 0, 0), "right", 0, 0, 0, 1));

    entities.push(new EntitySprite(new Vector3f(8, 0, 0), "white", 0, 0, 0, 1));

    entities.push(new EntityModel(ModelLoader.loadModel("taurus"), "brick", new Vector3f(0, 0, 0), 0, 0, 0, 1));

    VectorRenderer = ScriptingEngine.addScript("VectorRenderer");

    if(ControllerManager.getControllerCount() > 0){
        controller = ControllerManager.getContoller(0);
    }

    left_btn = new Debouncer(false);
    right_btn = new Debouncer(false);
}

function tick(){
    if(Keyboard.isKeyDown(KeyEvent.VK_T)) {
        entities[0].rotate(0, 1, 0);
    }

    if(Keyboard.isKeyDown(KeyEvent.VK_SPACE)){
        light.setPosition(new Vector3f(CameraManager.getCam().getPosition()));
    }

    if(controller != undefined){
        if(left_btn.risingAction(controller.getButton(EnumButtonType.LEFT_BUMPER)>0)){
            if(index > 0){
                index--;
                Log.println("Test left");
            }
        }
        if(right_btn.risingAction(controller.getButton(EnumButtonType.RIGHT_BUMPER)>0)){
            if(index < entities.length-1){
                index++;
                Log.println("Test right");
            }
        }
        var vecLocation = (controller.getLeftThumbStick()).mul(new Vector3f(-90, -90, 0));
        entities[index].setRotation(vecLocation);

        Log.println("index:"+index);
    }
}

function render(){
    shader.start();
    shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    shader.loadData("cameraRot", Maths.getCameraRot(CameraManager.getCam()));
    shader.run("loadLights", LightingEngine.getLights());
    // shader.run("loadMaterial", "brick");
    for(var i = 0; i < entities.length; i++){
        shader.loadData("rotationMatrix", Maths.getEntityRot(entities[i]));
        var model = entities[i].getComponent(EnumComponentType.MESH).getModel();
        shader.bindVAOFromID(model.getVaoID());
        shader.run("loadMaterial", entities[i].getMaterial().getName());
        shader.render(entities[i]);
        shader.unBindVAO();
    }
    shader.stop();
}
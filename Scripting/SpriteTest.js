/**
 *
 * Created by Bailey on 2/24/2018.
 */
var shader;
var entities = [];

var VectorRenderer;

var controller;

var light;

var index = 0;
var left_btn;
var right_btn;

function init(){
    shader = new Shader("dynamic_lighting");
    CameraManager.setCamera(new FPSCamera());

    light = LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 0, 0));
    LightingEngine.addWorldLight();
    // light = LightingEngine.addLight(new Vector3f(-3, 4, 2), new Vector3f(1, 0, 0));

    // Materials
    var floor = new EntityModel(ModelLoader.loadModel("hex"), "brick", new Vector3f(24, 0, 0), 90, 0, 0, 1);
    floor.setMaterial("White");

    entities.push(floor);

    var floor2 = new EntityModel(ModelLoader.loadModel("quad2"), "brick", new Vector3f(12, 0, 0), 0, 0, 0, 6);
    floor2.setMaterial("Brick");

    entities.push(floor2);

    var floor2 = new EntityModel(ModelLoader.loadModel("cube4"), "brick", new Vector3f(0, 0, 0), 0, 0, 0, 3);
    floor2.setMaterial("Brick");

    entities.push(floor2);
    //
    var floor3 = new EntityModel(ModelLoader.loadModel("sphere2"), "brick", new Vector3f(-12, 0, 0), 0, 0, 0, 1);
    floor3.setMaterial("White");

    entities.push(floor3);

    var floor4 = new EntityModel(ModelLoader.loadModel("dragon"), "brick", new Vector3f(-24, 0, 0), 0, 0, 0, 1);
    floor4.setMaterial("White");

    entities.push(floor4);

    entities.push(new EntitySprite(new Vector3f(0, 0, 0), "outsidetrainwall", 0, 0, 0, 1));


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
            }else{
                index = entities.length -1;
            }
        }
        if(right_btn.risingAction(controller.getButton(EnumButtonType.RIGHT_BUMPER)>0)){
            if(index < entities.length-1){
                index++;
            }else{
                index = 0;
            }
        }
        var vecLocation = (controller.getLeftThumbStick()).mul(new Vector3f(-90, -90, 0));
        entities[index].setRotation(vecLocation);
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
        shader.run("loadMaterial", entities[i].getMaterial());
        // shader.loadData("albedoMap",            entities[i].getMaterial().getAlbedoID());
        // shader.loadData("ambientOcclusionMap",  entities[i].getMaterial().getAOID());
        // shader.loadData("normalMap",            entities[i].getMaterial().getNormalID());
        // shader.loadData("displacementMap",      entities[i].getMaterial().getDisplacementID());
        // shader.loadData("reflectionMap",        entities[i].getMaterial().getReflectionID());
        // shader.loadData("roughnessMap",         entities[i].getMaterial().getRoughnessID());
        // shader.loadData("emissiveMap",          entities[i].getMaterial().getEmissiveID());
        // shader.loadData("emissivemaskMap",      entities[i].getMaterial().getEmissiveMaskID());
        shader.render(entities[i]);
        shader.unBindVAO();
    }
    shader.stop();

    // VectorRenderer.run("drawSurfaceNormals", entities);
}
var entities = [];


var shader;
var vectorShader;
var basicShader;

var light;

var vao;

var gun;
var offset;
var min_offset;
var max_offset;
var offset_speed = 0.01;

var cooldown = 0;


var mousePressed_left;
var mousePressed;

var rightClick = false;
var leftClick = false;

var VectorRenderer;

function init(){
    vectorShader = new Shader("vector");
    shader = new Shader("dynamic_lighting");
    basicShader = new Shader("basic");

    offset = 0.2;
    max_offset = offset;
    min_offset = offset/2;

    mousePressed = new Debouncer(false);
    mousePressed_left = new Debouncer(false);

    CameraManager.setCamera(new FPSCamera());
    CameraManager.getCam().setPosition(new Vector3f(0, 1.5, 0));

    light = LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));
    LightingEngine.addLight(new Vector3f(-4, 1, -4), new Vector3f(1, 0, 0));
    LightingEngine.addLight(new Vector3f(0, 1, 4), new Vector3f(0, 1, 0));
    LightingEngine.addLight(new Vector3f(4, 1, -4), new Vector3f(0, 0, 1));

    LightingEngine.addWorldLight(new Vector3f(0, 10000, 0), new Vector3f(1, 1, 1));

    entities.push(new EntityModel(ModelLoader.generateQuad(24, 64), "white", new Vector3f(0, 0, 0), 90, 0, 0, 1));

    entities.push(new EntityModel(ModelLoader.loadModel("big_dodge"), "white", new Vector3f(4, 1, 4), 0, 37, 0, 0.33));
    entities.push(new EntityModel(ModelLoader.loadModel("big_dodge"), "white", new Vector3f(16, 1, 9), 0, -37, 0, 0.33));
    entities.push(new EntityModel(ModelLoader.loadModel("big_dodge"), "white", new Vector3f(-12, 1, 3), 0, 90, 0, 0.33));

    gun = new EntityModel(ModelLoader.loadModel("tommygun"), "white", new Vector3f(0, 0, 0), 0, 90, 0, 0.1);
    entities.push(gun);

    VectorRenderer = ScriptingEngine.addScript("VectorRenderer");
}

//Tick function is called (Game.FPS) times per second.
function tick(){
    if(Keyboard.isKeyDown(KeyEvent.VK_F)) {
        entities[0].rotate(0, 0, 1);
    }

    if(mousePressed_left.risingAction(Mouse.pressed(EnumMouseButton.LEFT))){
        leftClick = true;
    }

    if(mousePressed_left.fallingAction(Mouse.pressed(EnumMouseButton.LEFT))){
       leftClick = false;
    }

    if(leftClick && cooldown==0){
        var camVec = CameraManager.getCam().getPosition();
        var mouseVec = MouseRay.getCurrentRay();
        var ray = [camVec.x, camVec.y, camVec.z, camVec.x + (mouseVec.x * 20), camVec.y + (mouseVec.y * 20), camVec.z + (mouseVec.z * 20)];
        VectorRenderer.run("addVector", camVec, mouseVec.mul(20));

        var result = new Vector3f(0, 0, 0);

        if(fire(new Vector3f(ray[0], ray[1], ray[2]), new Vector3f(ray[3], ray[4], ray[5]), result)){
            // entities.push(new EntityModel(ModelLoader.loadModel("fracttree"), "white", result.add(new Vector3f(0, 5, 0)), 0, Math.random() * 360, 0, 1));
            Log.println("HIT:"+result);
        }

        // cooldown+=parseInt(Math.random() * 8);
        // offset = (Math.random() * (max_offset))+ min_offset;
    }

    if(cooldown > 0){
        cooldown--;
    }

    if(mousePressed.risingAction(Mouse.pressed(EnumMouseButton.RIGHT))){
        rightClick = true;
    }

    if(mousePressed.fallingAction(Mouse.pressed(EnumMouseButton.RIGHT))){
        rightClick = false;
    }

    if(rightClick){
        if(offset > min_offset){
            offset -= offset_speed;
            if(offset < min_offset){
                offset = min_offset;
            }
        }
    }else{
        if(offset < max_offset){
            offset += offset_speed;
            if(offset > max_offset){
                offset = max_offset;
            }
        }
    }

    gun.setPosition(new Vector3f(new Vector3f(0, 1.5, 0)).add(CameraManager.getCam().getLookingDirection().mul(offset)));
    gun.setPosition(new Vector3f(CameraManager.getCam().getPosition()).add(CameraManager.getCam().getLookingDirection().mul(offset)));
    gun.setRotation(new Vector3f(CameraManager.getCam().getRotation()).mul(1, 0, 0).add(new Vector3f(90, 0, 0)));
}

function render(){
    // Render
    shader.start();
    shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    shader.loadData("cameraRot", Maths.getCameraRot(CameraManager.getCam()));

    shader.run("loadLights", LightingEngine.getLights());

    for(var i = 0; i < entities.length; i++){
        shader.bindVAOFromEntity(entities[i]);
        shader.loadData("textureSampler", SpriteBinder.loadSprite("white").getID());
        shader.render(entities[i]);
        shader.unBindVAO();
    }

    shader.stop();

    if(Keyboard.isKeyDown(KeyEvent.VK_1)) {
        vectorShader.start();
        vectorShader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));

        var tmp_sprite = SpriteBinder.loadSprite("white");

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
            vectorShader.loadData("textureSampler", tmp_sprite.getID());
            vectorShader.render(entities[i].getPosition(), vao.getVBOLength(0) / 3, entities[i].getRotX(), entities[i].getRotY(), entities[i].getRotZ());
            vectorShader.unBindVAO();
        }
        vectorShader.stop();
    }
}

function fire(vec1, vec2, vec3){
    for (var i = 0; i < entities.length; i++) {
        if (entities[i].hasComponent(EnumComponentType.COLLIDER)) {
            var collision = entities[i].getComponent(EnumComponentType.COLLIDER).rayHitsMesh(vec1, vec2, vec3);
            if(collision){
                return true;
            }
        }
    }
    return false;
}
/**
 *
 * Created by Bailey on 2/24/2018.
 */

var shader;
var entities = [];

var speed = 0.005;

var flat;

function init(){
    shader = new Shader("dynamic_lighting");
    CameraManager.setCamera(new DynamicCamera(new Vector3f(0, 0, 5), new Vector3f(0, 0, 0)));

    entities.push(new EntityModel(ModelLoader.generateQuad(1.8, 1), "LL2_wireframe", new Vector3f(0, 0, 0), 0, 0, 180, 1));
    flat = new EntityModel(ModelLoader.generateQuad(1.47, 1), "LL2_flat", new Vector3f(0, 0, 0),  18.4, 0, 180 + 18.4, 1);
    entities.push(flat);
}

function tick(){
    if(Keyboard.isKeyDown(KeyEvent.VK_J)){
        flat.rotate(0, 0.1, 0);
    }
    if(Keyboard.isKeyDown(KeyEvent.VK_L)){
        flat.rotate(0, -0.1, 0);
    }
    if(Keyboard.isKeyDown(KeyEvent.VK_I)){
        flat.rotate(-0.1, 0, 0);
    }
    if(Keyboard.isKeyDown(KeyEvent.VK_K)){
        flat.rotate(0.1, 0, 0);
    }
    if(Keyboard.isKeyDown(KeyEvent.VK_U)){
        flat.rotate(0, 0, -0.1);
    }
    if(Keyboard.isKeyDown(KeyEvent.VK_O)){
        flat.rotate(0, 0, 0.1);
    }

    if(Keyboard.isKeyDown(328)){
        flat.translate(0, speed, 0);
    }
    if(Keyboard.isKeyDown(325)){
        flat.translate(0, -speed, 0);
    }
    if(Keyboard.isKeyDown(324)){
        flat.translate(speed, 0, 0);
    }
    if(Keyboard.isKeyDown(326)){
        flat.translate(-speed, 0, 0);
    }
    if(Keyboard.isKeyDown(327)){
        flat.translate(0, 0, -speed);
    }
    if(Keyboard.isKeyDown(329)){
        flat.translate(0, 0, speed);
    }

    if(Keyboard.isKeyDown(KeyEvent.VK_SPACE)){
        Log.println("rot:"+flat.getRotation());
        Log.println("off:"+flat.getPosition());
    }
}

function render(){
    shader.start();
    shader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    shader.loadData("cameraRot", Maths.getCameraRot(CameraManager.getCam()));

    for(var i = 0; i < entities.length; i++){
        GL11.glClearColor(0.0, 0.0, 1.0, 1.0);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        shader.loadData("entityRot", entities[i]);
        var model = entities[i].getComponent(EnumComponentType.MESH).getModel();
        shader.bindVAOFromID(model.getVaoID());
        shader.loadData("textureSampler", entities[i].getTextureID());
        shader.render(entities[i]);
        shader.unBindVAO();
    }

    shader.stop();
}
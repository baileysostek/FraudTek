/**
 * Created by Bailey on 11/7/2017.
 */
var bg_sprite;
var mouse_button;
var play;

var lamp1;
var lamp2;
var quad;
var cam;
var cube;

var dragon;

function init(){
    System.out.println("This is a Test of FraudTek");

    mouse_button = new Debouncer(false);

    bg_sprite = SpriteBinder.loadSprite("DRENCHED");
    var playButton_sprite = SpriteBinder.loadSprite("brick");

    guis.add(new Gui(bg_sprite.textureID, new Vector2f(0.0, 0.0), new Vector2f(1, 1)));
    play = new Gui(playButton_sprite.textureID, new Vector2f(0.0, -0.375), new Vector2f(0.5, 0.125));
    guis.add(play);

    lamp1 = EntityManager.generateEntity("lamp", new Vector3f(0, 0, 0));
    lamp2 = EntityManager.generateEntity("lamp", new Vector3f(4, 0, 0));

    var x = 4;

    quad = new EntityModel(ModelLoader.generateQuad(x * 1, x * (9/16)), Renderer.getFBO().getTextureID(), new Vector3f(1.5, 1, -0.5), 0, 0, 0, 1);

    LightingEngine.addLight(new Vector3f(12, 5, 4), new Vector3f(1, 1, 1));

    dragon = new EntityModel(ModelLoader.loadModel("robot"), "cobblestone", new Vector3f(12, 3, 4), 0, 0, 0, 1);
    EntityManager.addEntity(dragon);
    EntityManager.addEntity(quad);


    cam = new DynamicCamera(quad.getPosition().add(new Vector3f(0, 0, -0.5)), new Vector3f(0, 180, 0));
}

//Tick function is called (Game.FPS) times per second.
function tick(){
    dragon.rotate(0, 0.1, 0);
    var mouse3D = Mouse.getMouseCoords();
    if(mouse_button.risingAction(Mouse.isPressed(EnumMouseButton.LEFT))){
        if(play.pointInside(new Vector2f(mouse3D.x()/WIDTH, mouse3D.y()/HEIGHT))){
            guis.clear();
            var camera = new FPSCamera();
            CameraManager.setCamera(new FPSCamera());
            cube = new EntityModel(ModelLoader.generateCube(1, 1, 1), "white", camera.getPosition(), 0, 0, 0, 1);
            EntityManager.addEntity(cube);
            // LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));
            for(var i = 0; i < 16; i++){
                for(var j = 0; j < 16; j++){
                    EntityManager.addEntity(new EntityModel(ModelLoader.generateQuad(1, 1), "cobblestone", new Vector3f(i, 0, j), 90, 0, 0, 1));
                }
            }

            LightingEngine.addLight(new Vector3f(2, 2, 2), new Vector3f(1, 0, 0));
            // guis.add(new Gui(Renderer.getFBO().getTextureID(), new Vector2f(-0.5, 0.5), new Vector2f(0.5, 0.5)));
        }
    }
    if(cube != null) {
        cube.setPosition(CameraManager.getCam().getPosition());
        cube.setRotation(CameraManager.getCam().getRotation());
    }
}

function render(){
    Renderer.getFBO().bindFrameBuffer();

    Renderer.getFBO().unbindFrameBuffer();
}
/**
 * Created by Bailey on 11/7/2017.
 */
var bg_sprite;
var mouse_button;
var play;

function init(){
    System.out.println("This is a Test of FraudTek");

    mouse_button = new Debouncer(false);

    bg_sprite = SpriteBinder.loadSprite("DRENCHED");
    var playButton_sprite = SpriteBinder.loadSprite("brick");

    guis.add(new Gui(bg_sprite.textureID, new Vector2f(0.0, 0.0), new Vector2f(1, 1)));
    play = new Gui(playButton_sprite.textureID, new Vector2f(0.0, -0.375), new Vector2f(0.5, 0.125));
    guis.add(play);
    LightingEngine.addLight(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0));
}

//Tick function is called (Game.FPS) times per second.
function tick(){
    var mouse3D = Mouse.getMouseCoords();
    if(mouse_button.risingAction(Mouse.isPressed(EnumMouseButton.LEFT))){
        if(play.pointInside(new Vector2f(mouse3D.x()/WIDTH, mouse3D.y()/HEIGHT))){
            guis.clear();
            EntityManager.addEntity("chest", new Vector3f(0, 0, 0));
            CameraManager.setCamera(new FPSCamera());
            LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));
        }
    }
}
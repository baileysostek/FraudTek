/**
 * Created by Bailey on 12/18/2017.
 */
var bg_sprite;
var mouse_button;
var play;


function init(){
    mouse_button = new Debouncer(false);

    bg_sprite = SpriteBinder.loadSprite("murder_train_logo");
    var playButton_sprite = SpriteBinder.loadSprite("brick");

    guis.add(new Gui(bg_sprite.textureID, new Vector2f(0.0, 0.0), new Vector2f(1, 1)));
    play = new Gui(playButton_sprite.textureID, new Vector2f(0.0, -0.375), new Vector2f(0.5, 0.125));
    guis.add(play);
}

//Tick function is called (Game.FPS) times per second.
function tick(){
    var mouse3D = Mouse.getMouseCoords();
    if(mouse_button.risingAction(Mouse.isPressed(EnumMouseButton.LEFT))){
        if(play.pointInside(new Vector2f(mouse3D.x()/WIDTH, mouse3D.y()/HEIGHT))){
            guis.clear();
            var camera = new FPSCamera();
            CameraManager.setCamera(camera);

            LightingEngine.addLight(CameraManager.getCam().getPosition(), new Vector3f(1, 1, 1));
            // var chest = EntityManager.generateEntity("chest", new Vector3f(0, 0, 0));
            // EntityManager.addEntity(chest);

            for(var i = 0; i < 16; i++){
                for(var j = 0; j < 16; j++){
                    EntityManager.addEntity(new EntityModel(ModelLoader.generateQuad(1, 1), "white", new Vector3f(i, 0, j), 90, 0, 0, 1));
                }
            }


        }
    }
}

function render(){

}
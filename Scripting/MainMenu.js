/**
 * Created by Bailey on 12/18/2017.
 */
var bg_sprite;
var mouse_button;
var play;

//Change these variables
var PATH_TO_BG = "murder_train_logo";
var PATH_TO_PLAY = "brick";
var PATH_TO_SCRIPT = "FPS_Test";

function init(){
    mouse_button = new Debouncer(false);

    bg_sprite = SpriteBinder.loadSprite(PATH_TO_BG);
    var playButton_sprite = SpriteBinder.loadSprite(PATH_TO_PLAY);

    guis.add(new Gui(bg_sprite.textureID, new Vector2f(0.0, 0.0), new Vector2f(1, 1)));
    play = new Gui(playButton_sprite.textureID, new Vector2f(0.0, -0.375), new Vector2f(0.5, 0.125));
    guis.add(play);
}

//Tick function is called (Game.FPS) times per second.
function tick(){
    var mouse3D = MouseRay.getMouseCoords();
    if(mouse_button.risingAction(Mouse.pressed(EnumMouseButton.LEFT))){
        if(play.pointInside(new Vector2f(mouse3D.x()/WIDTH, mouse3D.y()/HEIGHT))){
            guis.clear();
            ScriptingEngine.addScript(PATH_TO_SCRIPT);
            ScriptingEngine.remove(self);
        }
    }
}

function render(){

}
//item_name.js
var entity;

//Attributes
var name;
var sprite;
var gravity;
var onGround;
var distance_to_interact;

var ComponentLight = Java.type("entity.component.ComponentLight");
var Vector3f = Java.type("org.joml.Vector3f");

function init(reference){
    entity = reference;
    name = entity.getAttribute("name");
    sprite = entity.getAttribute("sprite");
    gravity = entity.getAttribute("gravity");
    onGround = entity.getAttribute("onGround");
    distance_to_interact = entity.getAttribute("distance_to_interact");

    entity.addComponent(new ComponentLight(entity, new Vector3f(1, 0, 0)));

    sprite.setData("rock");

    distance_to_interact.setData(0.4);
}

//Tick function is called (Game.FPS) times per second.
function tick(){

}

//Functions
//Added from interact component.
function onIteract(){
    // Pickup call
    entity.addAcceleration(new Vector3f(0, 0.04, 0));
}

//Added from interact component.
function leftClick(){
     //User defined
}

//Added from interact component.
function rightClick(){
     //User defined
}


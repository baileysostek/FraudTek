//item_name.js
var entity;

//Attributes
var name;
var sprite;
var gravity;
var onGround;
var distance_to_interact;

function init(reference){
     entity = reference;
     name = entity.getAttribute("name");
     sprite = entity.getAttribute("sprite");
     gravity = entity.getAttribute("gravity");
     onGround = entity.getAttribute("onGround");
     distance_to_interact = entity.getAttribute("distance_to_interact");

    sprite.setData("rock");
    name.setData("item_rock");
}

//Tick function is called (Game.FPS) times per second.
function tick(){

}

//Functions
//Added from interact component.
function onIteract(){
    // Pickup call
    out.println("Suck my ass motherfucker im a");
}

//Added from interact component.
function leftClick(){
     //User defined
}

//Added from interact component.
function rightClick(){
     //User defined
}


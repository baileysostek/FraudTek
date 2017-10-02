//item_torch.js
var entity;

//Attributes
var name;
var sprite;
var onGround;
var callback;
var gravity;
var distance_to_interact;

function init(reference){
     entity = reference;
     name = entity.getAttribute("name");
     sprite = entity.getAttribute("sprite");
     onGround = entity.getAttribute("onGround");
     callback = entity.getAttribute("callback");
     gravity = entity.getAttribute("gravity");
     distance_to_interact = entity.getAttribute("distance_to_interact");
}

//Tick function is called (Game.FPS) times per second.
function tick(){

}

//Functions
//Added from interact component.
function onIteract(){

}

//Added from interact component.
function leftClick(){

}

//Added from interact component.
function rightClick(){

}


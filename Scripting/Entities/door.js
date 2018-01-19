//Entities/chest.js
var entity;

//Attributes
var mesh1;
var onGround;
var render;
var distance_to_interact;
var controllerIndex;
var controller;

var Debouncer = Java.type("base.util.Debouncer");
var EnumButtonType = Java.type("base.controller.EnumButtonType");
var Vector3f = Java.type("org.joml.Vector3f");

var open;

function init(reference){
     entity = reference;
     mesh1 = entity.getAttribute("mesh1");
     onGround = entity.getAttribute("onGround");
     render = entity.getAttribute("render");
     distance_to_interact = entity.getAttribute("distance_to_interact");
     controllerIndex = entity.getAttribute("controllerIndex");
     controller = entity.getAttribute("controller");

     open = new Debouncer(false);
}

//Tick function is called (Game.FPS) times per second.
function tick(){

}

//Functions
//Added from collider component.
function onCollide(){

}

//Added from interact component.
function onIteract(){
    if(open.risingAction(controller.getData().getButton(EnumButtonType.A) > 0)) {
        entity.rotate(0, -22.5, 0);
    }
}


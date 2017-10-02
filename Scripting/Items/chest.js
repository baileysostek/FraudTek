//chest.js
var entity;

//Attributes
var onGround;
var callback;
var render;
var distance_to_interact;
var controllerIndex;
var controller;

var Camera = Java.type("camera.DynamicCamera");
var Debouncer = Java.type("Base.util.Debouncer");
var Game = Java.type("Base.engine.Game");

var Vector3f = Java.type("org.joml.Vector3f");

var interact;

var zoomed = false;

function init(reference){
     entity = reference;
     onGround = entity.getAttribute("onGround");
     callback = entity.getAttribute("callback");
     render = entity.getAttribute("render");
     distance_to_interact = entity.getAttribute("distance_to_interact");
     controllerIndex = entity.getAttribute("controllerIndex");
     controller = entity.getAttribute("controller");
     interact = new Debouncer(false);
}

//Functions
//Added from interact component.
function onIteract(){
     if(!Game.cameraManager.isTransitioning()) {
         if (!zoomed) {
             if (interact.risingAction(controller.getData().getButton(Java.type("Base.Controller.EnumButtonType").X) > 0)) {
                 var transitionCam = new Camera(new Vector3f(entity.getPosition()).add(0, 1, 0), new Vector3f(0, 90, 0));
                 Game.cameraManager.transition(transitionCam, 60);
                 zoomed = true;
             }
         } else {
             if (interact.risingAction(controller.getData().getButton(Java.type("Base.Controller.EnumButtonType").X) > 0)) {
                 Game.cameraManager.lastCam(60);
                 zoomed = false;
             }
         }
     }
}

function tick(){

}


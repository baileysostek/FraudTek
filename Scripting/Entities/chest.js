//Entities/chest.js
var entity;

//Attributes
var mesh;
var shouldRender;

function init(reference){
     entity = reference;
     mesh = entity.getAttribute("mesh");
     shouldRender = entity.getAttribute("shouldRender");
}

//Tick function is called (Game.FPS) times per second.
function tick(){

}

//Functions
function render(){

}
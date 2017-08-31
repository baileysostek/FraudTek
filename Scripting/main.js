var panel;
var water;
var Debouncer;
var EntityModel;
var PrebuiltModels;
var Vector3f;
var action;

var falling = false;

function init(){

  Debouncer = Java.type("Base.util.Debouncer");
  EntityModel = Java.type("entity.EntityModel");
  PrebuiltModels = Java.type("models.PrebuiltModels");
  Vector3f = Java.type("org.joml.Vector3f");

  action = new Debouncer(false);

  var data = {
    position:{
      x:0,
      y:-0.5,
      z:-3
    },
    rotation:{
      x:-90,
      y:-45,
      z:0
    },
    width:7,
    height:7
  }

  panel = EntityManager.generate(PANEL, data);
  EntityManager.addEntity(panel);

  water = new EntityModel(PrebuiltModels.generateQuad(30, 30), new Vector3f(0,-2,0), 90,0,0,1);
  water.texture(SpriteBinder.loadSprite("water"));
  EntityManager.addEntity(water);
}

function tick(){
  if(action.risingAction(panel.isSolved())){
      falling = true;
  }
  if(falling){
    if(water.getPosition().y() > -8){
      water.translate(0,-0.01, 0);
    }else{
      falling = false;
    }
  }
}

function render(){

}

//Entities/room.js
var entity;

//Attributes
var width = 33;
var height = 3;
var deapth = 4;

var camera;

var entities = [0];

var mouse_button;

function init(reference){
    mouse_button = new Debouncer(false);
    entity = reference;

    //Back
    for(var j = 0; j < height; j++){
        for(var i = 0; i < width; i++){
            var tile = new EntityModel(ModelLoader.generateQuad(1, 1), "white", new Vector3f(i + entity.x(), j + entity.y(), entity.z() - 0.5), 180, 0, 0, 1);
            EntityManager.addEntity(tile);
            entities[entities.length] = tile;
        }
    }

    //Floor
    for(var j = 0; j < deapth; j++){
        for(var i = 0; i < width; i++){
            var tile = new EntityModel(ModelLoader.generateQuad(1, 1), "white", new Vector3f(i + entity.x(), entity.y() - 0.5, j + entity.z()), 90, 0, 0, 1);
            EntityManager.addEntity(tile);
            entities[entities.length] = tile;
        }
    }

    //Left Wall
    for(var j = 0; j < height; j++){
        for(var i = 0; i < deapth; i++){
            var tile = new EntityModel(ModelLoader.generateQuad(1, 1), "white", new Vector3f(entity.x() - 0.5, entity.y() + j, entity.z() + i), 0, 90, 0, 1);
            EntityManager.addEntity(tile);
            entities[entities.length] = tile;
        }
    }

    //Right Wall
    for(var j = 0; j < height; j++){
        for(var i = 0; i < deapth; i++){
            var tile = new EntityModel(ModelLoader.generateQuad(1, 1), "white", new Vector3f(entity.x() + (width - 1) + 0.5, entity.y() + j, entity.z() + i), 0, -90, 0, 1);
            EntityManager.addEntity(tile);
            entities[entities.length] = tile;
        }
    }

    //Outside Wall
    var sprite = new EntitySprite(new Vector3f(entity.x() + (width/2) - 0.5, entity.y() + (height / 2) - 1, entity.z() - 0.5 + deapth), "outsidetrainwall_2", 0, 0, 0);
    sprite.setNormal("outsidetrainwall_normal");
    EntityManager.addEntity(sprite);

    //Lighting
    // LightingEngine.addLight(new Vector3f(entity.x() + 2, entity.y() + height, entity.z() + (deapth/2)), new Vector3f(1, 1, 1));
    // LightingEngine.addLight(new Vector3f(entity.x() + (width - 2), entity.y() + height, entity.z() + (deapth/2)), new Vector3f(1, 1, 1));

    //Painting
    EntityManager.addEntity(new EntitySprite(new Vector3f(entity.x(), entity.y() + 2, entity.z() - 0.5 + (1 /16)), "painting", 0, 0, 0));
}

//Tick function is called (Game.FPS) times per second.
function tick(){

}

//Functions
function render(){

}

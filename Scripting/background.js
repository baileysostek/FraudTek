/**
 * Created by Bailey on 12/29/2017.
 */

var backgrounds = [];

var speed = -0.1;

var size = 0;

var back = -50;

var scale = 8;

var up = 10;

var rot = -15;

var image;

function init(imagePath, backIn, scaleIn, upIn, rotZ){

    image = imagePath;

    back = backIn;

    scale = scaleIn;

    up = upIn;

    var background = new EntitySprite(new Vector3f(0, up, back), image, rot, 0, rotZ);
    background.setScale(scale);
    EntityManager.addEntity(background);

    size = background.width * scale;

    var background2 = new EntitySprite(new Vector3f(background.width * scale, up, back), image, rot, 0, rotZ);
    background2.setScale(scale);
    EntityManager.addEntity(background2);

    var background4 = new EntitySprite(new Vector3f(-background.width * scale, up, back), image, rot, 0, rotZ);
    background4.setScale(scale);
    EntityManager.addEntity(background4);

    var background5 = new EntitySprite(new Vector3f(background.width * scale * 2, up  , back), image, rot, 0, rotZ);
    background5.setScale(scale);
    EntityManager.addEntity(background5);

    backgrounds[backgrounds.length] = background;
    backgrounds[backgrounds.length] = background2;
    backgrounds[backgrounds.length] = background4;
    backgrounds[backgrounds.length] = background5;
}

//Tick function is called (Game.FPS) times per second.
function tick(){
    for(var i = 0; i < backgrounds.length; i++){
        backgrounds[i].getPosition().add(new Vector3f(speed, 0, 0));
        if(DistanceCalculator.distance(new Vector3f(-size*1.5, up, back), backgrounds[i].getPosition()) < 10){
            backgrounds[i].getPosition().add(new Vector3f(size * (backgrounds.length-1), 0, 0));
        }
    }
}

function render(){

}

function setRot(inRot) {
    rot = inRot;
    System.out.println("Working");
    for(var i = 0; i < backgrounds.length; i++){
        backgrounds[i].setRotX(inRot);
    }
}
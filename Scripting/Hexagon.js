
//HEXAGON FUNCTIONS -----------------------------------------------------------------------------------------------

//generate a grid: takes lenght, width, texture of tiles
function generateHexGrid(gridLength, gridWidth, texture){
    var constantWidth = Math.sqrt(3)/2 * tileSize;
    var tmp_sprite = SpriteBinder.loadSprite(texture);
    for(var j = 0; j < gridLength; j++){
        for(var i = 0; i < gridWidth; i++){
            if(j%2 == 1){
                hexagons[i+(j * gridWidth)] = (EntityManager.generate(new Vector3f(((constantWidth)/2)+(i * constantWidth), 0, j * 0.75 * tileSize), "tile"));
            }else{
                hexagons[i+(j * gridWidth)] = (EntityManager.generate(new Vector3f((i * constantWidth), 0, j * 0.75 * tileSize), "tile"));
            }
            hexagons[i+(j * gridWidth)].setRotY(30);
            hexagons[i+(j * gridWidth)].setScale(tileSize);
            hexagons[i+(j * gridWidth)].setTexture(tmp_sprite.getID());
            setXY(hexagons[i+(j * gridWidth)], i, j);
        }
    }
}

//takes in an x and y coordinate and returns the hexagon you want
function getHexByCoords(x, y){
    if(isValid(x, y)){
        var desiredHex = x + (gridWidth * y);
        return hexagons[desiredHex];
    }
    return;
}

//Checks to see that the hex at xy exists.
function isValid(x, y) {
    if(x >=0 && x <gridWidth){
        if(y >=0 && y <gridLength){
            return true;
        }
    }
    return false;
}

//takes in a desired height and desired hexagon and changes its height
function setHexHeight(desiredHex, height){
    var currentPos = desiredHex.getPosition();
    desiredHex.setPosition(currentPos.x(),height,currentPos.z());
}

//takes in a hex and returns an array of 6 hexes
function getHexNeighbors(x, y){
    var neighbors = [];
    if(isValid(x + 1, y)) {
        neighbors.push(getHexByCoords(x + 1, y));
    }
    if(isValid(x - 1, y)) {
        neighbors.push(getHexByCoords(x - 1, y));
    }
    if(isValid(x, y - 1)) {
        neighbors.push(getHexByCoords(x, y - 1));
    }
    if(isValid(x, y + 1)) {
        neighbors.push(getHexByCoords(x, y + 1));
    }
    if(isValid(x + 1, y + 1)) {
        neighbors.push(getHexByCoords(x+1, y+1));
    }
    if(isValid(x + 1, y - 1)) {
        neighbors.push(getHexByCoords(x + 1, y - 1));
    }
    return neighbors;
}


// takes coords of a hex and a height and sets all neighbors of that hex to that height.
function setNeighboringHeights(x, y, height){
    var homeHexNeighbors = getHexNeighbors(x, y);
    for(var i=0; i<homeHexNeighbors.length;i++){
        setHexHeight(homeHexNeighbors[i] , height);
    }
}

//takes two hexes and returns the distance between them
function distanceBetweenIndices(x1, y1, x2, y2){
    var neighbors = getHexNeighbors(x1, y1);
    var dist = 1;
    var hex2 = getHexByCoords(x2, y2);

    for(var i = 0; i<neighbors.length;i++){
        if(hexArrayContains(neighbors, hex2)){
            dist++;
            return dist;
        }

        if(i == neighbors.length-1){
            var closest = Number.MAX_VALUE;
            var closestIndex = 0;

            for(var j = 0; j < neighbors.length; j++){
                var distance = DistanceCalculator.distance(hex2.getPosition(), neighbors[j].getPosition());
                if(distance < closest){
                    closest = distance;
                    closestIndex = j;
                }
            }

            var xy = getXY(neighbors[closestIndex]);
            neighbors = getHexNeighbors(xy[0], xy[1]);
            i=0;
            dist++;
        }
    }
}

//takes two hexes and returns the distance between them
function distanceBetweenHexes(hex1, hex2){
    var xy1 = getXY(hex1);
    var xy2 = getXY(hex2);
    return distanceBetweenIndices(xy1[0], xy1[1], xy2[0], xy2[1]);
}

function hexArrayContains(hexArray, hex){
    for(var i = 0; i < hexArray.length; i++){
        if(hexArray[i] == hex){
            return true;
        }
    }
    return false;
}

//takes a hex and makes it king of the hill.
//It determines the distance from the homehex and makes its height lower relative to how far it is
//away from the center
function kingHex(){

}

//Engine Interface Function
function setXY(hex, x, y) {
    if(hex.hasComponent(EnumComponentType.SCRIPT)){
        var scriptComponent = hex.getComponent(EnumComponentType.SCRIPT).getScript();
        scriptComponent.run("setXY", x, y);
    }
}

function getXY(hex) {
    if(hex instanceof Entity) {
        if (hex.hasComponent(EnumComponentType.SCRIPT)) {
            var scriptComponent = hex.getComponent(EnumComponentType.SCRIPT).getScript();
            return scriptComponent.run("getXY");
        }
    }
}

function findHexWithXY(x, y){
    for(var i = 0; i < hexagons.length; i++){
        var xy = getXY(hexagons[i]);
        if(xy[0] == x && xy[1] == y){
            return hexagons[i];
        }
    }
}
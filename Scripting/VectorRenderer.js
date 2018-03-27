/**
 * Created by Bailey on 2/21/2018.
 */
var VERSION = 1.0;
var vectors = [];

var vectorShader;

function init(){
    vectorShader = new Shader("vector");
    Log.println();
    Log.println("Vector Renderer Version:"+VERSION);
    Log.println("Created by Bailey");
    Log.println();
}

function tick(){

}

function render(){
    vectorShader.start();
    vectorShader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));

    for (var i = 0; i < vectors.length; i++) {
        vectorShader.bindVAOFromID(vectors[i]);
        vectorShader.render(new Vector3f(0, 0, 0), vao.getVBOLength(0) / 3, 0, 0, 0);
        vectorShader.unBindVAO();
    }

    vectorShader.stop();
}

function addVector(pos, dir){
    var ray = [pos.x, pos.y, pos.z, pos.x + (dir.x * 1), pos.y + (dir.y * 1), pos.z + (dir.z * 1)];
    //Buffer only once in the future, per model
    vao = new VAO();
    var indicieList = [];
    for (var j = 0; j < ray.length / 3; j++) {
        indicieList[j] = j;
    }
    VAOManager.bindIndiciesBuffer(indicieList);

    vao.addVBO(3, ray);
    VAOManager.unbindVAO();
    vectors.push(vao.getID());
}

function drawSurfaceNormals() {
    vectorShader.start();
    vectorShader.loadData("viewMatrix", Maths.createViewMatrix(CameraManager.getCam()));
    var entities = EntityManager.getEntities();
    for (var i = 0; i < entities.length; i++) {
        //Buffer only once in the future, per model
        vao = new VAO();
        var normals = entities[i].getComponent(EnumComponentType.MESH).getModel().getNormals();
        var normalList = [];
        for (var j = 0; j < normals.length / 3; j++) {
            var faceCenter = entities[i].getComponent(EnumComponentType.MESH).getModel().getFaceCenterpoint(j);
            var scale = entities[i].getScale();
            normalList[j * 6 + 0] = faceCenter[0] * scale;
            normalList[j * 6 + 1] = faceCenter[1] * scale;
            normalList[j * 6 + 2] = faceCenter[2] * scale;
            normalList[j * 6 + 3] = normals[j * 3 + 0] + faceCenter[0] * scale;
            normalList[j * 6 + 4] = normals[j * 3 + 1] + faceCenter[1] * scale;
            normalList[j * 6 + 5] = normals[j * 3 + 2] + faceCenter[2] * scale;
        }

        var indicieList = [];
        for (var j = 0; j < normalList.length / 3; j++) {
            indicieList[j] = j;
        }
        VAOManager.bindIndiciesBuffer(indicieList);

        vao.addVBO(3, normalList);
        VAOManager.unbindVAO();

        vectorShader.bindVAO(vao);
        vectorShader.loadData("textureSampler", entities[i].getID());
        vectorShader.render(entities[i].getPosition(), vao.getVBOLength(0) / 3, entities[i].getRotX(), entities[i].getRotY(), entities[i].getRotZ());
        vectorShader.unBindVAO();
    }
    vectorShader.stop();
}
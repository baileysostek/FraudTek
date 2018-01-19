#version 400 core

in vec3 position;       //0
in vec2 textureCoords;  //1
in vec3 normal;         //2
in vec3 tangent;        //3
in vec3 bitangent;      //4

out vec2 pass_textureCoords;


uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position =  projectionMatrix * viewMatrix * worldPosition;
    pass_textureCoords = textureCoords;
}

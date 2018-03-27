/**
 * Created by Bailey on 1/18/2018.
 */
var VERSION = "400 core";
var MAX_LIGHTS = 4;

//This function is called when a shader is initialized for the first time
function init() {

}

//This is the GL version that the Shaders will be using
function getVersion() {
    return "VERSION";
}

function getAttributes() {
    var attributes = [
        {
            "type":"vec3",
            "name":"position"
        },
        {
            "type":"vec2",
            "name":"textureCoords"
        },
        {
            "type":"vec3",
            "name":"normal"
        },
        {
            "type":"vec3",
            "name":"tangent"
        },
        {
            "type":"vec3",
            "name":"bitangent"
        }
    ]

    return attributes;
}

function getUniforms() {
    var uniforms = [
        {
            "type":"mat4",
            "name":"transformationMatrix"
        },
        {
            "type":"mat4",
            "name":"projectionMatrix"
        },
        {
            "type":"mat4",
            "name":"viewMatrix"
        },
        {
            "type":"sampler2D",
            "name":"textureSampler"
        }
    ];

    return uniforms;
}
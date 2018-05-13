#version 400 core

in vec2 pass_textureCoords;
in vec3 toCameraVector;
in vec3 toLightVector[4];
in vec3 passNormal;

out vec4 out_Color;

uniform sampler2D albedoMap;
uniform sampler2D ambientOcclusionMap;
uniform sampler2D normalMap;
uniform sampler2D displacementMap;
uniform sampler2D reflectionMap;
uniform sampler2D roughnessMap;

uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;

void main(void){
    vec4 albedoColor = texture(albedoMap, pass_textureCoords);
    vec3 unitVectorToCamera = normalize(toCameraVector);     //Unit Vector to camera
    vec3 unitNormal = normalize(passNormal);         //Unit Normal

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);
    vec3 ambient = vec3(1.0, 1.0, 1.0) * 0.03;

    for(int i = 0; i < 4; i++){
        float distance = length(toLightVector[i]);
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
        float nDot = dot(normalize(toLightVector[i]), unitNormal);
        totalDiffuse = totalDiffuse+((lightColor[i] * nDot) / attFactor);
        float specularAddition = (dot(reflect(-normalize(toLightVector[i]), unitNormal), unitVectorToCamera));
        specularAddition = max(specularAddition, 0.0);
        float dampedSpecular = pow(specularAddition, 0.94);
        totalSpecular = totalSpecular + (lightColor[i] * dampedSpecular);
    }



    totalDiffuse = max(totalDiffuse, 0.0);
    totalDiffuse = min(totalDiffuse, 1.0);


    if(albedoColor.a<0.5){
        discard;
    }

    out_Color = (vec4(totalDiffuse + ambient, 1.0)) * albedoColor + (vec4(totalSpecular, 1.0));
//    out_Color = vec4(passNormal, 1.0);
}

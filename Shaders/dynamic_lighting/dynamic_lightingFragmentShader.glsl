#version 400 core

in vec2 pass_textureCoords;
in vec3 toCameraVector;
in vec3 toLightVector[4];
in vec3 passNormal;
in mat3 TBN;

out vec4 out_Color;

uniform sampler2D albedoMap;
uniform sampler2D ambientOcclusionMap;
uniform sampler2D normalMap;
uniform sampler2D displacementMap;
uniform sampler2D reflectionMap;
uniform sampler2D roughnessMap;
uniform sampler2D emissiveMap;
uniform sampler2D emissivemaskMap;

uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;

void main(void){
    vec4 albedoColor = texture(albedoMap, pass_textureCoords);
    vec4 ambientOcclusionMap = texture(ambientOcclusionMap, pass_textureCoords);
    vec4 normalColor = texture(normalMap, pass_textureCoords);
    vec3 normalMapVector = normalize(normalColor.rgb*2.0 - 1.0);
    vec4 roughnessColor = texture(roughnessMap, pass_textureCoords);
    vec4 emissiveColor = texture(emissiveMap, pass_textureCoords);
    vec4 emissiveMaskColor = texture(emissivemaskMap, pass_textureCoords);

    vec3 unitVectorToCamera = normalize(toCameraVector);     //Unit Vector to camera
    vec3 unitNormal = normalize(passNormal);         //Unit Normal
    normalMapVector = (TBN * normalize(normalMapVector)) * vec3(1.0, 1.0, -1.0);

    vec3 displacement = texture(displacementMap, pass_textureCoords).xyz * unitNormal * -1;

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);
    vec3 ambient = vec3(1.0, 1.0, 1.0) * 0.3;

    for(int i = 0; i < 4; i++){
        float distance = length(toLightVector[i]);
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
        float nDot = dot(normalize(toLightVector[i]), normalize(unitNormal + normalMapVector));
        totalDiffuse = totalDiffuse+((lightColor[i] * nDot) / attFactor);
        float specularAddition = (dot(reflect(-normalize(toLightVector[i]), normalize(unitNormal + normalMapVector)), unitVectorToCamera));
        specularAddition = max(specularAddition, 0.0);
        specularAddition = min(specularAddition, 1.0);
        float dampedSpecular = pow(specularAddition, 1000)/ attFactor;
        totalSpecular = totalSpecular + (lightColor[i] * dampedSpecular);
    }



    totalDiffuse = max(totalDiffuse, 0.0);
    totalDiffuse = min(totalDiffuse, 1.0);

    totalSpecular = max(totalSpecular, 0.0);
    totalSpecular = min(totalSpecular, 1.0);


    if(albedoColor.a<0.5){
        discard;
    }

    out_Color = (emissiveMaskColor * emissiveColor) + (albedoColor * (vec4(totalDiffuse + ambient, 1.0)) + (vec4(totalSpecular, 1.0) * roughnessColor));

//    out_Color = vec4(passNormal, 1.0);
//    out_Color = vec4(normalMapVector, 1.0);
}

#version 400 core

in vec2 pass_textureCoords;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in mat3 TBN;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform sampler2D normalSampler;
uniform sampler2D specularSampler;
uniform sampler2D roughnessSampler;

uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;

void main(void){

	vec4 textureColor = texture(textureSampler, pass_textureCoords);
	vec4 specularColor = texture(specularSampler, pass_textureCoords);
	vec4 normalColor = texture(normalSampler, pass_textureCoords);
	vec4 roughnessColor = texture(roughnessSampler, pass_textureCoords);
	

    vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 normalMapVector = normalize(normalColor.rgb*2.0 - 1.0);
	
	
	vec3 unitNormal = normalize(TBN * (normalMapVector)) * vec3(1, -1, 1);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i = 0; i < 4; i++){
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		
		vec3 unitLightVector = normalize(toLightVector[i]);
        float nDot = dot(unitVectorToCamera , unitNormal * unitLightVector);
		
        float brightness = max(nDot * (roughnessColor.r + roughnessColor.g + roughnessColor.b)/3.0f, 0.3);
        totalDiffuse = totalDiffuse + (((brightness) * lightColor[i])/attFactor);
    }

    totalDiffuse = max(totalDiffuse, 0.0);

    if(textureColor.a<0.5){
        discard;
    }

    out_Color = (vec4(totalDiffuse, 1.0) * textureColor) ;
}
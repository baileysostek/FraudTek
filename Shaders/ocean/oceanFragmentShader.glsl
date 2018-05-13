#version 400 core

in vec2 pass_textureCoords;

uniform sampler2D textureSampler;

out vec4 out_Color;

void main(void){
    vec4 textureColor = texture(textureSampler, pass_textureCoords);
    out_Color = (textureColor);
    out_Color.a = 1.0;
}

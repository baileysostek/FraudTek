package shaders;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Bailey on 1/20/2018.
 */
public class ShaderUtils {
    //This will build the vertex Data to be sent to a vertex shader
    public static String[] buildVertex(String version, HashMap<String, String> attributeTypes, HashMap<String, String> passAttributes, HashMap<String, String> uniformTypes, HashMap<String, String> uniformLocations){
        LinkedList<String> vertexData= new LinkedList<String>();

        //Put in version data
        vertexData.add("#version "+version);
        vertexData.add("");
        for(int i = 0; i < attributeTypes.size(); i++){
            vertexData.add("in "+attributeTypes.values().toArray()[i]+" "+attributeTypes.keySet().toArray()[i]+";");
        }
        vertexData.add("");
        for(int i = 0; i < passAttributes.size(); i++){
            vertexData.add("out "+passAttributes.values().toArray()[i]+" "+passAttributes.keySet().toArray()[i]+";");
        }
        vertexData.add("");
        for(int i = 0; i < uniformTypes.size(); i++){
            if(uniformLocations.get(uniformTypes.keySet().toArray()[i]).equals("vertex")) {
                vertexData.add("uniform " + uniformTypes.values().toArray()[i] + " " + uniformTypes.keySet().toArray()[i] + ";");
            }
        }
        vertexData.add("");
        vertexData.add("void main(void){");
        vertexData.add("    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);");
        vertexData.add("    gl_Position =  projectionMatrix * viewMatrix * worldPosition;");
        vertexData.add("    pass_textureCoords = textureCoords;");
        vertexData.add("}");


        String[] out = new String[vertexData.size()];
        int index = 0;
        for(String s:vertexData){
            out[index] = s;
            index++;
        }
        return out;
    }

    //This will build the fragment Data to be sent to a vertex shader
    public static String[] buildFragment(String version, HashMap<String, String> passAttributes, HashMap<String, String> uniformTypes, HashMap<String, String> uniformLocations){
        LinkedList<String> fragmentData= new LinkedList<String>();

        //Put in version data
        fragmentData.add("#version "+version);
        fragmentData.add("");
        for(int i = 0; i < passAttributes.size(); i++){
            fragmentData.add("in "+passAttributes.values().toArray()[i]+" "+passAttributes.keySet().toArray()[i]+";");
        }
        fragmentData.add("");
        fragmentData.add("out vec4 out_Color;");
        fragmentData.add("");
        for(int i = 0; i < uniformTypes.size(); i++){
            if(uniformLocations.get(uniformTypes.keySet().toArray()[i]).equals("fragment")) {
                fragmentData.add("uniform " + uniformTypes.values().toArray()[i] + " " + uniformTypes.keySet().toArray()[i] + ";");
            }
        }
        fragmentData.add("");
        fragmentData.add("void main(void){");
        fragmentData.add("    out_Color = vec4(1.0, 1.0, 1.0, 1.0);");
        fragmentData.add("}");


        String[] out = new String[fragmentData.size()];
        int index = 0;
        for(String s:fragmentData){
            out[index] = s;
            index++;
        }
        return out;
    }

    public static String[] buildJS() {
        return new String[]{
            "var VERSION = \"400 core\";",
            "var SHADER;",
            "",
            "//This function is called on initialization of this shader object.",
            "function init(shader){",
            "   SHADER = shader;",
            "}",
            "//This function is to get the VERSION variable.",
            "function getVersion(){",
            "   return VERSION;",
            "}",
            "",
            "//Add Attribute variables here, these variables are the types associated with a VAO",
            "//Each Attribute needs a \"name\" element, and a \"type\" element",
            "//Variable types are (vec2, vec3, mat4, float)",
            "//If an array is required, add the element (array=(number))",
            "//These attributes will end up as 'in' variables in the Vertex Shader",
            "function getAttributes(){",
            "   var attributes=[",
            "       {",
            "           \"type\":\"vec3\",",
            "           \"name\":\"position\"",
            "       },",
            "       {",
            "           \"type\":\"vec2\",",
            "           \"name\":\"textureCoords\"",
            "       },",
            "       {",
            "           \"type\":\"vec3\",",
            "           \"name\":\"normal\"",
            "       },",
            "       {",
            "           \"type\":\"vec3\",",
            "           \"name\":\"tangent\"",
            "       },",
            "       {",
            "           \"type\":\"vec3\",",
            "           \"name\":\"bitangent\"",
            "       }",
            "   ];",
            "   return attributes;",
            "}",
            "",
            "//Add Attribute variables here, these variables are the types associated with a VAO",
            "//Each Attribute needs a \"name\" element, and a \"type\" element",
            "//Variable types are (vec2, vec3, mat4, float)",
            "//If an array is required, add the element (array=(number))",
            "//These attributes will end up as 'out' variables in the Vertex Shader and 'in' variables in the fragment shader.",
            "function getPassAttributes(){",
            "   var pass=[",
            "       {",
            "           \"type\":\"vec2\",",
            "           \"name\":\"pass_textureCoords\"",
            "       }",
            "   ];",
            "   return pass;",
            "}",
            "",
            "//Add Uniform variables here, they simply need to exist either in the Vertex or Fragment Shader",
            "//Each uniform needs a \"name\" element, a \"location\" element, and a \"type\" element",
            "//Location tells java which shader to put each attribute into \"vertex\", or \"fragment\"",
            "//Variable types are (float, vec3, mat4, sampler2D)",
            "//If an array is required, add the element (array=(number))",
            "function getUniforms(){",
            "   var uniforms=[",
            "       {",
            "           \"type\":\"mat4\",",
            "           \"name\":\"transformationMatrix\",",
            "           \"location\":\"vertex\"",
            "       },",
            "       {",
            "           \"type\":\"mat4\",",
            "           \"name\":\"projectionMatrix\",",
            "           \"location\":\"vertex\"",
            "       },",
            "       {",
            "           \"type\":\"mat4\",",
            "           \"name\":\"viewMatrix\",",
            "           \"location\":\"vertex\"",
            "       }",
            "   ];",
            "   return uniforms;",
            "}"
        };
    }
}

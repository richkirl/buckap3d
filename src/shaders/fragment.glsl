#version 400 core
in vec2 pass_textureCoords;
in vec3 surfaceNormals;
in vec3 toLightVector;
in vec3 toCameraVector;
out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;


void main(){
	vec3 unitNormal = normalize(surfaceNormals);
	vec3 unitLightVector = normalize(toLightVector);
	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = max(nDotl,0.2);
	vec3 diffuse = brightness * lightColour;

	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
	float specularFactor = dot(reflectedLightDirection,unitVectorToCamera);
	specularFactor = max(specularFactor, 0.2);
	float dampedFactor = pow(specularFactor,shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

	out_Color =vec4(diffuse,0.7) * texture(textureSampler,pass_textureCoords)+vec4(finalSpecular,1.0);
}

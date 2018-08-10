#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 passTextureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform bool useFakeLighting;

const float fogDensity = 0.007; // TODO: Make fog variables uniform and add fog API?
const float fogGradient = 1.5;

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 posRelativeToCam = viewMatrix * worldPosition;
	
	gl_Position = projectionMatrix * posRelativeToCam;
	passTextureCoords = textureCoords;
	
	vec3 actualNormal = normal;
	if (useFakeLighting) {
		actualNormal = vec3(0.0, 1.0, 0.0);
	}
	
	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	float distToCam = length(posRelativeToCam.xyz);
	visibility = exp(-pow((distToCam * fogDensity), fogGradient)); // TODO: Future fog API should be able to turn this off
	visibility = clamp(visibility, 0.0, 1.0);
}

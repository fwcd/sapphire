#version 400 core

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform bool lightEnabled;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.1); // TODO: Experiment with proper minimum brightness here
	vec3 diffuse = brightness * lightColor;
	
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDir = reflect(lightDirection, unitNormal);
	
	float specularFactor = dot(reflectedLightDir, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * lightColor * reflectivity;
	
	vec4 textureColor = texture(textureSampler, passTextureCoords);
	
	if (textureColor.a < 0.5) {
		discard;
	}
	
	if (lightEnabled) {
		outColor = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
	} else {
		outColor = textureColor;
	}
	
	// TODO: Future fog API could disable the following
	outColor = mix(vec4(skyColor, 1.0), outColor, visibility);
}

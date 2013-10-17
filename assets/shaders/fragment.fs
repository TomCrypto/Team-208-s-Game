#version 110

uniform sampler2D texture_diffuse;

varying vec3 pass_Position;
varying vec3 pass_Normal;
varying vec2 pass_TextureCoord;

void main(void) {

	float distance = length(vec3(0.0, 0.0, 0.0) - pass_Position);
	vec3 lightVector = normalize(vec3(0.0, 0.0, 0.0));
	float diffuse = max(dot(pass_Normal, lightVector), 0.8);

	diffuse = diffuse * (2.0 / (1.0 + (1.2 * distance)));

	//gl_FragColor = vec4(0.2, 0.8, 0.35, 1.0);
	gl_FragColor = diffuse * texture2D(texture_diffuse, pass_TextureCoord);
}

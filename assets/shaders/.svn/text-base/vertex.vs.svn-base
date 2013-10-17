#version 110

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

attribute vec4 in_Position;
attribute vec4 in_Normal;
attribute vec2 in_TextureCoord;

varying vec3 pass_Position;
varying vec3 pass_Normal;
varying vec2 pass_TextureCoord;

void main(void) {
	gl_Position = in_Position;

	pass_Position = vec3(projectionMatrix * viewMatrix * modelMatrix * in_Position);
	pass_Normal = vec3(viewMatrix * modelMatrix * in_Normal);
	pass_TextureCoord = in_TextureCoord;

	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;


}

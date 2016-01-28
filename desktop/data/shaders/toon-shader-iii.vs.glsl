

varying vec3 normal;

uniform mat4 u_projView;
uniform mat3 u_projViewNormalMatrix;

attribute vec3 a_normal;
attribute vec4 a_position;

void main()
{
	normal = u_projViewNormalMatrix * a_normal;
	gl_Position = u_projView * a_position;

}
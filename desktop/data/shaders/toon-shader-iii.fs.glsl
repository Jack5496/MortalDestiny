

varying vec3 normal;

uniform vec3 u_lightDir;

void main()
{
	float intensity;
	vec4 color;
	vec3 n = normalize(normal);
	intensity = dot(u_lightDir, n);

	if (intensity > 0.95) {
		color = vec4(0.0, 0.5, 1.0, 1.0);
	} else if (intensity > 0.5) {
		color = vec4(0.0, 0.3, 0.6, 1.0);
	} else if (intensity > 0.25) {
		color = vec4(0.0, 0.2, 0.4, 1.0);
	} else {
		color = vec4(0.0, 0.1, 0.2, 1.0);
    }
	gl_FragColor = color;

}
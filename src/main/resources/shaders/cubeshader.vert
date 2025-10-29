varying vec3 color;
layout (location=0) in vec3 position;

void main() {
    color = gl_Color.rgb;
    gl_Position = vec4(position, 1.0);
}
varying vec4 vertColor;
varying float mean1;

uniform float mean;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    vertColor = abs(gl_Position) * gl_Position;
    mean1 = mean;
}
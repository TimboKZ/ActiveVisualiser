varying vec4 vertColor;
varying float mean1;

uniform float mean;

void main(){
    gl_FragColor = vertColor / 2;
}
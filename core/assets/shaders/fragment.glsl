#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_brushPos;       // pozycja pędzla (0.0–1.0)
uniform vec2 u_boundsMin;
uniform vec2 u_boundsMax;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);
    vec2 fragPos = gl_FragCoord.xy;

    float localX = (fragPos.x - u_boundsMin.x) / (u_boundsMax.x - u_boundsMin.x);
    float localY = (fragPos.y - u_boundsMin.y) / (u_boundsMax.y - u_boundsMin.y);

    float inX = step(0.0, localX) * step(localX, 1.0);
    float inY = step(0.0, localY) * step(localY, 1.0);
    float inComponent = inX * inY;

    float inBrush = step(localX, u_brushPos); // trwały efekt
    float mask = inComponent * inBrush;

    vec3 base = texColor.rgb;
    vec3 inverted = vec3(1.0) - base;
    vec3 mixed = mix(base, inverted, mask);

    gl_FragColor = vec4(mixed, texColor.a);
}
#version 150 core

// This shader is used to highlight a component. It inverts colors progressing from the left to the right.

// The texture coordinates passed from the vertex shader.
varying vec2 v_texCoords;

// The color passed from the vertex shader.
varying vec4 v_color;

// The current brush position from 0.0 to 1.0.
uniform float u_brushPos;

// The bounds of the texture in pixels.
uniform vec2 u_boundsMin;
uniform vec2 u_boundsMax;

// The texture containing the image to be drawn on.
uniform sampler2D u_texture;

void main() {
    // Sample the texture at the current coordinates.
    vec4 texColor = texture2D(u_texture, v_texCoords);

    // Calculate the position of the current fragment in the texture's coordinate system.
    vec2 fragPos = gl_FragCoord.xy;
    float localX = (fragPos.x - u_boundsMin.x) / (u_boundsMax.x - u_boundsMin.x);
    float localY = (fragPos.y - u_boundsMin.y) / (u_boundsMax.y - u_boundsMin.y);

    // If the fragment is outside of the texture, set its color to black.
    float inX = step(0.0, localX) * step(localX, 1.0);
    float inY = step(0.0, localY) * step(localY, 1.0);
    float inComponent = inX * inY;

    // If the fragment is to the left of the brush, set its color to the inverted color of the fragment.
    float inBrush = step(localX, u_brushPos);
    float mask = inComponent * inBrush;

    // Mix the original color with the inverted color.
    vec3 base = texColor.rgb;
    vec3 inverted = vec3(1.0) - base;
    vec3 mixed = mix(base, inverted, mask);

    // Set the final color of the fragment.
    gl_FragColor = vec4(mixed, texColor.a);
}
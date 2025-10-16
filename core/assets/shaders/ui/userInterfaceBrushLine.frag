#version 150 core

// Input data from vertex shader
in vec4 v_color;// Color of the vertex
in vec2 v_texCoords;// Texture coordinates of the vertex
out vec4 FragColor;// Final color of the pixel

// Uniform values set by the program
uniform sampler2D u_texture;// Texture to be drawn
uniform float strokeProgress;// Progress of the brush stroke (0..1)
uniform vec2 strokeTopStart;// Starting point of the top brush stroke
uniform vec2 strokeBottomStart;// Starting point of the bottom brush stroke
uniform float strokeLength;// Length of the brush stroke
uniform float strokeWidth;// Width of the brush stroke

// Random number generator (hash function)
// Used to generate noise for the brush stroke
float rand(vec2 co) {
    return fract(sin(dot(co, vec2(12.9898,78.233))) * 43758.5453);
}

// Function to draw a single brush stroke
// pos: position of the pixel
// start: starting point of the brush stroke (left side of a component)
float drawBrushStroke(vec2 pos, vec2 start) {
    // Calculate the distance from the starting point
    // parallel: distance along the brush stroke
    // perpendicular: distance from the center of the brush stroke
    vec2 delta = pos - start;
    float parallel = delta.x;
    float perpendicular = delta.y;

    // Calculate the center of the brush stroke
    float center = strokeLength * 0.5;
    // Calculate the visible part of the brush stroke
    float visible = center * strokeProgress;
    // Mask the brush stroke to only show the visible part
    float timeMask = step(abs(parallel - center), visible);

    // Calculate the width of the brush stroke
    float edgeNoise = rand(pos * 0.02 + vec2(7.0, 3.0));
    float localWidth = strokeWidth * (0.6 + edgeNoise * 0.4);
    // If the pixel is outside the brush stroke, return 0
    if (abs(perpendicular) > localWidth * 0.5) return 0.0;

    // Fade the brush stroke at the ends
    float fade = smoothstep(strokeLength, strokeLength - 50.0, parallel) *
    smoothstep(0.0, 50.0, parallel);

    // Add some noise to the brush stroke
    float noise = rand(pos * 0.05 + sin(pos.yx * 0.01));
    // Return the final value of the brush stroke
    return (0.8 + noise * 0.4) * fade * timeMask;
}

void main() {
    // Get the position of the pixel
    vec2 pos = gl_FragCoord.xy;
    // Calculate the ink value for the top and bottom brush strokes
    float inkTop = drawBrushStroke(pos, strokeTopStart);
    float inkBottom = drawBrushStroke(pos, strokeBottomStart);
    // Calculate the final ink value
    float ink = inkTop + inkBottom;

    // Get the color of the texture at the current position
    vec4 texColor = texture(u_texture, v_texCoords) * v_color;
    // Calculate the final color of the pixel
    // by adding the ink value to the texture color
    vec3 finalColor = texColor.rgb + vec3(ink);
    // Set the final color of the pixel
    FragColor = vec4(finalColor, texColor.a);
}
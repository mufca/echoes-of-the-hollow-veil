#version 330 core

in vec4 v_color;
in vec2 v_texCoords;
out vec4 FragColor;

uniform sampler2D u_texture;
uniform float provideProgress; // 0.0 - 1.0
uniform vec2 provideStartingPosition;
uniform float strokeLength;
uniform float strokeWidth;

const vec2 strokeDir = normalize(vec2(1.0, 0.0));

float rand(vec2 co) {
    return fract(sin(dot(co, vec2(12.9898,78.233))) * 43758.5453);
}

float drawBrushStroke(vec2 currentFragmentPosition, vec2 startingPosition, float length, float width, float progress) {
    vec2 delta = currentFragmentPosition - startingPosition;
    float parallel = dot(delta, strokeDir);
    float perpendicular = dot(delta, vec2(-strokeDir.y, strokeDir.x));

    float center = length * 0.5;
    float maxVisible = center * progress;
    float timeMask = step(abs(parallel - center), maxVisible);

    float edgeNoise = rand(currentFragmentPosition * 0.02 + vec2(7.0, 3.0));
    float localWidth = width * (0.6 + edgeNoise * 0.4);
    if (abs(perpendicular) > localWidth * 0.5) return 0.0;

    float fade = smoothstep(length, length - 50.0, parallel) *
    smoothstep(0.0, 5.0, parallel);

    float noise = rand(currentFragmentPosition * 0.05 + sin(currentFragmentPosition.yx * 0.01));
    float variation = 0.8 + noise * 0.4;

    return variation * fade * timeMask;
}

void main() {
    vec2 currentFragmentPosition = gl_FragCoord.xy;

    float ink = drawBrushStroke(currentFragmentPosition, provideStartingPosition,
    strokeLength, strokeWidth, provideProgress);

    // użycie u_texture jako tła — bez zmian wizualnych
    vec3 baseColor = texture(u_texture, v_texCoords).rgb;

    vec3 color = mix(baseColor, vec3(1.0), ink); // pociągnięcie na tle tekstury

    FragColor = vec4(color, 1.0);
}

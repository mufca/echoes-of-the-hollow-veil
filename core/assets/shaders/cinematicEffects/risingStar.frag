// ---------------------------------------------
// Shader: Rising Star Fragment
// Author: Mówca & Jadzia
// Description: Procedural star animation with radial glow and dotted ray arms
// Last updated: 2025-06-14
// ---------------------------------------------

#version 330 core
#ifdef GL_ES
precision mediump float;
#endif

// Resolution of the screen in pixels
uniform vec2 u_resolution;
// Time elapsed since the start of the animation
uniform float u_time;
// Texture to be drawn
uniform sampler2D u_texture;

// Random number generator (hash function)
// Used to generate noise for the star
float rand(vec2 co) {
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    // Normalized position of the pixel
    vec2 normalizedPosition = gl_FragCoord.xy / u_resolution;
    // Upward movement of the star
    float upwardMovement = .25 * min(u_time / 5., 1.);
    // Center of the star
    vec2 lightCenter = vec2(.5, .5 + upwardMovement);
    // Offset of the pixel from the center of the star
    vec2 offsetFromCenter = normalizedPosition - lightCenter;
    // Distance of the pixel from the center of the star
    float distanceFromStarToCenter = length(offsetFromCenter);
    // Progress of the animation
    float progress = min(u_time / 5., 1.);
    // Intensity of the star
    float intensity = smoothstep(.01, 1.2, progress);
    // Scale of the star
    float scale = max(u_resolution.x, u_resolution.y) / min(u_time / 5., 5.);
    // Glow of the star
    float glow = intensity / (distanceFromStarToCenter * scale);

    // Create an array of rays
    vec3 rayColor = vec3(.0);
    int numberOfRays = 3;
    for (int currentRayIndex = 0; currentRayIndex < numberOfRays; currentRayIndex++) {
        // Calculate the angle of the current ray
        float angle = u_time + float(currentRayIndex) * 6.28318 / float(numberOfRays);
        // Calculate the direction of the current ray
        vec2 rayDirection = vec2(cos(angle), sin(angle));
        // Calculate the dot product of the offset from the center and the ray direction
        float rayAlign = dot(offsetFromCenter, rayDirection);
        // Distance from the ray
        float distanceFromRay = length(offsetFromCenter - rayAlign * rayDirection);
        // Sharpness of the ray
        float sharpness = .0015 / u_resolution.y;
        // Time shift of the ray
        float timeShift = u_time + float(currentRayIndex);
        // Pulsation factor based on ray's growth phase
        float pulsation = pow(abs(sin(timeShift * 1.5)), 2.);
        // Growth factor for ray length (sinusoidal variation per ray)
        float growth = .2 + .25 * (1. + sin(timeShift));
        // Calculate the maximum length of the ray
        float maxRayLength = intensity * .3 * growth;
        // Spacing of the rays dots
        float raySpacing = .004;
        // Radius of the ray dots
        float rayRadius = raySpacing * .6;
        // Falloff of the ray dots
        float rayFalloff = exp(-pow(distanceFromRay, 2.) / sharpness);

        // If the pixel is within the length of the ray
        if (abs(rayAlign) <= maxRayLength) {
            // Length fade of the ray dot
            float lengthFade = 1. - smoothstep(.0, maxRayLength, abs(rayAlign));
            // Outer glow of the ray
            float outerGlow = rayFalloff * lengthFade * (.6 + 1.2 * pulsation);
            // Ray color based on outer glow
            rayColor += vec3(.1) * outerGlow;
        }
    }
    // Core glow of the star
    float coreGlow = exp(-distanceFromStarToCenter * distanceFromStarToCenter * 100.);
    coreGlow *= progress;
    // Color of the core of the star
    vec3 coreColor = vec3(.6, .6, .8);
    // Final color of the pixel
    vec3 color = vec3(glow) + rayColor + coreColor * coreGlow;
    // Get the color of the texture at the current position
    vec4 textureVec = texture(u_texture, normalizedPosition);
    // Set the final color of the pixel
    gl_FragColor = vec4(clamp(textureVec.rgb + color, .0, 1.), textureVec.a);
}
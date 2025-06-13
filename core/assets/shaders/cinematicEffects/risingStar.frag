#version 330 core
#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution;
uniform float u_time;
uniform sampler2D u_texture;

void main() {
    vec2 normalizedPosition = gl_FragCoord.xy / u_resolution;
    float upwardMovement = .25 * min(u_time / 5., 1.);
    vec2 lightCenter = vec2(.5, .5 + upwardMovement);
    float distanceFromStarToCenter = distance(normalizedPosition, lightCenter);
    float intensity = .3 + .5 * abs(sin(u_time * 2.));
    float scale = max(u_resolution.x, u_resolution.y) / min(u_time / 5., 5.);
    float glow = intensity / (10. * distanceFromStarToCenter * scale);
    vec2 offsetFromCenter = normalizedPosition - lightCenter;

    vec3 rayColor = vec3(.0);
    int numberOfRays = 3;
    for (int currentRayIndex = 0; currentRayIndex < numberOfRays; currentRayIndex++) {
        float twoPi = 6.28318;
        float offset = float(currentRayIndex) * twoPi / float(numberOfRays);
        float baseRotation = u_time;
        float angle = baseRotation + offset;
        vec2 rayDirection = vec2(cos(angle), sin(angle));
        float rayAlignFactor = dot(offsetFromCenter, rayDirection);
        float distanceFromRay = length(offsetFromCenter - rayAlignFactor * rayDirection);
        float sharpness = .0015 / max(u_resolution.x, u_resolution.y);
        float growth = min(u_time / 5., 1.);
        float maxRayLength = intensity* .3 * growth * (.4 + .1 * sin(u_time * (2. + float(currentRayIndex))));

        if (abs(rayAlignFactor) <= maxRayLength) {
            float falloff = exp(-distanceFromRay * distanceFromRay / sharpness);
            float lengthFade = 1. - smoothstep(.0, maxRayLength, abs(rayAlignFactor));

            // --- nowy noise: dziury jak ser szwajcarski ---
            float hashNoise = fract(sin(rayAlignFactor * 800 + u_time * 0.06) * 43758.);

            // 1. Ostra kropka (rdzeń)
            float solidCore = step(.75, hashNoise);// bardzo jasne tylko przy wysokich hashach
            float coreBrightness = falloff * lengthFade * solidCore;

            // 2. Halo – światło dookoła, bez rozmywania środka
            float haloRegion = smoothstep(0.4, 0.95, hashNoise);// tylko dla nieco niższych hashy
            float haloFalloff = falloff * 1.5 * (1.0 - solidCore);// halo tylko tam, gdzie nie ma rdzenia
            float halo = haloFalloff * haloRegion * lengthFade;

            // Składanie razem
            rayColor += vec3(.8, 0.8, 0.8) * (coreBrightness + halo);
            // ---------------------------------------------
        }

    }

    vec3 color = vec3(glow) + rayColor;

    vec4 textureColor = texture2D(u_texture, normalizedPosition);
    vec3 finalColor = clamp(textureColor.rgb + color, .0, 1.);

    gl_FragColor = vec4(finalColor, textureColor.a);
}

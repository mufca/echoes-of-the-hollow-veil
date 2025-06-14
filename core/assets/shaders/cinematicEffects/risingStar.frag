#version 330 core
#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution;
uniform float u_time;
uniform sampler2D u_texture;

float rand(vec2 co) {
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec2 normalizedPosition = gl_FragCoord.xy / u_resolution;
    float upwardMovement = .25 * min(u_time / 5., 1.);
    vec2 lightCenter = vec2(.5, .5 + upwardMovement);
    vec2 offsetFromCenter = normalizedPosition - lightCenter;

    float distanceFromStarToCenter = length(offsetFromCenter);
    float progress = min(u_time / 5., 1.);
    float intensity = smoothstep(.01, 1.2, progress);
    float scale = max(u_resolution.x, u_resolution.y) / min(u_time / 5., 5.);
    float glow = intensity / (distanceFromStarToCenter * scale);

    vec3 rayColor = vec3(.0);
    int numberOfRays = 3;
    for (int currentRayIndex = 0; currentRayIndex < numberOfRays; currentRayIndex++) {
        float angle = u_time + float(currentRayIndex) * 6.28318 / float(numberOfRays);
        vec2 rayDirection = vec2(cos(angle), sin(angle));

        float rayAlign = dot(offsetFromCenter, rayDirection);
        float distanceFromRay = length(offsetFromCenter - rayAlign * rayDirection);
        float sharpness = .0015 / u_resolution.y;

        float timeShift = u_time + float(currentRayIndex);
        float growth = .2 + .5 * min(.5 + .5 * sin(timeShift), 1.);
        float maxRayLength = intensity * .3 * growth;

        float raySpacing = .004;
        float rayRadius = raySpacing * .6;
        float rayFalloff = exp(-distanceFromRay * distanceFromRay / sharpness);

        if (abs(rayAlign) <= maxRayLength) {

            float lengthFade = 1. - smoothstep(.0, maxRayLength, abs(rayAlign));

            vec2 localRaySpace = vec2(rayAlign, dot(offsetFromCenter, vec2(-rayDirection.y, rayDirection.x)));
            vec2 gridIndex = floor(localRaySpace / raySpacing);
            vec2 jitteredCenter = (gridIndex + .5 + rand(gridIndex)) * raySpacing;
            vec2 relativeToDot = localRaySpace - jitteredCenter;

            float dotDistance = length(relativeToDot / vec2(1., .4));
            float dotMask = smoothstep(rayRadius, 0., dotDistance);

            float brightness = rayFalloff * lengthFade * dotMask;
            float outerGlow = rayFalloff * lengthFade;

            rayColor += vec3(1.5) * brightness;
            rayColor += vec3(.1) * outerGlow;
        }
    }

    float coreGlow = exp(-distanceFromStarToCenter * distanceFromStarToCenter * 100.0);// mocno skupione
    coreGlow *= progress;// opcjonalnie, żeby wzrastało z czasem
    vec3 coreColor = vec3(.6, .6, .8);// ciepły, złotawy kolor

    vec3 color = vec3(glow) + rayColor + coreColor * coreGlow;
    vec4 textureVec = texture(u_texture, normalizedPosition);
    gl_FragColor = vec4(clamp(textureVec.rgb + color, .0, 1.), textureVec.a);
}

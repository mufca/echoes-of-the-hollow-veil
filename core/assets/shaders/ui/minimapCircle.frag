#version 150

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_center;
uniform float u_radius;

in vec2 v_uv;
out vec4 fragColor;

void main() {
    vec2 fragCoord = v_uv * u_resolution;
    vec2 uv = fragCoord - u_center;

    float angle = atan(uv.y, uv.x);
    float dist = length(uv);

    angle += u_time * .3;

    float mainWave = sin(angle * 4.);

    float subWave = sin(angle * 12. + u_time * 2.) * .3;

    float wave = mainWave + subWave;

    float dynamicRadius = u_radius + wave * 4.;

    float thickness = 3.;
    float edge = smoothstep(dynamicRadius + thickness, dynamicRadius, dist)
    - smoothstep(dynamicRadius, dynamicRadius - thickness, dist);

    vec3 baseColor = vec3(.85, .25, .1);
    vec3 glow = vec3(1., .8, .4) * pow(max(mainWave, 0.), 2.);// lekki połysk na kierunkach
    vec3 color = baseColor + glow * .5;

    fragColor = vec4(color, edge);
}

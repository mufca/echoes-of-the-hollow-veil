#version 150

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_center;
uniform float u_radius;

in vec2 v_uv;
out vec4 fragColor;

void main() {
    const float tau = 6.28318;
    vec2 fragCoord = v_uv * u_resolution;
    vec2 uv = fragCoord - u_center;
    float dist = length(uv);
    float angle = atan(uv.y, uv.x) + u_time * .3;

    float spikes = 0.;
    float triLength = 7.;
    float triHalfAngle = .35;

    for (int i = 0; i < 4; i++) {
        float dir = float(i) * (tau * .25);
        float diff = angle - dir;
        diff = mod(diff + tau / 2., tau) - tau / 2.;
        float insideAngle = step(abs(diff), triHalfAngle);
        float tipStart = u_radius;
        float tipEnd = u_radius + triLength;
        float radialMask = step(tipStart, dist) * step(dist, tipEnd);
        spikes = max(spikes, insideAngle * radialMask);
    }

    vec3 color = vec3(1., 1., 1.);
    if (spikes < .01) discard;
    fragColor = vec4(color, 0.45);
}
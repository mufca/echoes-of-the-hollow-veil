package io.github.mufca.libgdx.shaders;

import lombok.Getter;

import java.util.function.Function;

import static io.github.mufca.libgdx.constant.PathConstants.SHADERS_HIGHLIGHT_FRAG;
import static io.github.mufca.libgdx.constant.PathConstants.SHADERS_HIGHLIGHT_VERT;
import static io.github.mufca.libgdx.constant.PathConstants.SHADERS_UI_STROKE_FRAG;
import static io.github.mufca.libgdx.constant.PathConstants.SHADERS_UI_STROKE_VERT;

@Getter
public enum ShaderType {
    WIDGET_HIGHLIGHT(SHADERS_HIGHLIGHT_VERT, SHADERS_HIGHLIGHT_FRAG,
        "u_brushPos", t -> Math.min(t / 0.2f, 1f)),
    WIDGET_STROKE(SHADERS_UI_STROKE_VERT, SHADERS_UI_STROKE_FRAG,
        "provideProgress", t -> Math.min(t / 0.3f, 1f));

    private final String vertex, fragment, defaultElapsedUniform;
    private final Function<Float, Float> defaultElapsedFunction;

    ShaderType(String vertex, String fragment, String elapsedUniform, Function<Float, Float> elapsedFunction) {
        this.vertex = vertex;
        this.fragment = fragment;
        this.defaultElapsedUniform = elapsedUniform;
        this.defaultElapsedFunction = elapsedFunction;
    }

}

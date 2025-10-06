package io.github.mufca.libgdx.shaders;

import static io.github.mufca.libgdx.constant.PathConstants.CINEMATIC_DEFAULT_VERT;
import static io.github.mufca.libgdx.constant.PathConstants.CINEMATIC_RISING_STAR_FRAG;
import static io.github.mufca.libgdx.constant.PathConstants.SHADERS_HIGHLIGHT_FRAG;
import static io.github.mufca.libgdx.constant.PathConstants.SHADERS_HIGHLIGHT_VERT;
import static io.github.mufca.libgdx.constant.PathConstants.SHADERS_UI_STROKE_FRAG;
import static io.github.mufca.libgdx.constant.PathConstants.SHADERS_UI_STROKE_VERT;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;

@Getter
public enum ShaderType {
    CINEMATIC_RISING_STAR(CINEMATIC_DEFAULT_VERT, CINEMATIC_RISING_STAR_FRAG, t -> t / 3f) {
        @Override
        public Map<String, Object> generateUniforms(Actor actor) {
            return Map.of("u_resolution", new Vector2(actor.getStage().getWidth(), actor.getStage().getHeight()));
        }
    },
    WIDGET_HIGHLIGHT(SHADERS_HIGHLIGHT_VERT, SHADERS_HIGHLIGHT_FRAG,
        "u_brushPos", t -> Math.min(t / 0.2f, 1f)) {
        @Override
        public Map<String, Object> generateUniforms(Actor actor) {
            return ShaderHelper.getHighlightUniforms(actor);
        }
    },
    WIDGET_STROKE(SHADERS_UI_STROKE_VERT, SHADERS_UI_STROKE_FRAG,
        "strokeProgress", t -> Math.min(t / 0.3f, 1f)) {
        @Override
        public Map<String, Object> generateUniforms(Actor actor) {
            return ShaderHelper.getStrokeUniforms(actor);
        }
    };

    private final String vertex, fragment, defaultElapsedUniform;
    private final Function<Float, Float> defaultElapsedFunction;

    ShaderType(String vertex, String fragment, String elapsedUniform, Function<Float, Float> elapsedFunction) {
        this.vertex = vertex;
        this.fragment = fragment;
        this.defaultElapsedUniform = elapsedUniform;
        this.defaultElapsedFunction = elapsedFunction;
    }

    ShaderType(String vertex, String fragment) {
        this(vertex, fragment, "u_time", t -> t);
    }

    ShaderType(String vertex, String fragment, Function<Float, Float> elapsedFunction) {
        this(vertex, fragment, "u_time", elapsedFunction);
    }

    public abstract Map<String, Object> generateUniforms(Actor currentActor);
}

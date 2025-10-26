package io.github.mufca.libgdx.shaders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.mufca.libgdx.util.LogHelper;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import lombok.Getter;
import lombok.Setter;

public class ShaderHandler {

    @Getter
    private final ShaderProgram shader;
    private final ShaderType type;
    @Setter
    String elapsedUniform = null;
    @Setter
    Function<Float, Float> elapsedFunction = null;
    @Getter
    private float elapsed = 0f;
    @Setter
    private Map<String, Object> uniforms;

    public ShaderHandler(ShaderProgramEnumerated shader) {
        Objects.requireNonNull(shader);
        this.shader = shader.program();
        this.type = shader.type();
    }

    public void applyUniforms(float delta) {
        if (uniforms != null && !uniforms.isEmpty()) {
            uniforms.forEach(this::applyUniform);
        }
        applyElapsedUniform(delta);
    }

    public void applyElapsedUniform(float delta) {
        elapsed += delta;
        float elapsedValue = elapsedFunction == null ? type.defaultElapsedFunction().apply(elapsed) :
            elapsedFunction.apply(elapsed);
        String localElapsedUniform = elapsedUniform == null ? type.defaultElapsedUniform() : elapsedUniform;
        shader.setUniformf(localElapsedUniform, elapsedValue);
    }

    private void applyUniform(String uniform, Object value) {
        if (!shader.hasUniform(uniform)) {
            LogHelper.info(this, "Uniform not found in shader: " + uniform);
            return;
        }
        switch (value) {
            case Integer intValue -> shader.setUniformi(uniform, intValue);
            case Float floatValue -> shader.setUniformf(uniform, floatValue);
            case Vector2 vector2 -> shader.setUniformf(uniform, vector2);
            case Vector3 vector3 -> shader.setUniformf(uniform, vector3);
            case Vector4 vector4 -> shader.setUniformf(uniform, vector4);
            case Color color -> shader.setUniformf(uniform, color);
            default -> throw new IllegalStateException("Illegal %s uniform value: %s".formatted(uniform, value));
        }
        LogHelper.debug(this, "Applying " + uniform + " with: " + value);
    }

    public void resetElapsed() {
        elapsed = 0f;
    }

    public void baseOnActor(Actor currentActor) {
        uniforms = type.generateUniforms(currentActor);
    }
}

package io.github.mufca.libgdx.gui.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import io.github.mufca.libgdx.util.LogHelper;

public class ShaderFactory {

    private ShaderFactory() {
    }

    private static ShaderProgram create(String vertexPath, String fragmentPath) {
        FileHandle vertex = Gdx.files.internal(vertexPath);
        FileHandle fragment = Gdx.files.internal(fragmentPath);
        ShaderProgram shader = new ShaderProgram(vertex, fragment);
        if (!shader.isCompiled()) {
            LogHelper.error(ShaderFactory.class, "Shader compile error: %s".formatted(shader.getLog()));
        }
        return shader;
    }

    public static ShaderProgramEnumerated create(ShaderType type) {
        return new ShaderProgramEnumerated(create(type.vertex(), type.fragment()), type);
    }
}

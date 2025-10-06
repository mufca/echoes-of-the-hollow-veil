package io.github.mufca.libgdx.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderFactory {

    private ShaderFactory() {
    }

    private static ShaderProgram create(String vertexPath, String fragmentPath) {
        FileHandle vertex = Gdx.files.internal(vertexPath);
        FileHandle fragment = Gdx.files.internal(fragmentPath);
        ShaderProgram shader = new ShaderProgram(vertex, fragment);
        if (!shader.isCompiled()) {
            Gdx.app.error("ShaderComponent", "Shader compile error:\n" + shader.getLog());
        }
        return shader;
    }

    public static ShaderProgramEnumerated create(ShaderType type) {
        return new ShaderProgramEnumerated(create(type.getVertex(), type.getFragment()), type);
    }
}

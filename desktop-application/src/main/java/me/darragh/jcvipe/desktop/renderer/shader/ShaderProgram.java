package me.darragh.jcvipe.desktop.renderer.shader;

import me.darragh.jcvipe.desktop.renderer.shader.preparation.ShaderPreprocessor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.lwjgl.opengl.GL20C.*;

/**
 * An object representative of a shader program in OpenGL.
 *
 * @author darraghd493
 * @since 0.0.1
 */
public abstract class ShaderProgram {
    public static final String BASE_PATH = "/assets/shaders/";

    private final Map<String, Integer> uniformCache = new HashMap<>();
    private final int programId;

    protected ShaderProgram(@NotNull String vertexPath, @NotNull String fragmentPath) {
        String vertexSource = ShaderPreprocessor.process(BASE_PATH + vertexPath),
                fragmentSource = ShaderPreprocessor.process(BASE_PATH + fragmentPath);

        int vertexShader = compile(GL_VERTEX_SHADER, vertexSource, log -> new RuntimeException("Failed to compile vertex shader: " + log)),
                fragmentShader = compile(GL_FRAGMENT_SHADER, fragmentSource, log -> new RuntimeException("Failed to compile fragment shader: " + log));

        this.programId = glCreateProgram();
        glAttachShader(this.programId, vertexShader);
        glAttachShader(this.programId, fragmentShader);

        this.bindAttributeLocations(this.programId);

        glLinkProgram(this.programId);
        if (glGetProgrami(this.programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Failed to link shader program: " + glGetProgramInfoLog(this.programId, 1024));
        }

        // detach + delete shaders now the program is linked
        glDetachShader(this.programId, vertexShader);
        glDetachShader(this.programId, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    //region Abstraction
    protected abstract void bindAttributeLocations(int programId);
    //endregion

    public void bind() {
        glUseProgram(this.programId);
    }

    public void release() {
        glUseProgram(0);
    }

    public void dispose() {
        glDeleteProgram(this.programId);
        this.uniformCache.clear();
    }

    //region Uniforms
    public int getUniformLocation(@NotNull String name) {
        return this.uniformCache.computeIfAbsent(name, n -> glGetUniformLocation(this.programId, n));
    }

    //region Integer
    public void setUniform1i(@NotNull String name, int value) {
        glUniform1i(this.getUniformLocation(name), value);
    }
    //endregion
    //endregion

    //region Helpers
    private static int compile(int type, String source, Function<String, ? extends RuntimeException> exception) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            String log = glGetShaderInfoLog(shader, 1024);
            throw exception.apply(log);
        }
        return shader;
    }
    //endregion
}

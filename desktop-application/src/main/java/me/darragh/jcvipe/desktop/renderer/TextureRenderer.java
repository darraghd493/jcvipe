package me.darragh.jcvipe.desktop.renderer;

import me.darragh.jcvipe.desktop.renderer.shader.impl.TextureShaderProgram;
import me.darragh.jcvipe.desktop.renderer.state.StateManager;
import me.darragh.jcvipe.desktop.renderer.state.tracked.BlendFunctionTrackedState;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static me.darragh.jcvipe.desktop.renderer.shader.GlobalShaderPrograms.TEXTURE;
import static org.lwjgl.opengl.GL30C.*;

/**
 * Handles the rendering and usage of {@link TextureShaderProgram}.
 *
 * @author darraghd493
 * @since 0.0.1
 */
public final class TextureRenderer implements Renderer {
    private static final int STRIDE = 4 * Float.BYTES;

    private final int vaoId, vboId,  eboId;

    public TextureRenderer() {
        this.vaoId = glGenVertexArrays();
        this.vboId = glGenBuffers();
        this.eboId = glGenBuffers();
        glBindVertexArray(this.vaoId);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            // NDC quad [-1, 1] => standard UVs [0, 1]
            FloatBuffer vertices = stack.floats(
                    /* top left */ -1f, 1f, 0f, 1f,
                    /* bottom right */ -1f, -1f, 0f, 0f,
                    /* bottom left */ 1f, -1f, 1f, 0f,
                    /* top right */ 1f, 1f, 1f, 1f
            );

            IntBuffer indices = stack.ints(
                    0, 1, 2,
                    2, 3, 0
            );

            glBindBuffer(GL_ARRAY_BUFFER, this.vboId);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.eboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        }

        // aPos (location = 0)
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, STRIDE, 0);

        // aTexCoords (location = 1)
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, STRIDE, 2 * Float.BYTES);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void draw(int textureId) {
        StateManager.DEPTH.disable();
        StateManager.CULL_FACE.disable();
        StateManager.STENCIL.disable();

        StateManager.BLEND.enable();
        StateManager.BLEND_FUNCTION.set(new BlendFunctionTrackedState.Arithmetic(GL_ONE, GL_ONE_MINUS_SRC_ALPHA));

        // bind program
        TEXTURE.bind();
        glActiveTexture(GL_TEXTURE0); // TODO: Manage state
        StateManager.TEXTURE.set(textureId);
        TEXTURE.bindSourceTexture(0);

        // render mesh
        StateManager.VERTEX_ARRAY.set(this.vaoId);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        // clean up state
        StateManager.VERTEX_ARRAY.set(0);
        StateManager.TEXTURE.set(0);
        TEXTURE.release();
    }

    @Override
    public void close() {
        glDeleteBuffers(this.vboId);
        glDeleteBuffers(this.eboId);
        glDeleteVertexArrays(this.vaoId);
    }
}

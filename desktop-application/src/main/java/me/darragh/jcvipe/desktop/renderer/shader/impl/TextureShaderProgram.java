package me.darragh.jcvipe.desktop.renderer.shader.impl;

import me.darragh.jcvipe.desktop.renderer.shader.ShaderProgram;

import static org.lwjgl.opengl.GL20C.glBindAttribLocation;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class TextureShaderProgram extends ShaderProgram {
    public TextureShaderProgram() {
        super("texture.vert", "texture.frag");
    }

    @Override
    protected void bindAttributeLocations(int programId) {
        glBindAttribLocation(programId, 0, "aPos");
        glBindAttribLocation(programId, 1, "aTexCoords");
    }

    public void bindSourceTexture(int textureIndex) {
        this.setUniform1i("sourceTex", textureIndex);
    }
}

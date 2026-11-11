package me.darragh.jcvipe.desktop.renderer.framebuffer;

import lombok.Getter;
import me.darragh.jcvipe.desktop.renderer.state.StateManager;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL30C.*;

/**
 * An object representative of an OpenGL framebuffer.
 *
 * @author darraghd493
 * @since 0.0.1
 */
@Getter
public final class Framebuffer {
    private int fboId, textureId;
    private int width, height;
    private final int internalFormat, format, type, filter;

    public Framebuffer(int width, int height) {
        this(width, height, GL_RGBA8, GL_RGBA, GL_UNSIGNED_BYTE, GL_LINEAR);
    }

    public Framebuffer(int width, int height, int internalFormat, int format, int type, int filter) {
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.internalFormat = internalFormat;
        this.format = format;
        this.type = type;
        this.filter = filter;
        this.create();
    }

    private void create() {
        this.fboId = glGenFramebuffers();
        StateManager.FRAMEBUFFER.set(this.fboId);

        this.textureId = glGenTextures();
        StateManager.TEXTURE.set(this.textureId);

        glTexImage2D(GL_TEXTURE_2D, 0, this.internalFormat, this.width, this.height, 0, this.format, this.type, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, this.filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, this.filter);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.textureId, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Failed to create framebuffer");
        }
    }

    public void bind() {
        StateManager.FRAMEBUFFER.set(this.fboId);
        glViewport( 0, 0, this.width, this.height);
    }

    public void release() {
        StateManager.FRAMEBUFFER.set(0);
    }

    public void clear(float r, float g, float b, float a) {
        this.bind();
        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void resize(int newWidth, int newHeight) {
        newWidth = Math.max(1, newWidth);
        newHeight = Math.max(1, newHeight);

        if (newWidth != this.width || newHeight != this.height) {
            this.width = newWidth;
            this.height = newHeight;

            StateManager.TEXTURE.set(this.textureId);
            glTexImage2D(GL_TEXTURE_2D, 0, this.internalFormat, this.width, this.height, 0, this.format, this.type, (ByteBuffer) null);
        }
    }

    public void delete() {
        glDeleteFramebuffers(this.fboId);
        glDeleteTextures(this.textureId);
    }
}

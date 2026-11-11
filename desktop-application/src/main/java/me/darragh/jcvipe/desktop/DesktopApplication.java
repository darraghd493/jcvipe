package me.darragh.jcvipe.desktop;

import lombok.extern.slf4j.Slf4j;
import me.darragh.jcvipe.desktop.renderer.TextureRenderer;
import me.darragh.jcvipe.desktop.renderer.framebuffer.Framebuffer;
import me.darragh.jcvipe.desktop.renderer.state.StateManager;
import me.darragh.jcvipe.desktop.window.impl.GlfwWindow;
import me.darragh.jcvipe.extension.display.impl.StateHoldingDisplayExtension;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11C.*;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@Slf4j
public final class DesktopApplication {
    private GlfwWindow window;

    private Framebuffer mainFramebuffer;

    private TextureRenderer textureRenderer;

    public void start() {
        log.info("Starting desktop application...");

        this.window = new GlfwWindow();
        this.window.init(
                StateHoldingDisplayExtension.DEFAULT_WIDTH * 16,
                StateHoldingDisplayExtension.DEFAULT_HEIGHT * 16
        );

        this.window.getFramebufferSizeHook().listen(size -> this.mainFramebuffer.resize(size.width(), size.height()));
        this.window.getKeyStateHook().listen(state -> {
//            if (state.isPress()) {
//                System.out.println(state.keyCode() + " " + state.keyName());
//            }
        });

        // prepare for rendering
        createCapabilities();
        this.mainFramebuffer = new Framebuffer(
                StateHoldingDisplayExtension.DEFAULT_WIDTH * 16,
                StateHoldingDisplayExtension.DEFAULT_HEIGHT * 16
        );
        this.textureRenderer = new TextureRenderer();

        // main loop
        this.loop();

        // shutdown
        this.close();
    }

    public void loop() {
        while (!this.window.shouldClose()) {
            this.renderFramebuffer();
            this.window.swapFramebuffers();
            this.handleInput();
        }
    }

    public void close() {
        this.window.close();
    }

    //region Rendering
    private void renderFramebuffer() {
        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.mainFramebuffer.bind();
        // TODO: Rendering
        this.mainFramebuffer.clear(
                (float) (Math.abs(Math.sin(System.currentTimeMillis() / 1000.0)) / 2f),
                (float) (Math.abs(Math.sin((System.currentTimeMillis() + 334L) / 1000.0)) / 2f),
                (float) (Math.abs(Math.sin((System.currentTimeMillis() + 667L) / 1000.0)) / 2f),
                1f
        );
        this.mainFramebuffer.release();

        this.textureRenderer.draw(this.mainFramebuffer.getTextureId());
    }
    //endregion

    //region Input Handling
    private void handleInput() {
        this.window.pollEvents();
    }
    //endregion
}

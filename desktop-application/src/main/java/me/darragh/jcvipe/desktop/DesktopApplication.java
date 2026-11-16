package me.darragh.jcvipe.desktop;

import lombok.extern.slf4j.Slf4j;
import me.darragh.jcvipe.desktop.input.KeyboardStateTracker;
import me.darragh.jcvipe.desktop.input.MouseStateTracker;
import me.darragh.jcvipe.desktop.renderer.TextureRenderer;
import me.darragh.jcvipe.desktop.renderer.context.NanoVGContext;
import me.darragh.jcvipe.desktop.renderer.framebuffer.Framebuffer;
import me.darragh.jcvipe.desktop.scene.SceneHarness;
import me.darragh.jcvipe.desktop.scene.impl.NoGameLoadedScene;
import me.darragh.jcvipe.desktop.window.impl.GlfwWindow;
import me.darragh.jcvipe.extension.display.impl.StateHoldingDisplayExtension;

import static org.lwjgl.opengl.GL11C.*;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@Slf4j
public final class DesktopApplication {
    //region Input
    private KeyboardStateTracker keyboardStateTracker;
    private MouseStateTracker mouseStateTracker;
    //endregion

    //region Rendering
    private GlfwWindow window;

    private Framebuffer contentFramebuffer;
    private TextureRenderer textureRenderer;

    private SceneHarness sceneHarness;

    private NanoVGContext vgContext;
    //endregion

    //region Lifecycle
    public void start() {
        log.info("Starting desktop application...");

        this.window = new GlfwWindow();
        this.window.init(
                StateHoldingDisplayExtension.DEFAULT_WIDTH * 16,
                StateHoldingDisplayExtension.DEFAULT_HEIGHT * 16
        );

        this.keyboardStateTracker = new KeyboardStateTracker();
        this.mouseStateTracker = new MouseStateTracker();

        log.info("Attaching hooks...");
        this.window.getFramebufferSizeHook().listen(size -> this.contentFramebuffer.resize(size.width(), size.height()));

        this.window.getMouseMoveHook().listen(state -> {
            this.mouseStateTracker.setX((int) state.x());
            this.mouseStateTracker.setY((int) state.y());
        });
        this.window.getMouseEnterHook().listen(state -> {
            this.mouseStateTracker.setHovered(state.entered());
        });
        this.window.getMouseScrollHook().listen(state -> {
            this.sceneHarness.mouseScroll((int) state.x(), (int) state.y());
        });
        this.window.getMouseButtonHook().listen(state -> {
            this.mouseStateTracker.reportButton(state);
            if (state.isButtonPress()) {
                this.sceneHarness.mouseClick(this.mouseStateTracker.getX(), this.mouseStateTracker.getY(), state.button());
            } else {
                this.sceneHarness.mouseRelease(this.mouseStateTracker.getX(), this.mouseStateTracker.getY(), state.button());
            }
        });

        this.window.getKeyHook().listen(state -> {
            this.keyboardStateTracker.report(state);
            if (state.isActionPress()) {
                this.sceneHarness.keyPress(state);
            } else if (state.isActionRelease()) {
                this.sceneHarness.keyPress(state);
            }
        });

        // prepare for rendering
        log.info("Preparing renderer...");

        this.contentFramebuffer = new Framebuffer(
                StateHoldingDisplayExtension.DEFAULT_WIDTH * 16,
                StateHoldingDisplayExtension.DEFAULT_HEIGHT * 16
        );
        this.textureRenderer = new TextureRenderer();

        this.sceneHarness = new SceneHarness();
        this.sceneHarness.load(new NoGameLoadedScene(this.vgContext));

        this.vgContext = NanoVGContext.create();

        // main loop
        log.info("Starting game loop...");
        this.loop();
        log.info("Game loop finished.");

        // shutdown
        log.info("Shutting down...");
        this.close();
    }

    public void loop() {
        while (!this.window.shouldClose()) {
            this.renderFramebuffer();
            this.window.swapFramebuffers();
            this.window.pollEvents();
        }
    }

    public void close() {
        this.window.close();
    }
    //endregion

    //region Rendering
    private void renderFramebuffer() {
        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.contentFramebuffer.bind();
        this.contentFramebuffer.clear(0.0627f /* 16 / 255, dark grey background */, 0.0627f, 0.0627f, 1f);

        this.contentFramebuffer.release();

        this.textureRenderer.draw(this.contentFramebuffer.getTextureId());
    }
    //endregion
}

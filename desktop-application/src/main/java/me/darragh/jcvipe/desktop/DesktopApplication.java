package me.darragh.jcvipe.desktop;

import lombok.Getter;
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
    @Getter
    private KeyboardStateTracker keyboardStateTracker;
    @Getter
    private MouseStateTracker mouseStateTracker;
    //endregion

    //region Rendering
    private GlfwWindow window;

    private Framebuffer contentFramebuffer;
    @Getter
    private TextureRenderer textureRenderer;

    @Getter
    private SceneHarness sceneHarness;

    private NanoVGContext vg;
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

        this.window.getPathDropHook().listen(state -> {
            this.sceneHarness.dropPaths(state.paths());
        });

        // prepare for rendering
        log.info("Preparing renderer...");

        this.contentFramebuffer = new Framebuffer(
                StateHoldingDisplayExtension.DEFAULT_WIDTH * 16,
                StateHoldingDisplayExtension.DEFAULT_HEIGHT * 16
        );
        this.textureRenderer = new TextureRenderer();

        this.vg = NanoVGContext.create();

        this.sceneHarness = new SceneHarness();
        this.sceneHarness.load(new NoGameLoadedScene(this.vg));

        log.info("Loading fonts...");
        this.vg.createFont(NanoVGContext.FONT_SANS_REGULAR, "/assets/fonts/Inter_28pt-Regular.ttf");
        this.vg.createFont(NanoVGContext.FONT_SANS_MEDIUM, "/assets/fonts/Inter_28pt-Medium.ttf");
        this.vg.createFont(NanoVGContext.FONT_SANS_BOLD, "/assets/fonts/Inter_28pt-Bold.ttf");

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
            this.sceneHarness.update();
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

        int width = this.contentFramebuffer.getWidth(),
                height = this.contentFramebuffer.getHeight();

        this.contentFramebuffer.bind();
        this.contentFramebuffer.clear(0f, 0f, 0f, 1f);
        this.sceneHarness.render(this.contentFramebuffer, width, height);
        this.contentFramebuffer.release();

        this.textureRenderer.draw(this.contentFramebuffer.getTextureId());
    }
    //endregion
}

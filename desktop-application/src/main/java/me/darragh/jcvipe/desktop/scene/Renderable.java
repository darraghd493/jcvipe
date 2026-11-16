package me.darragh.jcvipe.desktop.scene;

import me.darragh.jcvipe.desktop.renderer.framebuffer.Framebuffer;
import me.darragh.jcvipe.desktop.window.Window;
import org.jetbrains.annotations.NotNull;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public interface Renderable {
    default void update() {
        // no-op
    }

    void render(@NotNull Framebuffer framebuffer, int width, int height);

    default void mouseClick(int x, int y, int button) {
        // no-op
    }

    default void mouseRelease(int x, int y, int button) {
        // no-op
    }

    default void mouseScroll(int x, int y) {
        // no-op
    }

    default void keyPress(@NotNull Window.KeyState state) {
        // no-op
    }

    default void keyRelease(int keyCode, @NotNull Window.KeyState state) {
        // no-op
    }

    default void typeChar(@NotNull String character) {
        // no-op
    }

    default void dropPaths(@NotNull String[] paths) {
        // no-op
    }
}

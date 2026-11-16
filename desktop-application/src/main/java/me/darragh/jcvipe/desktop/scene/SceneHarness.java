package me.darragh.jcvipe.desktop.scene;

import me.darragh.jcvipe.desktop.renderer.framebuffer.Framebuffer;
import me.darragh.jcvipe.desktop.window.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Harnesses an active {@link Scene}.
 *
 * @author darraghd493
 * @since 0.0.1
 */
public final class SceneHarness implements Renderable {
    private @Nullable Scene currentScene;

    public void load(@NotNull Scene scene) {
        this.currentScene = scene;
    }

    @Override
    public void update() {
        if (this.currentScene != null) {
            this.currentScene.update();
        }
    }

    @Override
    public void render(@NotNull Framebuffer framebuffer, int width, int height) {
        if (this.currentScene != null) {
            this.currentScene.render(framebuffer, width, height);
        }
    }

    @Override
    public void mouseClick(int x, int y, int button) {
        if (this.currentScene != null) {
            this.currentScene.mouseClick(x, y, button);
        }
    }

    @Override
    public void mouseScroll(int x, int y) {
        if (this.currentScene != null) {
            this.currentScene.mouseScroll(x, y);
        }
    }

    @Override
    public void mouseRelease(int x, int y, int button) {
        if (this.currentScene != null) {
            this.currentScene.mouseRelease(x, y, button);
        }
    }

    @Override
    public void keyPress(@NotNull Window.KeyState state) {
        if (this.currentScene != null) {
            this.currentScene.keyPress(state);
        }
    }

    @Override
    public void keyRelease(int keyCode, @NotNull Window.KeyState state) {
        if (this.currentScene != null) {
            this.currentScene.keyRelease(keyCode, state);
        }
    }

    @Override
    public void typeChar(@NotNull String character) {
        if (this.currentScene != null) {
            this.currentScene.typeChar(character);
        }
    }

    @Override
    public void dropPaths(@NotNull String[] paths) {
        if (this.currentScene != null) {
            this.currentScene.dropPaths(paths);
        }
    }
}

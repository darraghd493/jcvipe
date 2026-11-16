package me.darragh.jcvipe.desktop.scene;

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
    public void render() {
        if (this.currentScene != null) {
            this.currentScene.render();
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
}

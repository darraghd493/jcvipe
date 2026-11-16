package me.darragh.jcvipe.desktop.scene.impl;

import me.darragh.jcvipe.desktop.renderer.context.NanoVGContext;
import me.darragh.jcvipe.desktop.scene.Scene;
import me.darragh.jcvipe.desktop.window.Window;
import org.jetbrains.annotations.NotNull;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class NoGameLoadedScene extends Scene {
    public NoGameLoadedScene(@NotNull NanoVGContext vg) {
        super(vg);
    }

    @Override
    public void render() {

    }

    @Override
    public void mouseClick(int x, int y, int button) {

    }

    @Override
    public void mouseRelease(int x, int y, int button) {

    }

    @Override
    public void keyPress(Window.KeyState state) {

    }

    @Override
    public void keyRelease(int keyCode, Window.KeyState state) {

    }
}

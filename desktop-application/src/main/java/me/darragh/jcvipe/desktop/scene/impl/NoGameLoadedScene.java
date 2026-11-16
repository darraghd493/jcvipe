package me.darragh.jcvipe.desktop.scene.impl;

import me.darragh.jcvipe.desktop.renderer.context.NanoVGContext;
import me.darragh.jcvipe.desktop.scene.Scene;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class NoGameLoadedScene extends Scene {
    private final NVGColor backgroundColour = NVGColor.malloc()
            .r(16f / 255f)
            .g(16f / 255f)
            .b(16f / 255f)
            .a(1f);

    private final NVGColor whiteColour = NVGColor.malloc()
            .r(1f)
            .g(1f)
            .b(1f)
            .a(1f);

    public NoGameLoadedScene(@NotNull NanoVGContext vg) {
        super(vg);
    }

    @Override
    public void render(int width, int height) {
        vg.beginPath();
        vg.rectangle(0, 0, width, height);
        vg.fillColour(backgroundColour);
        vg.fill();

        vg.fillColour(whiteColour);
        vg.fontFace(NanoVGContext.FONT_SANS_BOLD);
        vg.fontSize(42f);
        vg.textAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
        vg.text(width / 2f, height / 2f, "No Game Loaded");
    }
}

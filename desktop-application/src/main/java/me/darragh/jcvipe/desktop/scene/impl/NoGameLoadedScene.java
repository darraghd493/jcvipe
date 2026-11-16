package me.darragh.jcvipe.desktop.scene.impl;

import lombok.extern.slf4j.Slf4j;
import me.darragh.jcvipe.Emulator;
import me.darragh.jcvipe.desktop.configuration.Configuration;
import me.darragh.jcvipe.desktop.extension.DesktopApplicationExtensionCollection;
import me.darragh.jcvipe.desktop.renderer.context.NanoVGContext;
import me.darragh.jcvipe.desktop.renderer.framebuffer.Framebuffer;
import me.darragh.jcvipe.desktop.scene.Scene;
import me.darragh.jcvipe.extension.ExtensionCollection;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.nanovg.NVGColor;

import static me.darragh.jcvipe.desktop.EntryPoint.APPLICATION;
import static org.lwjgl.nanovg.NanoVG.*;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@Slf4j
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
    public void render(@NotNull Framebuffer framebuffer, int width, int height) {
        vg.beginFrame(width, height);

        vg.beginPath();
        vg.rectangle(0, 0, width, height);
        vg.fillColour(backgroundColour);
        vg.fill();

        vg.fillColour(whiteColour);
        vg.fontFace(NanoVGContext.FONT_SANS_BOLD);
        vg.fontSize(42f);
        vg.textAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
        vg.text(width / 2f, height / 2f, "No Game Loaded");

        vg.endFrame();
    }

    @Override
    public void dropPaths(@NotNull String[] paths) { // TODO: Clean up, used solely for development
        log.info("Loading program: " + paths[0]);

        ExtensionCollection extensions = DesktopApplicationExtensionCollection.collection(Configuration.DEFAULT, APPLICATION);

        //noinspection MagicConstant
        Emulator emulator = new Emulator(
                extensions,
                Configuration.DEFAULT.getMemoryAllocated()
        );

        APPLICATION.getSceneHarness().load(
                new EmulatorScene(emulator, extensions, this.vg)
        );
    }
}

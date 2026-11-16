package me.darragh.jcvipe.desktop.scene.impl;

import me.darragh.jcvipe.Emulator;
import me.darragh.jcvipe.desktop.extension.display.DesktopApplicationDisplayExtension;
import me.darragh.jcvipe.desktop.renderer.context.NanoVGContext;
import me.darragh.jcvipe.desktop.renderer.framebuffer.Framebuffer;
import me.darragh.jcvipe.desktop.scene.Scene;
import me.darragh.jcvipe.extension.ExtensionCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.nanovg.NVGColor;

import static me.darragh.jcvipe.desktop.EntryPoint.APPLICATION;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class EmulatorScene extends Scene { // TODO: Finish and clean up
    private final NVGColor offColour = NVGColor.malloc()
            .r(0f)
            .g(0f)
            .b(0f)
            .a(1f);

    private final NVGColor onColour = NVGColor.malloc()
            .r(1f)
            .g(1f)
            .b(1f)
            .a(1f);

    private final @NotNull Emulator emulator;
    private final Framebuffer internalFramebuffer;
    private @Nullable boolean[] pixels;

    public EmulatorScene(@NotNull Emulator emulator, @NotNull ExtensionCollection extensions, @NotNull NanoVGContext vg) {
        super(vg);
        this.emulator = emulator;

        assert extensions.displayExtension() instanceof DesktopApplicationDisplayExtension;

        DesktopApplicationDisplayExtension displayExtension = ((DesktopApplicationDisplayExtension) extensions.displayExtension());
        this.internalFramebuffer = new Framebuffer(displayExtension.getWidth(), displayExtension.getHeight());
        displayExtension.getUpdateHook().listen(state -> this.pixels = state.pixels());
    }

    @Override
    public void render(@NotNull Framebuffer framebuffer, int width, int height) {
        if (this.pixels != null) {
            this.internalFramebuffer.bind();

            int internalWidth = this.internalFramebuffer.getWidth(),
                    internalHeight = this.internalFramebuffer.getHeight();

            vg.beginFrame(internalWidth, internalHeight);
            for (int y = 0; y < internalHeight; y++) {
                for (int x = 0; x < internalWidth; x++) {
                    boolean pixelState = this.pixels[y * internalWidth + x];
                    vg.rectangle(x, y, 1, 1);
                    if (pixelState) {
                        vg.fillColour(this.onColour);
                    } else {
                        vg.fillColour(this.offColour);
                    }
                    vg.fill();
                }
            }
            vg.endFrame();

            this.internalFramebuffer.release();

            framebuffer.bind();

            // TODO: Sharp upscale
            APPLICATION.getTextureRenderer().draw(this.internalFramebuffer.getTextureId());
        }
    }
}

package me.darragh.jcvipe.extension.display.impl;

import me.darragh.jcvipe.extension.display.DisplayExtension;

import java.util.Arrays;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public abstract class StateHoldingDisplayExtension implements DisplayExtension {
    public static final int DEFAULT_WIDTH = 64;
    public static final int DEFAULT_HEIGHT = 32;

    private final byte width, height;
    private final boolean[] pixels;

    public StateHoldingDisplayExtension() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public StateHoldingDisplayExtension(int width, int height) {
        this.width = (byte) width;
        this.height = (byte) height;
        this.pixels = new boolean[width * height];
    }

    @Override
    public void clear() {
        Arrays.fill(this.pixels, false);
        this.onUpdate(this.pixels);
    }

    @Override
    public void draw(int x, int y, int rowCount, boolean[] flags) {
        for (int i = 0; i < rowCount; i++) {
            if (y + i < this.height && x < this.width) {
                this.pixels[(y + i) * this.width + x] = flags[i];
            }
        }
        this.onUpdate(this.pixels);
    }

    protected abstract void onUpdate(boolean[] pixels);
}

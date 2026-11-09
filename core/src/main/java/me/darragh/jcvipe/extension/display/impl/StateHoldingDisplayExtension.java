package me.darragh.jcvipe.extension.display.impl;

import me.darragh.jcvipe.extension.display.DisplayExtension;

import java.util.Arrays;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public abstract class StateHoldingDisplayExtension implements DisplayExtension {
    private static final byte STANDARD_WIDTH = (byte) 64;
    private static final byte STANDARD_HEIGHT = (byte) 128;

    private final byte width, height;
    private final boolean[] pixels;

    public StateHoldingDisplayExtension() {
        this(STANDARD_WIDTH, STANDARD_HEIGHT);
    }

    public StateHoldingDisplayExtension(byte width, byte height) {
        this.width = width;
        this.height = height;
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

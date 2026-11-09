package me.darragh.jcvipe.extension.display.impl;

import me.darragh.jcvipe.extension.display.DisplayExtension;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class StubDisplayExtension implements DisplayExtension {
    @Override
    public void clear() {
        // no-op
    }

    @Override
    public void draw(int x, int y, int rowCount, boolean[] flags) {
        // no-op
    }
}

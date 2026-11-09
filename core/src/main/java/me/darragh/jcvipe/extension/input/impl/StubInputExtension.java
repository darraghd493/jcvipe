package me.darragh.jcvipe.extension.input.impl;

import me.darragh.jcvipe.extension.input.InputExtension;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class StubInputExtension implements InputExtension {
    @Override
    public boolean isPressed(char key) {
        return false;
    }
}

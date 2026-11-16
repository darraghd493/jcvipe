package me.darragh.jcvipe.desktop.extension.input;

import lombok.RequiredArgsConstructor;
import me.darragh.jcvipe.desktop.configuration.Configuration;
import me.darragh.jcvipe.desktop.input.KeyboardStateTracker;
import me.darragh.jcvipe.extension.input.InputExtension;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@RequiredArgsConstructor
public final class DesktopApplicationInputExtension implements InputExtension {
    private final Configuration.Bindings bindings;
    private final KeyboardStateTracker keyboardStateTracker;

    @Override
    public boolean isPressed(char key) {
        char charToCheck = this.bindings.map(key);
        return this.keyboardStateTracker.get(charToCheck);
    }
}

package me.darragh.jcvipe.desktop.input;

import me.darragh.jcvipe.desktop.window.Window;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class KeyboardStateTracker implements StateTracker {
    private final Map<Integer, Boolean> trackedStates = new HashMap<>();

    public void report(@NotNull Window.KeyState keyState) {
        this.trackedStates.put(keyState.keyCode(), keyState.isKeyDown());
    }

    public boolean get(int keyCode) {
        return this.trackedStates.getOrDefault(keyCode, false);
    }
}

package me.darragh.jcvipe.desktop.input;

import lombok.Getter;
import lombok.Setter;
import me.darragh.jcvipe.desktop.window.Window;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class MouseStateTracker implements StateTracker {
    private final Map<Integer, Boolean> trackedStates = new HashMap<>(); // buttons

    @Getter
    @Setter
    private int x, y; // cursor position
    @Getter
    @Setter
    private boolean hovered;

    public void reportButton(@NotNull Window.MouseButtonState state) {
        this.trackedStates.put(state.button(), state.isButtonPress());
    }

    public boolean getButton(int button) {
        return this.trackedStates.getOrDefault(button, false);
    }
}

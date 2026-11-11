package me.darragh.jcvipe.desktop.renderer.state.tracked;

import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL11C.*;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class CapabilityTrackedState implements TrackedState<Boolean> {
    private final int target;

    private boolean value;

    public CapabilityTrackedState(int target) {
        this.target = target;
        this.value = glIsEnabled(target);
    }

    @Override
    public @NotNull Boolean get() {
        return this.value;
    }

    @Override
    public void set(@NotNull Boolean value) {
        if (value != this.value) {
            this.value = value;
            if (value) {
                glEnable(this.target);
            } else {
                glDisable(this.target);
            }
        }
    }

    public void enable() {
        this.set(true);
    }

    public void disable() {
        this.set(false);
    }
}

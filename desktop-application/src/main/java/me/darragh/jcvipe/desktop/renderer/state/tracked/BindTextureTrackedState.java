package me.darragh.jcvipe.desktop.renderer.state.tracked;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL11C.glBindTexture;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@RequiredArgsConstructor
public final class BindTextureTrackedState implements TrackedState<Integer> {
    private final int target;

    private int value;

    @Override
    public @NotNull Integer get() {
        return this.value;
    }

    @Override
    public void set(@NotNull Integer value) {
        if (value != this.value) {
            this.value = value;
            glBindTexture(this.target, value);
        }
    }
}

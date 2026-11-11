package me.darragh.jcvipe.desktop.renderer.state.tracked;

import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL30C.glBindVertexArray;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class BindVertexArrayTrackedState implements TrackedState<Integer> {
    private int value;

    @Override
    public @NotNull Integer get() {
        return this.value;
    }

    @Override
    public void set(@NotNull Integer value) {
        if (value != this.value) {
            this.value = value;
            glBindVertexArray(value);
        }
    }
}

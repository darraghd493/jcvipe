package me.darragh.jcvipe.desktop.renderer.state.tracked;

import org.jetbrains.annotations.NotNull;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public interface TrackedState<T> {
    @NotNull T get();

    void set(@NotNull T value);
}

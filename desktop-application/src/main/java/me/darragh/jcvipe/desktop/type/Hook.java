package me.darragh.jcvipe.desktop.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class Hook<T> {
    private @Nullable Callback<T> callback;

    public void call(@NotNull T value) {
        if (this.callback != null) {
            this.callback.call(value);
        }
    }

    public void listen(@NotNull Callback<T> callback) {
        if (this.callback != null) {
            throw new IllegalStateException("Hook is already listening to a callback");
        }
        this.callback = callback;
    }

    @FunctionalInterface
    public interface Callback<T> {
        void call(@Nullable T value);
    }
}

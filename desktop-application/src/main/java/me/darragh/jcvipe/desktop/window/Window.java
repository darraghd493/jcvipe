package me.darragh.jcvipe.desktop.window;

import me.darragh.jcvipe.desktop.type.Hook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public interface Window {
    void init(int defaultWidth, int defaultHeight);

    void close();

    boolean shouldClose();

    //region Hooks
    @NotNull Hook<Size> getWindowSizeHook();

    @NotNull Hook<Size> getFramebufferSizeHook();

    @NotNull Hook<MouseMoveState> getMouseMoveHook();

    @NotNull Hook<MouseEnterState> getMouseEnterHook();

    @NotNull Hook<MouseScrollState> getMouseScrollHook();

    @NotNull Hook<MouseButtonState> getMouseButtonHook();

    @NotNull Hook<KeyState> getKeyHook();

    @NotNull Hook<TypeCharacterState> getTypeCharacterHook();

    @NotNull Hook<PathDropState> getPathDropHook();
    //endregion

    //region Triggers
    void swapFramebuffers();

    void pollEvents();
    //endregion

    //region Data Models
    record Size(int width, int height) {
    }

    interface MouseMoveState {
        double x();

        double y();
    }

    interface MouseEnterState {
        boolean entered();
    }

    interface MouseButtonState {
        int button();

        int action();

        int modifiers();

        //region Action
        boolean isButtonPress();

        boolean isButtonRelease();
        //endregion

        //region Modifiers
        boolean isWithShift();

        boolean isWithControl();

        boolean isWithAlt();

        boolean isWithSuper();

        boolean isWithCapsLock();

        boolean isWithNumLock();
        //endregion
    }

    interface MouseScrollState {
        double x();

        double y();
    }

    interface KeyState { // expected to be inherited by records
        int keyCode();

        @Nullable String keyName();

        int scanCode();

        int action();

        int modifiers();

        //region Action
        boolean isActionPress();

        boolean isActionRelease();

        boolean isActionRepeat();

        //region Physical
        default boolean isKeyUp() {
            return this.isActionRelease();
        }

        default boolean isKeyDown() {
            return this.isActionPress() || this.isActionRepeat();
        }
        //endregion
        //endregion

        //region Modifiers
        boolean isWithShift();

        boolean isWithControl();

        boolean isWithAlt();

        boolean isWithSuper();

        boolean isWithCapsLock();

        boolean isWithNumLock();
        //endregion
    }

    interface TypeCharacterState {
        @NotNull String character();
    }

    interface PathDropState {
        @NotNull String[] paths();
    }
    //endregion
}

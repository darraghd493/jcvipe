package me.darragh.jcvipe.desktop.window.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.darragh.jcvipe.desktop.type.Hook;
import me.darragh.jcvipe.desktop.window.Window;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.createPrint;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@Slf4j
@Getter
@NoArgsConstructor
public final class GlfwWindow implements Window {
    private long window;

    private final Hook<Size> windowSizeHook = new Hook<>();
    private final Hook<Size> framebufferSizeHook = new Hook<>();

    private final Hook<MouseMoveState> mouseMoveHook = new Hook<>();
    private final Hook<MouseEnterState> mouseEnterHook = new Hook<>();
    private final Hook<MouseScrollState> mouseScrollHook = new Hook<>();
    private final Hook<MouseButtonState> mouseButtonHook = new Hook<>();

    private final Hook<KeyState> keyHook = new Hook<>();
    private final Hook<TypeCharacterState> typeCharacterHook = new Hook<>();

    private final Hook<PathDropState> pathDropHook = new Hook<>();

    private int width, height;
    private int framebufferWidth, framebufferHeight;

    @SuppressWarnings({"resource", "CodeBlock2Expr"})
    @Override
    public void init(int defaultWidth, int defaultHeight) {
        log.info("Initializing GLFW window...");
        createPrint(System.err).set();

        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }

        // prepare window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        log.info("Creating window...");
        this.window = glfwCreateWindow(defaultWidth, defaultHeight, "jcvipe v1.0.0", NULL, NULL); // TODO: jcvipe v{version}-{hash}
        if (this.window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        // attach hooks
        log.info("Registering callbacks...");
        glfwSetWindowSizeCallback(this.window, (_, width, height) -> {
            this.width = width;
            this.height = height;
            this.windowSizeHook.call(new Size(width, height));
        });
        glfwSetFramebufferSizeCallback(this.window, (_, width, height) -> {
            this.framebufferWidth = width;
            this.framebufferHeight = height;
            this.framebufferSizeHook.call(new Size(width, height));
        });
        glfwSetCursorPosCallback(this.window, (_, x, y) -> {
            this.mouseMoveHook.call(new GlfwMouseMoveState(x, y));
        });
        glfwSetCursorEnterCallback(this.window, (_, entered) -> {
            this.mouseEnterHook.call(new GlfwMouseEnterState(entered));
        });
        glfwSetScrollCallback(this.window, (_, x, y) -> {
            this.mouseScrollHook.call(new GlfwMouseScrollState(x, y));
        });
        glfwSetMouseButtonCallback(this.window, (_, button, action, modifiers) -> {
            this.mouseButtonHook.call(new GlfwMouseButtonState(button, action, modifiers));
        });
        glfwSetKeyCallback(this.window, (_, key, scancode, action, modifiers) -> {
            this.keyHook.call(new GlfwKeyState(key, scancode, action, modifiers));
        });
        glfwSetCharCallback(this.window, (_, character) -> {
            this.typeCharacterHook.call(new GlfwTypeCharacterState(character));
        });
        glfwSetDropCallback(this.window, (_, count, paths) -> {
            String[] pathArray = new String[count];
            for (int i = 0; i < count; i++) {
                pathArray[i] = GLFWDropCallback.getName(paths, i);
            }
            this.pathDropHook.call(new GlfwPathDropState(pathArray));
        });

        // update w/h
        log.info("Finishing up...");

        int[] windowWidth = new int[1], windowHeight = new int[1],
                framebufferWidth = new int[1], framebufferHeight = new int[1];

        glfwGetWindowSize(this.window, windowWidth, windowHeight);
        glfwGetFramebufferSize(this.window, framebufferWidth, framebufferHeight);

        this.width = windowWidth[0];
        this.height = windowHeight[0];
        this.framebufferWidth = framebufferWidth[0];
        this.framebufferHeight = framebufferHeight[0];

        this.windowSizeHook.call(new Size(this.width, this.height));
        this.framebufferSizeHook.call(new Size(this.framebufferWidth, this.framebufferHeight));

        // contextualise opengl
        log.info("Contextualising OpenGL context...");

        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(1);
        glfwShowWindow(this.window);

        createCapabilities();
    }

    @Override
    public void close() {
        glfwFreeCallbacks(this.window);
        glfwDestroyWindow(this.window);

        glfwTerminate();
        //noinspection resource,DataFlowIssue
        glfwSetErrorCallback(null).free();
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(this.window);
    }

    @Override
    public void swapFramebuffers() {
        glfwSwapBuffers(window);
    }

    @Override
    public void pollEvents() {
        glfwPollEvents();
    }

    //region Data Models
    public record GlfwMouseMoveState(double x, double y) implements MouseMoveState {
    }

    public record GlfwMouseEnterState(boolean entered) implements MouseEnterState {
    }

    public record GlfwMouseScrollState(double x, double y) implements MouseScrollState {
    }

    public record GlfwMouseButtonState(int button, int action, int modifiers) implements MouseButtonState {
        //region Action
        @Override
        public boolean isButtonPress() {
            return this.action == GLFW_PRESS;
        }

        @Override
        public boolean isButtonRelease() {
            return this.action == GLFW_RELEASE;
        }
        //endregion

        //region Modifiers
        @Override
        public boolean isWithShift() {
            return (this.modifiers & GLFW_MOD_SHIFT) != 0;
        }

        @Override
        public boolean isWithControl() {
            return (this.modifiers & GLFW_MOD_CONTROL) != 0;
        }

        @Override
        public boolean isWithAlt() {
            return (this.modifiers & GLFW_MOD_ALT) != 0;
        }

        @Override
        public boolean isWithSuper() {
            return (this.modifiers & GLFW_MOD_SUPER) != 0;
        }

        @Override
        public boolean isWithCapsLock() {
            return (this.modifiers & GLFW_MOD_CAPS_LOCK) != 0;
        }

        @Override
        public boolean isWithNumLock() {
            return (this.modifiers & GLFW_MOD_NUM_LOCK) != 0;
        }
        //endregion
    }

    public record GlfwKeyState(int keyCode, int scanCode, int action, int modifiers) implements KeyState {
        @Override
        public @Nullable String keyName() {
            return glfwGetKeyName(this.keyCode, this.scanCode);
        }

        //region Action
        @Override
        public boolean isActionPress() {
            return this.action == GLFW_PRESS;
        }

        @Override
        public boolean isActionRelease() {
            return this.action == GLFW_RELEASE;
        }

        @Override
        public boolean isActionRepeat() {
            return this.action == GLFW_REPEAT;
        }
        //endregion

        //region Modifiers
        @Override
        public boolean isWithShift() {
            return (this.modifiers & GLFW_MOD_SHIFT) != 0;
        }

        @Override
        public boolean isWithControl() {
            return (this.modifiers & GLFW_MOD_CONTROL) != 0;
        }

        @Override
        public boolean isWithAlt() {
            return (this.modifiers & GLFW_MOD_ALT) != 0;
        }

        @Override
        public boolean isWithSuper() {
            return (this.modifiers & GLFW_MOD_SUPER) != 0;
        }

        @Override
        public boolean isWithCapsLock() {
            return (this.modifiers & GLFW_MOD_CAPS_LOCK) != 0;
        }

        @Override
        public boolean isWithNumLock() {
            return (this.modifiers & GLFW_MOD_NUM_LOCK) != 0;
        }
        //endregion
    }

    public record GlfwTypeCharacterState(@NotNull String character) implements TypeCharacterState {
        public GlfwTypeCharacterState(int character) {
            this(String.valueOf(character));
        }
    }

    public record GlfwPathDropState(@NotNull String[] paths) implements PathDropState {
    }
    //endregion
}

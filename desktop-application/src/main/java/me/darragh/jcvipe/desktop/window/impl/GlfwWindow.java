package me.darragh.jcvipe.desktop.window.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.darragh.jcvipe.desktop.type.Hook;
import me.darragh.jcvipe.desktop.window.Window;
import org.jspecify.annotations.Nullable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.createPrint;
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
    private final Hook<KeyState> keyStateHook = new Hook<>();

    private int width, height;
    private int framebufferWidth, framebufferHeight;

    @SuppressWarnings("resource")
    @Override
    public void init(int defaultWidth, int defaultHeight) {
        createPrint(System.err).set();

        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }

        // prepare window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        this.window = glfwCreateWindow(defaultWidth, defaultHeight, "jcvipe v1.0.0", NULL, NULL); // TODO: jcvipe v{version}-{hash}
        if (this.window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        // attach hooks
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
        glfwSetKeyCallback(this.window, (_, key, scancode, action, modifiers) -> {
            this.keyStateHook.call(new KeyState(key, scancode, action, modifiers));
        });

        // update w/h
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
        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(1);
        glfwShowWindow(this.window);
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
    public record Size(int width, int height) {
    }

    public record KeyState(int keyCode, int scanCode, int action, int modifiers) {
        public @Nullable String keyName() {
            return glfwGetKeyName(this.keyCode, this.scanCode);
        }

        //region Action
        public boolean isPress() {
            return this.action == GLFW_PRESS;
        }

        public boolean isRelease() {
            return this.action == GLFW_RELEASE;
        }

        public boolean isRepeat() {
            return this.action == GLFW_REPEAT;
        }

        //region Physical
        public boolean isKeyUp() {
            return this.isRelease();
        }

        public boolean isKeyDown() {
            return this.isPress() || this.isRepeat();
        }
        //endregion
        //endregion

        //region Modifiers
        public boolean isWithShift() {
            return (this.modifiers & GLFW_MOD_SHIFT) != 0;
        }

        public boolean isWithControl() {
            return (this.modifiers & GLFW_MOD_CONTROL) != 0;
        }

        public boolean isWithAlt() {
            return (this.modifiers & GLFW_MOD_ALT) != 0;
        }

        public boolean isWithSuper() {
            return (this.modifiers & GLFW_MOD_SUPER) != 0;
        }

        public boolean isWithCapsLock() {
            return (this.modifiers & GLFW_MOD_CAPS_LOCK) != 0;
        }

        public boolean isWithNumLock() {
            return (this.modifiers & GLFW_MOD_NUM_LOCK) != 0;
        }
        //endregion
    }
    //endregion
}

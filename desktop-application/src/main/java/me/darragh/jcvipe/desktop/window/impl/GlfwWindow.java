package me.darragh.jcvipe.desktop.window.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.darragh.jcvipe.desktop.window.Window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.createPrint;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@Slf4j
@NoArgsConstructor
public final class GlfwWindow implements Window {
    private long window;

    @Override
    public void init(int defaultWidth, int defaultHeight) {
        createPrint(System.err).set();

        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        this.window = glfwCreateWindow(defaultWidth, defaultHeight, "jcvipe v1.0.0", NULL, NULL); // TODO: jcvipe v{version}-{hash}
        if (this.window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

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
    public void pollEvents() {
        // no-op
    }
}

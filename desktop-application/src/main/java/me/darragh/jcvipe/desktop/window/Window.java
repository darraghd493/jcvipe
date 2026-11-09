package me.darragh.jcvipe.desktop.window;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public interface Window {
    void init(int defaultWidth, int defaultHeight);

    void close();

    boolean shouldClose();

    void pollEvents();
}

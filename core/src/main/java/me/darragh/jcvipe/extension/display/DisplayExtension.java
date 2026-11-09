package me.darragh.jcvipe.extension.display;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public interface DisplayExtension {
    void clear();

    /**
     * This method expects the final state of each pixel (post-DXYN).
     */
    void draw(int x, int y, int rowCount, boolean[] flags  /* 8 pixels long, 8*rowCount */);
}

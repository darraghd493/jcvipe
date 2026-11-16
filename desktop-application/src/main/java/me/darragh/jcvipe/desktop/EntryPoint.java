package me.darragh.jcvipe.desktop;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class EntryPoint {
    public static final DesktopApplication APPLICATION = new DesktopApplication(); // TODO: Pass down via inheritance/contructors rather than referencing a static class

    static void main() {
        APPLICATION.start();
    }
}

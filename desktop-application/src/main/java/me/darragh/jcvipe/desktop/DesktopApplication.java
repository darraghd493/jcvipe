package me.darragh.jcvipe.desktop;

import lombok.extern.slf4j.Slf4j;
import me.darragh.jcvipe.desktop.window.impl.GlfwWindow;
import me.darragh.jcvipe.extension.display.impl.StateHoldingDisplayExtension;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@Slf4j
public final class DesktopApplication {
    private GlfwWindow window;

    public void start() {
        log.info("Starting desktop application...");

        this.window = new GlfwWindow();
        this.window.init(
                StateHoldingDisplayExtension.DEFAULT_WIDTH * 16,
                StateHoldingDisplayExtension.DEFAULT_HEIGHT * 16
        );

        this.loop();
    }

    public void loop() {
        while (!this.window.shouldClose()) {
            this.window.pollEvents();
        }
    }
}

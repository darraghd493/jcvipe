package me.darragh.jcvipe.desktop.extension;

import lombok.experimental.UtilityClass;
import me.darragh.jcvipe.desktop.DesktopApplication;
import me.darragh.jcvipe.desktop.configuration.Configuration;
import me.darragh.jcvipe.desktop.extension.display.DesktopApplicationDisplayExtension;
import me.darragh.jcvipe.desktop.extension.input.DesktopApplicationInputExtension;
import me.darragh.jcvipe.desktop.scene.impl.EmulatorScene;
import me.darragh.jcvipe.extension.Extension;
import me.darragh.jcvipe.extension.ExtensionCollection;
import me.darragh.jcvipe.extension.sound.impl.StubSoundExtension;
import me.darragh.jcvipe.extension.timer.impl.JavaTimerExtension;
import org.jetbrains.annotations.NotNull;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@UtilityClass
public final class DesktopApplicationExtensionCollection {
    public static @NotNull ExtensionCollection collection(
            @NotNull Configuration configuration,
            @NotNull DesktopApplication application
    ) {
        return new ExtensionCollection(
                new DesktopApplicationInputExtension(configuration.getBindings(), application.getKeyboardStateTracker()),
                new StubSoundExtension(), // TODO
                new DesktopApplicationDisplayExtension(),
                new JavaTimerExtension()
        );
    }
}

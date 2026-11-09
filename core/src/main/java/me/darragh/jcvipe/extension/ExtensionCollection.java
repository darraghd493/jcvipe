package me.darragh.jcvipe.extension;

import me.darragh.jcvipe.extension.display.DisplayExtension;
import me.darragh.jcvipe.extension.display.impl.StubDisplayExtension;
import me.darragh.jcvipe.extension.input.InputExtension;
import me.darragh.jcvipe.extension.input.impl.StubInputExtension;
import me.darragh.jcvipe.extension.sound.SoundExtension;
import me.darragh.jcvipe.extension.sound.impl.StubSoundExtension;
import me.darragh.jcvipe.extension.timer.TimerExtension;
import me.darragh.jcvipe.extension.timer.impl.JavaTimerExtension;
import org.jetbrains.annotations.NotNull;

/**
 * Stores a set of all extensions.
 *
 * @author darraghd493
 * @since 0.0.1
 */
public record ExtensionCollection(
        @NotNull InputExtension inputExtension,
        @NotNull SoundExtension soundExtension,
        @NotNull DisplayExtension displayExtension,
        @NotNull TimerExtension timerExtension
) {
    public static final ExtensionCollection STUB_COLLECTION = new ExtensionCollection(
            new StubInputExtension(),
            new StubSoundExtension(),
            new StubDisplayExtension(),
            new JavaTimerExtension() // enables functionality for testing
    );
}

package me.darragh.jcvipe.extension.timer.impl;

import me.darragh.jcvipe.extension.timer.TimerExtension;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class JavaTimerExtension implements TimerExtension {
    @Override
    public long getTimeNs() {
        return System.nanoTime();
    }

    @Override
    public long getTimeMs() {
        return System.currentTimeMillis();
    }
}

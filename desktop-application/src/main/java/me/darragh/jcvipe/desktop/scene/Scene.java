package me.darragh.jcvipe.desktop.scene;

import lombok.RequiredArgsConstructor;
import me.darragh.jcvipe.desktop.renderer.context.NanoVGContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@RequiredArgsConstructor
public abstract class Scene implements Renderable {
    protected final @NotNull NanoVGContext vg;
}

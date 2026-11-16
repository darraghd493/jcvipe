package me.darragh.jcvipe.desktop.extension.display;

import lombok.Getter;
import me.darragh.jcvipe.desktop.type.Hook;
import me.darragh.jcvipe.extension.display.impl.StateHoldingDisplayExtension;
import org.jetbrains.annotations.NotNull;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@Getter
public final class DesktopApplicationDisplayExtension extends StateHoldingDisplayExtension {
    private final @NotNull Hook<UpdateState> updateHook = new Hook<>();

    @Override
    protected void onUpdate(boolean[] pixels) {
        this.updateHook.call(new UpdateState(this.width, this.height, pixels));
    }

    public record UpdateState(int width, int height, boolean[] pixels) {
    }
}

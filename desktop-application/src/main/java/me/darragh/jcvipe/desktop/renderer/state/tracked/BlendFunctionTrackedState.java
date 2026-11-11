package me.darragh.jcvipe.desktop.renderer.state.tracked;

import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL11C.GL_ONE;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL14C.GL_ZERO;
import static org.lwjgl.opengl.GL14C.glBlendFuncSeparate;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public final class BlendFunctionTrackedState implements TrackedState<BlendFunctionTrackedState.ProvidedArithmetic> {
    private ProvidedArithmetic value = new Arithmetic(GL_ONE, GL_ZERO);
    private int value$hashCode = this.value.hashCode();

    @Override
    public @NotNull ProvidedArithmetic get() {
        return this.value;
    }

    @Override
    public void set(@NotNull ProvidedArithmetic value) {
        if (this.value$hashCode != value.hashCode()) {
            this.value = value;
            this.value$hashCode = value.hashCode();

            if (value.isSeparate()) {
                assert value instanceof SeparateArithmetic;
                SeparateArithmetic separate = (SeparateArithmetic) value;
                glBlendFuncSeparate(separate.rgbSource(), separate.rgbDestination(), separate.alphaSource(), separate.alphaDestination());
            } else {
                assert value instanceof Arithmetic;
                Arithmetic arithmetic = (Arithmetic) value;
                glBlendFunc(arithmetic.source(), arithmetic.destination());
            }
        }
    }

    //region Arithmetic
    public sealed interface ProvidedArithmetic permits Arithmetic, SeparateArithmetic {
        boolean isSeparate();

        static int hash(int rgbSource, int rgbDestination, int alphaSource, int alphaDestination) {
            long packed = ((rgbSource & 0xFFFFL) << 48) |
                    ((rgbDestination & 0xFFFFL) << 32) |
                    ((alphaSource & 0xFFFFL) << 16) |
                    (alphaDestination & 0xFFFFL);
            return Long.hashCode(packed);
        }
    }

    public record Arithmetic(int source, int destination) implements ProvidedArithmetic {
        @Override
        public boolean isSeparate() {
            return false;
        }

        @Override
        public int hashCode() {
            return ProvidedArithmetic.hash(this.source, this.destination, this.source, this.destination);
        }
    }

    public record SeparateArithmetic(int rgbSource, int rgbDestination, int alphaSource, int alphaDestination) implements ProvidedArithmetic {
        @Override
        public boolean isSeparate() {
            return true;
        }

        @Override
        public int hashCode() {
            return ProvidedArithmetic.hash(this.rgbSource, this.rgbDestination, this.alphaSource, this.alphaDestination);
        }
    }
    //endregion
}

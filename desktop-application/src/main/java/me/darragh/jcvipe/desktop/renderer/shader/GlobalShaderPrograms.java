package me.darragh.jcvipe.desktop.renderer.shader;

import lombok.experimental.UtilityClass;
import me.darragh.jcvipe.desktop.renderer.shader.impl.TextureShaderProgram;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@UtilityClass
public final class GlobalShaderPrograms {
    public static final TextureShaderProgram TEXTURE = new TextureShaderProgram();

    public static void dispose() {
        TEXTURE.dispose();
    }
}

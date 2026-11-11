package me.darragh.jcvipe.desktop.renderer.state;

import lombok.experimental.UtilityClass;
import me.darragh.jcvipe.desktop.renderer.state.tracked.*;

import static org.lwjgl.opengl.GL30C.*;

/**
 * Manages the active OpenGL state.
 *
 * @author darraghd493
 * @since 0.0.1
 */
@UtilityClass
public final class StateManager {
    public static final BindTextureTrackedState TEXTURE = new BindTextureTrackedState(GL_TEXTURE_2D);
    public static final BindFramebufferTrackedState FRAMEBUFFER = new BindFramebufferTrackedState(GL_FRAMEBUFFER);
    public static final BindVertexArrayTrackedState VERTEX_ARRAY = new BindVertexArrayTrackedState();

    public static final CapabilityTrackedState DEPTH = new CapabilityTrackedState(GL_DEPTH_TEST);
    public static final CapabilityTrackedState STENCIL = new CapabilityTrackedState(GL_STENCIL_TEST);
    public static final CapabilityTrackedState CULL_FACE = new CapabilityTrackedState(GL_CULL_FACE);

    public static final CapabilityTrackedState BLEND = new CapabilityTrackedState(GL_BLEND);
    public static final BlendFunctionTrackedState BLEND_FUNCTION = new BlendFunctionTrackedState();
}

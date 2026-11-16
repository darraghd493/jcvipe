package me.darragh.jcvipe.desktop.renderer.context;

import lombok.extern.slf4j.Slf4j;
import me.darragh.jcvipe.desktop.utility.ResourceUtility;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;

import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.nanovg.NanoVG.*;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@Slf4j
public record NanoVGContext(long ptr) {
    public static final String FONT_SANS_REGULAR = "Sans-Regular";
    public static final String FONT_SANS_MEDIUM = "Sans-Medium";
    public static final String FONT_SANS_BOLD = "Sans-Bold";


    public static @NotNull NanoVGContext create() {
        log.info("Creating NVG context...");
        return new NanoVGContext(
                nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES)
        );
    }

    public void close() {
        log.info("Closing NVG context...");
        nvgDelete(this.ptr);
    }

    //region Wrapper
    //region State Management
    public void save() {
        nvgSave(this.ptr);
    }

    public void restore() {
        nvgRestore(this.ptr);
    }

    public void reset() {
        nvgReset(this.ptr);
    }
    //endregion

    //region Transforms
    public void resetTransform() {
        nvgResetTransform(this.ptr);
    }

    public void translate(float x, float y) {
        nvgTranslate(this.ptr, x, y);
    }

    public void rotate(float angle) {
        nvgRotate(this.ptr, angle);
    }

    public void scale(float x, float y) {
        nvgScale(this.ptr, x, y);
    }

    public void skewX(float angle) {
        nvgSkewX(this.ptr, angle);
    }

    public void skewY(float angle) {
        nvgSkewY(this.ptr, angle);
    }
    //endregion

    //region Styling
    public void strokeColour(@NotNull NVGColor colour) {
        nvgStrokeColor(this.ptr, colour);
    }

    public void strokePaint(@NotNull NVGPaint paint) {
        nvgStrokePaint(this.ptr, paint);
    }

    public void fillColour(@NotNull NVGColor colour) {
        nvgFillColor(this.ptr, colour);
    }

    public void fillPaint(@NotNull NVGPaint paint) {
        nvgFillPaint(this.ptr, paint);
    }

    public void miterLimit(float limit) {
        nvgMiterLimit(this.ptr, limit);
    }

    public void strokeWidth(float size) {
        nvgStrokeWidth(this.ptr, size);
    }

    public void lineCap(int cap) {
        nvgLineCap(this.ptr, cap);
    }

    public void lineJoin(int join) {
        nvgLineJoin(this.ptr, join);
    }

    public void globalAlpha(float alpha) {
        nvgGlobalAlpha(this.ptr, alpha);
    }
    //endregion

    //region Pathing
    public void beginPath() {
        nvgBeginPath(this.ptr);
    }

    public void closePath() {
        nvgClosePath(this.ptr);
    }

    public void moveTo(float x, float y) {
        nvgMoveTo(this.ptr, x, y);
    }

    public void lineTo(float x, float y) {
        nvgLineTo(this.ptr, x, y);
    }

    public void quadTo(float cx, float cy, float x, float y) {
        nvgQuadTo(this.ptr, cx, cy, x, y);
    }

    public void bezierTo(float c1x, float c1y, float c2x, float c2y, float x, float y) {
        nvgBezierTo(this.ptr, c1x, c1y, c2x, c2y, x, y);
    }

    public void arcTo(float x1, float y1, float x2, float y2, float radius) {
        nvgArcTo(this.ptr, x1, y1, x2, y2, radius);
    }

    public void arc(float cx, float cy, float r, float a0, float a1, int dir) {
        nvgArc(this.ptr, cx, cy, r, a0, a1, dir);
    }

    //region Objects
    public void circle(float x, float y, float radius) {
        nvgCircle(this.ptr, x, y, radius);
    }

    public void ellipse(float cx, float cy, float rx, float ry) {
        nvgEllipse(this.ptr, cx, cy, rx, ry);
    }

    public void rectangle(float x, float y, float width, float height) {
        nvgRect(this.ptr, x, y, width, height);
    }

    public void roundedRectangle(float x, float y, float width, float height, float radius) {
        nvgRoundedRect(this.ptr, x, y, width, height, radius);
    }

    public void roundedRectangle(float x, float y, float width, float height,
                                 float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft) {
        nvgRoundedRectVarying(this.ptr, x, y, width, height, radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft);
    }
    //endregion
    //endregion

    //region Drawing
    public void fill() {
        nvgFill(this.ptr);
    }

    public void stroke() {
        nvgStroke(this.ptr);
    }
    //endregion

    //region Font
    public int createFont(@NotNull String name, @NotNull String resourcePath) {
        return ResourceUtility.getAsBytes(resourcePath).map(bytes -> {
            ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            return nvgCreateFontMem(this.ptr, name, buffer, false);
        }).orElse(-1);
    }

    public void fontSize(float size) {
        nvgFontSize(this.ptr, size);
    }

    public void fontBlur(float blur) {
        nvgFontBlur(this.ptr, blur);
    }

    public void fontFace(@NotNull CharSequence font) {
        nvgFontFace(this.ptr, font);
    }

    public void fontFaceId(int fontId) {
        nvgFontFaceId(this.ptr, fontId);
    }
    //endregion

    //region Text
    public void textLetterSpacing(float spacing) {
        nvgTextLetterSpacing(this.ptr, spacing);
    }

    public void textLineHeight(float lineHeight) {
        nvgTextLineHeight(this.ptr, lineHeight);
    }

    public void textAlign(int align) {
        nvgTextAlign(this.ptr, align);
    }

    public void text(float x, float y, CharSequence string) {
        nvgText(this.ptr, x, y, string);
    }

    public void textBox(float x, float y, float breakRowWidth, CharSequence string) {
        nvgTextBox(this.ptr, x, y, breakRowWidth, string);
    }
    //endregion

    //region Scissor
    public void scissor(float x, float y, float w, float h) {
        nvgScissor(this.ptr, x, y, w, h);
    }

    public void intersectScissor(float x, float y, float w, float h) {
        nvgIntersectScissor(this.ptr, x, y, w, h);
    }

    public void resetScissor() {
        nvgResetScissor(this.ptr);
    }
    //endregion

    //region Frame
    public void beginFrame(int width, int height) {
        this.beginFrame(width, height, 1f);
    }

    public void beginFrame(int width, int height, float devicePixelRatio) {
        nvgBeginFrame(this.ptr, width, height, devicePixelRatio);
    }

    public void cancelFrame() {
        nvgCancelFrame(this.ptr);
    }

    public void endFrame() {
        nvgEndFrame(this.ptr);
    }
    //endregion
    //endregion
}
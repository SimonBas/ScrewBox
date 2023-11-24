package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.*;

import java.util.function.Supplier;

public class StandbyRenderer implements Renderer {

    @Override
    public void updateScreen(final boolean antialiased) {
        // does nothing
    }

    @Override
    public Sprite takeScreenshot() {
        return null;
    }

    @Override
    public void fillWith(final Color color) {
        // does nothing
    }

    @Override
    public void fillRectangle(final ScreenBounds bounds, final Color color) {
        // does nothing
    }

    @Override
    public void fillCircle(final Offset offset, final int diameter, final Color color) {
        // does nothing
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percent opacity,
                           final Rotation rotation, final Flip flip, final ScreenBounds clipArea) {
        // does nothing
    }

    @Override
    public void drawText(final Offset offset, final String text, final Font font, final Color color) {
        // does nothing
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final Color color) {
        // does nothing
    }

    @Override
    public void drawTextCentered(final Offset position, final String text, final Font font, final Color color) {
        // does nothing
    }

    @Override
    public void drawFadingCircle(final Offset offset, final int diameter, final Color color) {
        // does nothing
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final double scale, final Percent opacity,
                           final Rotation rotation,
                           final Flip flip, final ScreenBounds clipArea) {
        // does nothing
    }

    @Override
    public void drawCircle(final Offset offset, final int diameter, final Color color) {
        // does nothing
    }

}

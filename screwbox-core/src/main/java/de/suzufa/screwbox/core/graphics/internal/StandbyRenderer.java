package de.suzufa.screwbox.core.graphics.internal;

import java.util.List;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;

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
    public void drawRectangle(final WindowBounds bounds, final Color color) {
        // does nothing
    }

    @Override
    public void drawCircle(final Offset offset, final int diameter, final Color color) {
        // does nothing
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percentage opacity,
            final Rotation rotation, final FlipMode flipMode) {
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
    public void drawPolygon(final List<Offset> points, final Color color) {
        // does nothing
    }

    @Override
    public void drawTextCentered(final Offset position, final String text, final Font font, final Color color) {
        // does nothing
    }

}

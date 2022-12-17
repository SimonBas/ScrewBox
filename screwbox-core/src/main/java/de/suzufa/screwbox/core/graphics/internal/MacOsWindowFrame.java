package de.suzufa.screwbox.core.graphics.internal;

import java.awt.GraphicsDevice;
import java.awt.Window;

public class MacOsWindowFrame extends WindowFrame {

    private static final long serialVersionUID = 1L;

    @Override
    public void makeFullscreen(final GraphicsDevice graphicsDevice) {
        try {
            final var screenUtils = Class.forName("com.apple.eawt.FullScreenUtilities");
            final var canFullscreenMethod = screenUtils.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE);
            canFullscreenMethod.invoke(screenUtils, this, true);

            final var application = Class.forName("com.apple.eawt.Application");
            final var fullScreenMethod = application.getMethod("requestToggleFullScreen", Window.class);
            fullScreenMethod.invoke(application.getConstructor().newInstance(), this);

        } catch (final Exception e) {
            throw new IllegalStateException(
                    "Please add jvm parameters to allow native fullscreen on MacOs: --add-opens java.desktop/com.apple.eawt=ALL-UNNAMED",
                    e);
        }
    }
}

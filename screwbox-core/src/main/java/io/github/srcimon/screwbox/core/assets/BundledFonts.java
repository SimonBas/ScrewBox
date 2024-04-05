package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.Cache;

import java.util.Arrays;

import static io.github.srcimon.screwbox.core.graphics.Color.BLACK;

//TODO implement rest of #191 BundledSounds
//TODO implement rest of #191 BundledSprites
//TODO Test and Javadoc
public enum BundledFonts implements BundledAsset<Pixelfont> {

    SCREWBOX(Asset.asset(() -> loadFont("assets/pixelfonts/default_font.png", Size.square(8),
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '.', ',', ':', '!', '?', '-'))),

    SKINNY_SANS(Asset.asset(() -> loadFont("assets/pixelfonts/skinny_sans.png", Size.square(8),
            'A', 'a', 'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f', 'G', 'g', 'H', 'h', 'I', 'i', 'J', 'j', 'K', 'k', 'L', 'l', 'M', 'm', 'N', 'n', 'O', 'o', 'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't', 'U', 'u', 'V', 'v', 'W', 'w', 'X', 'x', 'Y', 'y', 'Z', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '.', ',', ':', '!', '?', '-')));

    private final Cache<Color, Pixelfont> cache = new Cache<>();
    private final Asset<Pixelfont> asset;

    BundledFonts(final Asset<Pixelfont> supplier) {
        this.asset = supplier;
    }

    public Pixelfont white() {
        return customColor(Color.WHITE);
    }

    public Pixelfont customColor(final Color color) {
        return cache.getOrElse(color, () -> asset.get().replaceColor(BLACK, color));
    }

    private static Pixelfont loadFont(final String resouce, final Size size, final Character... characters) {
        final Pixelfont font = new Pixelfont();
        final var sprites = Sprite.multipleFromFile(resouce, size);
        font.addCharacters(Arrays.asList(characters), sprites.stream().map(Sprite::cropHorizontal).toList());
        return font;
    }


    @Override
    public Asset<Pixelfont> asset() {
        return asset;
    }
}
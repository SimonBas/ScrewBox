package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.filter.BlurImageFilter;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.DefaultRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@Order(SystemOrder.PRESENTATION_OVERLAY)
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(ReflectionComponent.class, TransformComponent.class);
    private static final Archetype RELECTED_ENTITIES = Archetype.of(TransformComponent.class, RenderComponent.class);

    private static double adjustSmaller(final double value) {
        return Math.floor(value / 16) * 16;
    }

    private static double adjustGreater(final double value) {
        return Math.ceil(value / 16) * 16;
    }

    @Override
    public void update(final Engine engine) {
        final List<Entity> oldReflections = engine.environment().fetchAll(Archetype.of(ReflectionImageComponent.class));
        engine.environment().remove(oldReflections);
        double zoom = engine.graphics().camera().zoom();
        var reflectableEntities = engine.environment().fetchAll(RELECTED_ENTITIES);
        for (final Entity reflectionEntity : engine.environment().fetchAll(REFLECTING_AREAS)) {

            Bounds visibleArea = engine.graphics().world().visibleArea();
            double x = adjustSmaller(visibleArea.origin().x());
            double y = adjustSmaller(visibleArea.origin().y());
            double xdelte = visibleArea.origin().x() - x;
            double ydelte = visibleArea.origin().y() - y;
            Bounds visibleAreaAdjusted = Bounds.atOrigin(
                    x,
                    y,
                    adjustGreater(visibleArea.width() + xdelte),
                    adjustGreater(visibleArea.height() + ydelte));
            final var xxxx = reflectionEntity.bounds().intersection(visibleAreaAdjusted);

            xxxx.ifPresent(reflection -> {
                var reflectionOnScreen = engine.graphics().toScreen(reflection);

                var reflectedArea = reflection.moveBy(Vector.y(-reflection.height()));

                ReflectionComponent reflectionComponent = reflectionEntity.get(ReflectionComponent.class);

                int width = (int) (Math.ceil(reflectionOnScreen.size().width() / zoom));
                int height = (int) (Math.ceil(reflectionOnScreen.size().height() / zoom));

                if (width > 0 && height > 0) {
                    var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    var graphics = (Graphics2D) image.getGraphics();

                    var renderer = new DefaultRenderer();
                    renderer.updateGraphicsContext(() -> graphics, Size.of(width, height));
                    for (var entity : reflectableEntities) {
                        var render = entity.get(RenderComponent.class);

                        Bounds entityRenderArea = Bounds.atPosition(entity.bounds().position(),
                                reflectionEntity.bounds().width() * render.options.scale() ,
                                reflectionEntity.bounds().height() * render.options.scale());

                        ScreenBounds screenUsingParallax = engine.graphics().toScreenUsingParallax(entity.bounds(), render.parallaxX, render.parallaxY);

                        if (screenUsingParallax.intersects(engine.graphics().toScreen(reflectedArea))) {

                            if (render.drawOrder <= reflectionComponent.drawOrder) {
                                var eos = screenUsingParallax;
                                var ldist = eos.center().substract(engine.graphics().toScreen(reflectedArea).offset());
                                var ldistOffset = Offset.at(
                                        ldist.x() / zoom - render.sprite.size().width() * render.options.scale() / 2,
                                        height - ldist.y() / zoom - render.sprite.size().height() * render.options.scale() / 2
                                );

                                renderer.drawSprite(render.sprite, ldistOffset, render.options.invertVerticalFlip());
                            }
                        }


                    }

                    graphics.dispose();

                    Sprite reflectionSprite = reflectionComponent.blur > 1
                            ? Sprite.fromImage(new BlurImageFilter(reflectionComponent.blur).apply(image))
                            : Sprite.fromImage(image);
                    RenderComponent renderComponent = new RenderComponent(
                            reflectionSprite,
                            reflectionComponent.drawOrder,
                            SpriteDrawOptions.originalSize().opacity(reflectionComponent.opacityModifier)
                    );
                    engine.environment().addEntity(
                            new TransformComponent(reflection),
                            renderComponent,
                            new ReflectionImageComponent()
                    );
                }
            });
        }

    }

}

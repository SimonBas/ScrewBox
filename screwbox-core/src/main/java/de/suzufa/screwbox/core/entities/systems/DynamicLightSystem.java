package de.suzufa.screwbox.core.entities.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.LightBlockingComponent;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Lightmap;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.physics.RaycastBuilder;

public class DynamicLightSystem implements EntitySystem {

    // TODO: What classes to see? / make lightmap part of the engine.graphics() /
    // async rendering of image
    private static final Archetype POINTLIGHT_EMITTERS = Archetype.of(
            PointLightComponent.class, TransformComponent.class);

    private static final Archetype LIGHT_BLOCKING = Archetype.of(
            LightBlockingComponent.class, TransformComponent.class);

    private final double raycastAngle;

    private int resolution;

    public DynamicLightSystem() {
        this(3, 4);
    }

    public DynamicLightSystem(final double raycastAngle, int resolution) {
        this.raycastAngle = raycastAngle;
        this.resolution = resolution;
    }

    @Override
    public void update(final Engine engine) {
        try (final Lightmap lightmap = new Lightmap(engine.graphics().window().size(), resolution)) {

            for (final Entity pointLightEntity : engine.entities().fetchAll(POINTLIGHT_EMITTERS)) {
                final PointLightComponent pointLight = pointLightEntity.get(PointLightComponent.class);
                final Vector pointLightPosition = pointLightEntity.get(TransformComponent.class).bounds.position();
                final Offset offset = engine.graphics().windowPositionOf(pointLightPosition);
                final int range = (int) (pointLight.range / engine.graphics().cameraZoom());
                final List<Offset> area = new ArrayList<>();

                RaycastBuilder raycast = engine.physics().raycastFrom(pointLightPosition).checkingFor(LIGHT_BLOCKING);
                for (double degrees = 0; degrees < 360; degrees += raycastAngle) {
                    // TODO: make utility method in Angle for this:
                    double radians = Angle.ofDegrees(degrees).radians();
                    Vector raycastEnd = Vector.$(
                            pointLightPosition.x() + (range * Math.sin(radians)),
                            pointLightPosition.y() + (range * -Math.cos(radians)));

                    Optional<Vector> hit = raycast.castingTo(raycastEnd).nearestHit();
                    Vector endpoint = hit.isPresent() ? hit.get() : raycastEnd;
                    area.add(engine.graphics().windowPositionOf(endpoint));
                }
                lightmap.addPointLight(offset, range, area);

            }
            Sprite createImage = lightmap.createImage();
            engine.graphics().window().drawSprite(createImage, Offset.origin(), resolution, Percentage.max(),
                    Angle.none());
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_LIGHT;
    }

}

package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

import static java.util.Objects.nonNull;

public class MovementPathSystem implements EntitySystem {

    private static final Archetype AUTO_MOVERS = Archetype.of(MovementPathComponent.class, PhysicsComponent.class,
            TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity mover : engine.environment().fetchAll(AUTO_MOVERS)) {
            final var automovement = mover.get(MovementPathComponent.class);
            if (nonNull(automovement.path)) {
                if (mover.position().distanceTo(automovement.path.lastNode()) < 1) {
                    mover.get(PhysicsComponent.class).momentum = Vector.zero();
                    mover.remove(MovementTargetComponent.class);
                } else {
                    if (automovement.path.nodeCount() > 1 && mover.position().distanceTo(automovement.path.firstNode()) < mover.bounds().extents().length()) {
                        automovement.path = automovement.path.removeNode(0);
                    }
                    mover.remove(MovementTargetComponent.class);
                    mover.addCustomized(new MovementTargetComponent(automovement.path.firstNode()), target -> {
                        target.acceleration = automovement.acceleration;
                        target.maxSpeed = automovement.speed;
                    });
                }
            }
        }
    }
}

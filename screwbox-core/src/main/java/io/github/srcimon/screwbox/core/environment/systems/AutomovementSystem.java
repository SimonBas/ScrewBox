package io.github.srcimon.screwbox.core.environment.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.components.AutomovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.RigidBodyComponent;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;

public class AutomovementSystem implements EntitySystem {

    private static final Archetype AUTO_MOVERS = Archetype.of(AutomovementComponent.class, RigidBodyComponent.class,
            TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity mover : engine.environment().fetchAll(AUTO_MOVERS)) {
            final var automovement = mover.get(AutomovementComponent.class);
            if (automovement.path != null) {
                final Vector position = mover.get(TransformComponent.class).bounds.position();
                if (position.distanceTo(automovement.path.end()) < 1) {
                    mover.get(RigidBodyComponent.class).momentum = Vector.zero();
                } else {
                    if (automovement.path.nodeCount() > 1 && position.distanceTo(automovement.path.start()) < 1) {
                        automovement.path.removeNode(0);
                    }
                    final Vector direction = automovement.path.start().substract(position);
                    mover.get(RigidBodyComponent.class).momentum = direction.adjustLengthTo(automovement.speed);
                }
            }
        }

    }

}

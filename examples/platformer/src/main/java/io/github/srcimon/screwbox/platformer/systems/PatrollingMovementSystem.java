package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.platformer.components.PatrollingMovementComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

public class PatrollingMovementSystem implements EntitySystem {

    private static final Archetype PATROLLING = Archetype.of(PatrollingMovementComponent.class,
            PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(PATROLLING)) {
            final var physicsBodyComponent = entity.get(PhysicsComponent.class);
            final var patrollingMovementComponent = entity.get(PatrollingMovementComponent.class);

            if (isOnEdge(engine, entity) || spotsWall(engine, entity)) {
                invertSpeed(entity);
            }

            physicsBodyComponent.momentum = Vector.of(
                    patrollingMovementComponent.right ? 20 : -20,
                    physicsBodyComponent.momentum.y());
        }
    }

    private boolean spotsWall(final Engine engine, final Entity entity) {
        final var slimeComp = entity.get(PatrollingMovementComponent.class);
        return engine.physics()
                .raycastFrom(entity.position())
                .ignoringEntities(entity)
                .ignoringEntitiesHaving(PlayerMarkerComponent.class)
                .ignoringEntitesNotIn(entity.bounds().expand(8))
                .checkingBorders(Borders.VERTICAL_ONLY)
                .castingHorizontal(slimeComp.right ? 8 : -8).hasHit();
    }

    private boolean isOnEdge(final Engine engine, final Entity entity) {
        final var bounds = entity.bounds();
        final var slimeComp = entity.get(PatrollingMovementComponent.class);
        final Vector start = slimeComp.right
                ? Vector.of(bounds.maxX(), bounds.position().y())
                : Vector.of(bounds.minX(), bounds.position().y());

        return engine.physics()
                .raycastFrom(start)
                .checkingBorders(Borders.TOP_ONLY)
                .ignoringEntitesNotIn(bounds.expand(8))
                .castingVertical(8)
                .noHit();
    }

    private void invertSpeed(final Entity entity) {
        final var physicsBodyComponent = entity.get(PhysicsComponent.class);
        final var pattrolling = entity.get(PatrollingMovementComponent.class);
        physicsBodyComponent.momentum = physicsBodyComponent.momentum.invertX();
        pattrolling.right = !pattrolling.right;
    }
}
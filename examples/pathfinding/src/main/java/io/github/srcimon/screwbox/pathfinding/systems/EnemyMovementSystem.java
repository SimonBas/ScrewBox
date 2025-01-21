package io.github.srcimon.screwbox.pathfinding.systems;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.ai.MovementPathComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.pathfinding.components.PlayerMovementComponent;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMovementComponent.class);

    private static final Archetype ENEMIES = Archetype.of(
            PhysicsComponent.class, RenderComponent.class, MovementPathComponent.class);

    private final Sheduler sheduler = Sheduler.withInterval(Duration.ofMillis(250));

    @Override
    public void update(final Engine engine) {
        if (sheduler.isTick(engine.loop().time())) {
            final Entity player = engine.environment().fetchSingleton(PLAYER);
            final Vector playerPosition = player.position();
            for (final Entity enemy : engine.environment().fetchAll(ENEMIES)) {
                final Vector enemyPosition = enemy.position();
                final var automovement = enemy.get(MovementPathComponent.class);
                engine.async().runExclusive(automovement, () -> engine.physics().findPath(enemyPosition, playerPosition).ifPresent(value -> automovement.path = value));
            }
        }
    }

}

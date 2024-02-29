package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.core.GlobalBoundsComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.utils.Sheduler;

@Order(SystemOrder.PREPARATION)
public class PhysicsGridUpdateSystem implements EntitySystem {

    private static final Archetype WORLD = Archetype.of(GlobalBoundsComponent.class, TransformComponent.class);
    private static final Archetype OBSTACLES = Archetype.of(PhysicsGridObstacleComponent.class, TransformComponent.class);

    private final int gridSize;
    private final Sheduler updateSheduler;

    public PhysicsGridUpdateSystem(final int gridSize, final Sheduler updateSheduler) {
        this.gridSize = gridSize;
        this.updateSheduler = updateSheduler;
    }

    @Override
    public void update(final Engine engine) {
        if (updateSheduler.isTick(engine.loop().lastUpdate())) {
            final Bounds bounds = engine.environment().forcedFetch(WORLD).get(TransformComponent.class).bounds;
            final Grid grid = new Grid(bounds, gridSize);
            for (final Entity blocking : engine.environment().fetchAll(OBSTACLES)) {
                grid.blockArea(blocking.get(TransformComponent.class).bounds);
            }
            engine.physics().setGrid(grid);
        }
    }
}
package de.suzufa.screwbox.core.physics.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.Grid.Node;
import de.suzufa.screwbox.core.physics.PathfindingConfiguration;
import de.suzufa.screwbox.core.physics.Physics;
import de.suzufa.screwbox.core.physics.RaycastBuilder;
import de.suzufa.screwbox.core.physics.SelectEntityBuilder;

public class DefaultPhysics implements Physics {

    private final Engine engine;

    private PathfindingConfiguration configuration = new PathfindingConfiguration();

    public DefaultPhysics(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public RaycastBuilder raycastFrom(final Vector position) {
        return new RaycastBuilder(engine.entityEngine(), position);
    }

    @Override
    public SelectEntityBuilder searchAtPosition(final Vector position) {
        return new SelectEntityBuilder(engine.entityEngine(), position);
    }

    @Override
    public SelectEntityBuilder searchInRange(final Bounds range) {
        return new SelectEntityBuilder(engine.entityEngine(), range);
    }

    @Override
    public Optional<Path> findPath(Grid grid, Vector start, Vector end) {
        Node startPoint = grid.toGrid(start);
        Node endPoint = grid.toGrid(end);

        List<Node> path = configuration.algorithm().findPath(grid, startPoint, endPoint);
        if (path.isEmpty()) {
            return Optional.empty();
        }

        // replace first and last grid-node with actual positions
        List<Vector> list = new ArrayList<>();
        list.add(start);

        for (int i = 1; i < path.size() - 1; i++) {
            list.add(grid.toWorld(path.get(i)));
        }

        list.add(end);
        return Optional.of(Path.withNodes(list));
    }

    @Override
    public PathfindingConfiguration pathfindingConfiguration() {
        return configuration;
    }

}

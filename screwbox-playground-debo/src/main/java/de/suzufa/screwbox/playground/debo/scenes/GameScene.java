package de.suzufa.screwbox.playground.debo.scenes;

import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.systems.AreaTriggerSystem;
import de.suzufa.screwbox.core.entityengine.systems.CameraMovementSystem;
import de.suzufa.screwbox.core.entityengine.systems.CollisionSensorSystem;
import de.suzufa.screwbox.core.entityengine.systems.CombineStaticCollidersSystem;
import de.suzufa.screwbox.core.entityengine.systems.FadeOutSystem;
import de.suzufa.screwbox.core.entityengine.systems.GravitySystem;
import de.suzufa.screwbox.core.entityengine.systems.LogFpsSystem;
import de.suzufa.screwbox.core.entityengine.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entityengine.systems.ScreenTransitionSystem;
import de.suzufa.screwbox.core.entityengine.systems.SpriteRenderSystem;
import de.suzufa.screwbox.core.entityengine.systems.StateSystem;
import de.suzufa.screwbox.core.entityengine.systems.TimeoutSystem;
import de.suzufa.screwbox.core.resources.EntityExtractor;
import de.suzufa.screwbox.core.resources.InputFilter;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.playground.debo.collectables.Cherries;
import de.suzufa.screwbox.playground.debo.collectables.DeboB;
import de.suzufa.screwbox.playground.debo.collectables.DeboD;
import de.suzufa.screwbox.playground.debo.collectables.DeboE;
import de.suzufa.screwbox.playground.debo.collectables.DeboO;
import de.suzufa.screwbox.playground.debo.components.CurrentLevelComponent;
import de.suzufa.screwbox.playground.debo.components.ScreenshotComponent;
import de.suzufa.screwbox.playground.debo.effects.BackgroundConverter;
import de.suzufa.screwbox.playground.debo.effects.FadeInEffect;
import de.suzufa.screwbox.playground.debo.enemies.MovingSpikes;
import de.suzufa.screwbox.playground.debo.enemies.slime.Slime;
import de.suzufa.screwbox.playground.debo.enemies.tracer.Tracer;
import de.suzufa.screwbox.playground.debo.map.MapBorderLeft;
import de.suzufa.screwbox.playground.debo.map.MapBorderRight;
import de.suzufa.screwbox.playground.debo.map.MapBorderTop;
import de.suzufa.screwbox.playground.debo.map.MapGravity;
import de.suzufa.screwbox.playground.debo.map.WorldBounds;
import de.suzufa.screwbox.playground.debo.props.Box;
import de.suzufa.screwbox.playground.debo.props.Diggable;
import de.suzufa.screwbox.playground.debo.props.Platfom;
import de.suzufa.screwbox.playground.debo.props.VanishingBlock;
import de.suzufa.screwbox.playground.debo.specials.Camera;
import de.suzufa.screwbox.playground.debo.specials.CatCompanion;
import de.suzufa.screwbox.playground.debo.specials.Waypoint;
import de.suzufa.screwbox.playground.debo.specials.player.Player;
import de.suzufa.screwbox.playground.debo.systems.AutoflipByMovementSystem;
import de.suzufa.screwbox.playground.debo.systems.BackgroundSystem;
import de.suzufa.screwbox.playground.debo.systems.CameraShiftSystem;
import de.suzufa.screwbox.playground.debo.systems.CatMovementSystem;
import de.suzufa.screwbox.playground.debo.systems.ChangeMapSystem;
import de.suzufa.screwbox.playground.debo.systems.CollectableSystem;
import de.suzufa.screwbox.playground.debo.systems.DebugConfigSystem;
import de.suzufa.screwbox.playground.debo.systems.DetectLineOfSightToPlayerSystem;
import de.suzufa.screwbox.playground.debo.systems.DiggableSystem;
import de.suzufa.screwbox.playground.debo.systems.FollowPlayerSystem;
import de.suzufa.screwbox.playground.debo.systems.GroundDetectorSystem;
import de.suzufa.screwbox.playground.debo.systems.KillZoneSystem;
import de.suzufa.screwbox.playground.debo.systems.KilledFromAboveSystem;
import de.suzufa.screwbox.playground.debo.systems.LetsGoSystem;
import de.suzufa.screwbox.playground.debo.systems.MovableSystem;
import de.suzufa.screwbox.playground.debo.systems.MovingPlattformSystem;
import de.suzufa.screwbox.playground.debo.systems.PatrollingMovementSystem;
import de.suzufa.screwbox.playground.debo.systems.PauseSystem;
import de.suzufa.screwbox.playground.debo.systems.PlayerControlSystem;
import de.suzufa.screwbox.playground.debo.systems.PrintSystem;
import de.suzufa.screwbox.playground.debo.systems.ResetSceneSystem;
import de.suzufa.screwbox.playground.debo.systems.ShadowSystem;
import de.suzufa.screwbox.playground.debo.systems.ShowLabelSystem;
import de.suzufa.screwbox.playground.debo.systems.SmokePuffSystem;
import de.suzufa.screwbox.playground.debo.systems.VanishingOnCollisionSystem;
import de.suzufa.screwbox.playground.debo.systems.ZoomSystem;
import de.suzufa.screwbox.playground.debo.tiles.NonSolidTile;
import de.suzufa.screwbox.playground.debo.tiles.OneWayGround;
import de.suzufa.screwbox.playground.debo.tiles.SolidGround;
import de.suzufa.screwbox.playground.debo.zones.ChangeMapZone;
import de.suzufa.screwbox.playground.debo.zones.KillZone;
import de.suzufa.screwbox.playground.debo.zones.ShowLabelZone;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.Tile;
import de.suzufa.screwbox.tiled.TiledSupport;

public class GameScene implements Scene {

    private final String mapName;

    public GameScene(final String mapName) {
        this.mapName = mapName;
    }

    @Override
    public void initialize(EntityEngine entityEngine) {
        entityEngine
                .add(createEntitiesFromMap())
                .add(new Entity().add(new ScreenshotComponent(), new CurrentLevelComponent(mapName)));

        entityEngine.add(
                new LogFpsSystem(),
                new CollisionSensorSystem(),
                new MovingPlattformSystem(),
                new CollectableSystem(),
                new CameraMovementSystem(),
                new StateSystem(),
                new VanishingOnCollisionSystem(),
                new KilledFromAboveSystem(),
                new GroundDetectorSystem(),
                new KillZoneSystem(),
                new DebugConfigSystem(),
                new PauseSystem(),
                new ZoomSystem(),
                new FadeOutSystem(),
                new MovableSystem(),
                new DiggableSystem(),
                new FollowPlayerSystem(),
                new PlayerControlSystem(),
                new SmokePuffSystem(),
                new ShowLabelSystem(),
                new LetsGoSystem(),
                new ScreenTransitionSystem(),
                new PrintSystem(),
                new ChangeMapSystem(),
                new ShadowSystem(),
                new PhysicsSystem(),
                new GravitySystem(),
                new CameraShiftSystem(),
                new CombineStaticCollidersSystem(),
                new DetectLineOfSightToPlayerSystem(),
                new PatrollingMovementSystem(),
                new AreaTriggerSystem(),
                new TimeoutSystem(),
                new ResetSceneSystem(),
                new AutoflipByMovementSystem(),
                new BackgroundSystem(),
                new CatMovementSystem(),
                new SpriteRenderSystem());
    }

    List<Entity> createEntitiesFromMap() {
        Map map = TiledSupport.loadMap(mapName);

        return EntityExtractor.from(map)
                .use(new MapGravity())
                .use(new WorldBounds())
                .useIf(propertyIsSet("closed-left"), new MapBorderLeft())
                .useIf(propertyIsSet("closed-right"), new MapBorderRight())
                .useIf(propertyIsSet("closed-top"), new MapBorderTop())

                .forEach(Map::allLayers)
                .useIf(Layer::isImageLayer, new BackgroundConverter())
                .endLoop()

                .forEach(Map::allTiles)
                .useIf(tileTypeIs("non-solid"), new NonSolidTile())
                .useIf(tileTypeIs("solid"), new SolidGround())
                .useIf(tileTypeIs("one-way"), new OneWayGround())
                .endLoop()

                .forEach(Map::allObjects)
                .useIf(hasName("cat"), new CatCompanion())
                .useIf(hasName("moving-spikes"), new MovingSpikes())
                .useIf(hasName("vanishing-block"), new VanishingBlock())
                .useIf(hasName("slime"), new Slime())
                .useIf(hasName("platform"), new Platfom())
                .useIf(hasName("waypoint"), new Waypoint())
                .useIf(hasName("camera"), new Camera())
                .useIf(hasName("player"), new Player())
                .useIf(hasName("debo-d"), new DeboD())
                .useIf(hasName("debo-e"), new DeboE())
                .useIf(hasName("debo-b"), new DeboB())
                .useIf(hasName("debo-o"), new DeboO())
                .useIf(hasName("cherries"), new Cherries())
                .useIf(hasName("killzone"), new KillZone())
                .useIf(hasName("box"), new Box())
                .useIf(hasName("diggable"), new Diggable())
                .useIf(hasName("change-map-zone"), new ChangeMapZone())
                .useIf(hasName("show-label-zone"), new ShowLabelZone())
                .useIf(hasName("fade-in"), new FadeInEffect())
                .useIf(hasName("tracer"), new Tracer())
                .endLoop()

                .buildAllEntities();
    }

    private InputFilter<GameObject> hasName(String name) {
        return gameObject -> name.equals(gameObject.name());
    }

    private InputFilter<Map> propertyIsSet(String property) {
        return map -> map.properties().getBoolean(property).orElse(false);
    }

    private InputFilter<Tile> tileTypeIs(String name) {
        return tile -> {
            final Optional<String> type = tile.layer().properties().get("type");
            if (type.isPresent()) {
                return name.equals(type.get());
            }
            final Optional<String> tileType = tile.properties().get("type");
            return tileType.isPresent() && name.equals(tileType.get());
        };
    }

}

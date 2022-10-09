package de.suzufa.screwbox.playground.debo.scenes;

import static java.util.Objects.nonNull;

import java.util.Optional;
import java.util.function.Predicate;

import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.systems.AreaTriggerSystem;
import de.suzufa.screwbox.core.entities.systems.AutFlipSpriteSystem;
import de.suzufa.screwbox.core.entities.systems.CameraMovementSystem;
import de.suzufa.screwbox.core.entities.systems.CollisionSensorSystem;
import de.suzufa.screwbox.core.entities.systems.CombineStaticCollidersSystem;
import de.suzufa.screwbox.core.entities.systems.FadeOutSystem;
import de.suzufa.screwbox.core.entities.systems.GravitySystem;
import de.suzufa.screwbox.core.entities.systems.LogFpsSystem;
import de.suzufa.screwbox.core.entities.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entities.systems.ReflectionRenderSystem;
import de.suzufa.screwbox.core.entities.systems.ScreenTransitionSystem;
import de.suzufa.screwbox.core.entities.systems.SpriteRenderSystem;
import de.suzufa.screwbox.core.entities.systems.StateSystem;
import de.suzufa.screwbox.core.entities.systems.TimeoutSystem;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.playground.debo.collectables.Cherries;
import de.suzufa.screwbox.playground.debo.collectables.DeboB;
import de.suzufa.screwbox.playground.debo.collectables.DeboD;
import de.suzufa.screwbox.playground.debo.collectables.DeboE;
import de.suzufa.screwbox.playground.debo.collectables.DeboO;
import de.suzufa.screwbox.playground.debo.components.CurrentLevelComponent;
import de.suzufa.screwbox.playground.debo.components.ScreenshotComponent;
import de.suzufa.screwbox.playground.debo.effects.Background;
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
import de.suzufa.screwbox.playground.debo.zones.ReflectionZone;
import de.suzufa.screwbox.playground.debo.zones.ShowLabelZone;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.Tile;

public class GameScene implements Scene {

    private final String mapName;

    public GameScene() {
        this(null);
    }

    public GameScene(final String mapName) {
        this.mapName = mapName;
    }

    @Override
    public void initialize(Entities entities) {
        if (nonNull(mapName)) {
            importEntities(entities);
        }

        entities.add(
                new LogFpsSystem(),
                new ReflectionRenderSystem(),
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
                new AutFlipSpriteSystem(),
                new BackgroundSystem(),
                new CatMovementSystem(),
                new SpriteRenderSystem());
    }

    void importEntities(Entities entities) {
        entities.add(new Entity()
                .add(new ScreenshotComponent())
                .add(new CurrentLevelComponent(mapName)));

        Map map = Map.fromJson(mapName);

        entities.importSource(map)
                .as(new MapGravity())
                .as(new WorldBounds())
                .when(propertyIsActive("closed-left")).as(new MapBorderLeft())
                .when(propertyIsActive("closed-left")).as(new MapBorderLeft())
                .when(propertyIsActive("closed-right")).as(new MapBorderRight())
                .when(propertyIsActive("closed-top")).as(new MapBorderTop());

        entities.importSource(map.layers())
                .when(Layer::isImageLayer).as(new Background());

        entities.importSource(map.tiles())
                .usingIndex(this::tileType)
                .when("non-solid").as(new NonSolidTile())
                .when("solid").as(new SolidGround())
                .when("one-way").as(new OneWayGround());

        entities.importSource(map.objects())
                .usingIndex(GameObject::name)
                .when("reflection-zone").as(new ReflectionZone())
                .when("cat").as(new CatCompanion())
                .when("moving-spikes").as(new MovingSpikes())
                .when("vanishing-block").as(new VanishingBlock())
                .when("slime").as(new Slime())
                .when("platform").as(new Platfom())
                .when("waypoint").as(new Waypoint())
                .when("camera").as(new Camera())
                .when("player").as(new Player())
                .when("debo-d").as(new DeboD())
                .when("debo-e").as(new DeboE())
                .when("debo-b").as(new DeboB())
                .when("debo-o").as(new DeboO())
                .when("cherries").as(new Cherries())
                .when("killzone").as(new KillZone())
                .when("box").as(new Box())
                .when("diggable").as(new Diggable())
                .when("change-map-zone").as(new ChangeMapZone())
                .when("show-label-zone").as(new ShowLabelZone())
                .when("fade-in").as(new FadeInEffect())
                .when("tracer").as(new Tracer());
    }

    private Predicate<Map> propertyIsActive(String property) {
        return map -> map.properties().getBoolean(property).orElse(false);
    }

    private String tileType(Tile tile) {
        final Optional<String> layerType = tile.layer().properties().get("type");
        if (layerType.isPresent()) {
            return layerType.get();
        }
        final Optional<String> tileType = tile.properties().get("type");
        return tileType.isPresent() ? tileType.get() : "none";
    }

}

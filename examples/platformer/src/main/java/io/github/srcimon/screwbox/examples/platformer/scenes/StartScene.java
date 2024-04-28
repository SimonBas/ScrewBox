package io.github.srcimon.screwbox.examples.platformer.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.graphics.MouseCursor;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.ui.KeyboardAndMouseInteractor;
import io.github.srcimon.screwbox.core.ui.KeyboardInteractor;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.examples.platformer.menues.StartGameMenu;

import java.util.List;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.tiled.Tileset.fromJson;

public class StartScene implements Scene {

    private static final Asset<List<Sprite>> BACKGROUNDS = Asset
            .asset(() -> List.of(
                    fromJson("tilesets/specials/player.json").findByName("idle"),
                    fromJson("tilesets/enemies/slime.json").findByName("moving"),
                    fromJson("tilesets/enemies/tracer.json").findByName("active"),
                    fromJson("tilesets/specials/cat.json").findByName("walking"),
                    fromJson("tilesets/collectables/cherries.json").first(),
                    fromJson("tilesets/props/box.json").first()));

    @Override
    public void populate(Environment environment) {
        Sheduler t = Sheduler.withInterval(Duration.ofMillis(40));
        environment
                .enableTweening()
                .enablePhysics()
                .enableParticles()
                .enableRendering()
                .addEntity(1, new TransformComponent(0, 0, 60, 60), new ParticleInteractionComponent(20), new PhysicsComponent())
                .addSystem(engine -> engine.environment().fetchById(1).moveTo(engine.mouse().position()))
                .addSystem(engine -> {
                    if (t.isTick()) {
                        engine.particles().spawnMultiple(1, engine.graphics().world().visibleArea().moveBy(0, engine.graphics().world().visibleArea().height()), ParticleOptions.unknownSource()
                                .baseSpeed(Vector.y(-60))
                                .tweenMode(TweenMode.SINE_IN_OUT)
                                .randomStartScale(6, 8)
                                .startOpacity(Percent.zero())
                                .animateOpacity(Percent.zero(), Percent.of(0.1))
                                .chaoticMovement(50, ofSeconds(1))
                                .drawOrder(2)
                                .randomStartRotation()
                                .randomLifeTimeSeconds(2, 4)
                                .animateScale(0.5, 1.5)
                                .animateOpacity(Percent.zero(), Percent.max())
                                .sprites(BACKGROUNDS.get()));
                    }
                });
    }

    @Override
    public void onEnter(Engine engine) {
        engine.ui()
                .setInteractor(new KeyboardAndMouseInteractor())
                .openMenu(new StartGameMenu());
        engine.window()
                .setTitle("Platformer (Menu)")
                .setCursor(MouseCursor.DEFAULT);
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui()
                .setInteractor(new KeyboardInteractor())
                .closeMenu();

        engine.window()
                .setCursor(MouseCursor.HIDDEN);
    }
}

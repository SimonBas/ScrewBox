package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.core.utils.ListUtil;
import io.github.srcimon.screwbox.platformer.components.MovingPlatformComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.platformer.components.VanishingOnCollisionComponent;

import java.util.List;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;

@Order(Order.SystemOrder.PREPARATION)
public class VanishingOnCollisionSystem implements EntitySystem {

    private static final Archetype VANISHINGS = Archetype.ofSpacial(VanishingOnCollisionComponent.class);
    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);

    @Override
    public void update(Engine engine) {
        Time now = engine.loop().time();

        Entity player = engine.environment().fetchSingleton(PLAYER);
        Bounds playerBounds = player.bounds();

        List<Entity> activatedEntities = ListUtil.merge(

                engine.physics().raycastFrom(playerBounds.bottomRight().addX(-2))
                        .ignoringEntities(player)
                        .checkingBorders(Borders.TOP_ONLY)
                        .checkingFor(VANISHINGS)
                        .castingVertical(0.5)
                        .selectAllEntities(),
                engine.physics().raycastFrom(playerBounds.bottomLeft().addX(2))
                        .ignoringEntities(player)
                        .checkingBorders(Borders.TOP_ONLY)
                        .checkingFor(VANISHINGS)
                        .castingVertical(0.5)
                        .selectAllEntities());

        for (final Entity entity : activatedEntities) {
            var vanish = entity.get(VanishingOnCollisionComponent.class);
            if (vanish.vanishTime.isUnset()) {
                engine.audio().playSound(SoundBundle.STEAM);
                vanish.vanishTime = vanish.timeout.addTo(now);
            }
        }

        for (Entity vanishEntity : engine.environment().fetchAll(VANISHINGS)) {
            if (now.isAfter(vanishEntity.get(VanishingOnCollisionComponent.class).vanishTime)) {
                Vector center = vanishEntity.position();
                Vector targetPosition = center.addY(200);
                vanishEntity
                        .add(new TweenComponent(ofMillis(400)))
                        .add(new TweenOpacityComponent())
                        .add(new TweenDestroyComponent())
                        .add(new CollisionSensorComponent())
                        .add(new MovingPlatformComponent(targetPosition, 20))
                        .remove(VanishingOnCollisionComponent.class);
            }
        }

    }
}

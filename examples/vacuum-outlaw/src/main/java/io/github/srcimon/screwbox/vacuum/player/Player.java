package io.github.srcimon.screwbox.vacuum.player;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.vacuum.SpeedComponent;

public class Player implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("player")
                .add(new TransformComponent(object.position(), 8, 8))
                .add(new PhysicsComponent())
                .add(new ShadowCasterComponent(false))
                .add(new SpeedComponent())
                .add(new RenderComponent(SpriteBundle.SLIME_MOVING, object.layer().order()))
                .add(new CameraTargetComponent(5));
    }
}

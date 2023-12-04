package io.github.srcimon.screwbox.examples.platformer.enemies.tracer;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntityState;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.DetectLineOfSightToPlayerComponent;
import io.github.srcimon.screwbox.examples.platformer.components.FollowPlayerComponent;
import io.github.srcimon.screwbox.tiled.Tileset;

public class TracerActiveState implements EntityState {

    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("tilesets/enemies/tracer.json", "active");
    private static final Asset<Sound> SOUND = Sound.assetFromFile("sounds/scream.wav");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get().freshInstance();
        entity.add(new FollowPlayerComponent());
        engine.audio().playEffectLooped(SOUND.get());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return entity.get(DetectLineOfSightToPlayerComponent.class).isInLineOfSight
                ? this
                : new TracerInactiveState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(FollowPlayerComponent.class);
        engine.audio().stop(SOUND.get());
    }

}
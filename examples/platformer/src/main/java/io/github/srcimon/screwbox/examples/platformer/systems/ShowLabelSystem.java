package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.FontsBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.SignalComponent;
import io.github.srcimon.screwbox.examples.platformer.components.LabelComponent;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class ShowLabelSystem implements EntitySystem {

    private static final Archetype LABELD = Archetype.of(SignalComponent.class, LabelComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.environment().fetchAll(LABELD)) {
            if (entity.get(SignalComponent.class).isTriggered) {
                LabelComponent labelComponent = entity.get(LabelComponent.class);
                Bounds bounds = entity.get(TransformComponent.class).bounds;

                Vector position = Vector.of(bounds.position().x(), bounds.minY());
                engine.graphics().world().drawTextCentered(position, labelComponent.label, FontsBundle.BOLDZILLA.getWhite(),
                        Percent.max(),
                        labelComponent.size / 15.0);
            }
        }

    }
}

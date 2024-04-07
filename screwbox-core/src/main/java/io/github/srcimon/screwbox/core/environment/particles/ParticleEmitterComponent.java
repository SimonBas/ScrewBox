package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.io.Serial;

public class ParticleEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean active = true;
    public boolean useArea = true;//TODO replace with enum
    public Sheduler sheduler;

    public ParticleEmitterComponent(final Sheduler sheduler) {
        this.sheduler = sheduler;
    }
}
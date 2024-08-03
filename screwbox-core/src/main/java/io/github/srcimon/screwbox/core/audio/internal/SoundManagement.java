package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;

import javax.sound.sampled.SourceDataLine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SoundManagement {

    private final Map<UUID, ManagedSound> activeSounds = new ConcurrentHashMap<>();

    public static class ManagedSound {
        private final Playback playback;
        private boolean isShutdown = false;
        private SourceDataLine line;

        public boolean isEffect() {
            return playback.options().isEffect();
        }

        public Percent volume() {
            return playback.options().volume();
        }

        public Sound sound() {
            return playback.sound();
        }

        public SourceDataLine line() {
            return line;
        }

        public ManagedSound(final Playback playback) {
            this.playback = playback;
        }

        public void stop() {
            isShutdown = true;
        }

        public Playback playback() {
            return playback;
        }

        public boolean isShutdown() {
            return isShutdown;
        }

        public void setLine(SourceDataLine line) {
            this.line = line;
        }
    }

    public ManagedSound add(UUID id, Playback playback) {
        ManagedSound value = new ManagedSound(playback);
        activeSounds.put(id, value);
        return value;
    }

    public void remove(UUID id) {
        activeSounds.remove(id);
    }

    public List<ManagedSound> activeSounds() {
        return new ArrayList<>(activeSounds.values());
    }

    public List<ManagedSound> fetchActiveSounds(final Sound sound) {
        final List<ManagedSound> active = new ArrayList<>();
        for (final var activeSound : activeSounds.values()) {
            if (activeSound.playback().sound().equals(sound)) {
                active.add(activeSound);
            }
        }
        return active;
    }
}

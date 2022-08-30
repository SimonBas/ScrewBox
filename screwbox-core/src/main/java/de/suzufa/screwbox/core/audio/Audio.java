package de.suzufa.screwbox.core.audio;

import de.suzufa.screwbox.core.Percentage;

public interface Audio {

    Audio playEffect(Sound sound);

    Audio playEffectLooped(Sound sound);

    Audio playMusic(Sound sound);

    Audio resume(Sound sound);

    Audio resumeLooped(Sound sound);

    Audio stop(Sound sound);

    Audio stopAllSounds();

    Audio setEffectVolume(Percentage volume);

    Audio setMusicVolume(Percentage volume);

    int activeCount(Sound sound);

}

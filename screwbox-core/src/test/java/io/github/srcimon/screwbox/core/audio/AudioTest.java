package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.assets.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AudioTest {

    @Spy
    Audio audio;

    @Test
    void playEffect_soundAsset_playsSoundFromAsset() {
        Sound sound = Sound.fromFile("kill.wav");
        Asset<Sound> soundAsset = Asset.asset(() -> sound);

        audio.playEffect(soundAsset);

        verify(audio).playEffect(sound);
    }

}

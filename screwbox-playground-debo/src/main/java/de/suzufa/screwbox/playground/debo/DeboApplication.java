package de.suzufa.screwbox.playground.debo;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.ui.WobblyUiLayouter;
import de.suzufa.screwbox.playground.debo.scenes.DeadScene;
import de.suzufa.screwbox.playground.debo.scenes.PauseScene;
import de.suzufa.screwbox.playground.debo.scenes.StartScene;

public class DeboApplication {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Debo Game");

        engine.ui().setLayouter(new WobblyUiLayouter());

        // TODO: shite
        engine.async().run(DeboApplication.class,
                () -> new Demo().findAllClassesUsingClassLoader("de.suzufa.screwbox.playground.debo.enemies.slime"));

        for (var clazz : new Demo()
                .findAllClassesUsingClassLoader("de.suzufa.screwbox.playground.debo.enemies.slime")) {
            for (var field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Asset.class)) {
                    System.out.println("Found asset: " + field.getName());
                }
            }
        }

        engine.scenes()
                .add(new DeadScene())
                .add(new PauseScene())
                .add(new StartScene());

        engine.start(StartScene.class);
    }
}
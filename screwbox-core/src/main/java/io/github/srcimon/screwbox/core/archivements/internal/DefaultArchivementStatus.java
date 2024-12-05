package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;
import io.github.srcimon.screwbox.core.archivements.ArchivementStatus;

import java.util.Objects;
import java.util.Optional;

class DefaultArchivementStatus implements ArchivementStatus {

    private final Archivement archivement;
    private final ArchivementDetails details;
    private int score = 0;
    private final Time startTime;
    private Time completionTime;

    public DefaultArchivementStatus(final Archivement archivement) {
        this.details = archivement.details();
        this.archivement = archivement;
        this.startTime = Time.now();
        this.completionTime = Time.unset();
    }

    @Override
    public String title() {
        return resolvePlaceholders(details.title());
    }

    @Override
    public Optional<String> description() {
        return Objects.isNull(details.description())
                ? Optional.empty()
                : Optional.of(resolvePlaceholders(details.description()));
    }

    @Override
    public int goal() {
        return details.goal();
    }

    @Override
    public boolean isCompleted() {
        return score() >= goal();
    }

    @Override
    public Percent progress() {
        return Percent.of((double) score / goal());
    }

    @Override
    public Time startTime() {
        return startTime;
    }

    @Override
    public Time completionTime() {
        return completionTime;
    }

    @Override
    public int score() {
        return score;
    }

    //TODO reduce interaction between this and calling class
    public void progress(final int progress) {
        setProgress(details.progressionIsAbsolute() ? score +progress : progress);
    }

    public void setProgress(final int progress) {//TODO return boolean of status
        score = Math.min(goal(), progress);
        if(progress == goal()) {
            completionTime = Time.now();
        }
    }

    public boolean isOfFamily(Class<? extends Archivement> definition) {
        return this.archivement.getClass().equals(definition) || //TODO needed?
                this.archivement.getClass().isAssignableFrom(definition);
    }

    public int autoProgress(Engine engine) {
        return archivement.progress(engine);
    }

    private String resolvePlaceholders(final String value) {
        return value.replace("{goal}", String.valueOf(details.goal()));
    }

    public boolean canBeUpdatedLazy() {
        return details.progressionIsAbsolute();
    }
}

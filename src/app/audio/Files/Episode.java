package app.audio.Files;

import app.utils.Enums;
import lombok.Getter;

@Getter
public final class Episode extends AudioFile {
    private final String description;

    public Episode(String name, Integer duration, String description) {
        super(name, duration);
        this.description = description;
    }
}

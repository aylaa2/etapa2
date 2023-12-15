package app.audio.Collections;

import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public final class Album extends AudioCollection {
    private final int releaseYear;
    private final String description;
    private final List<Song> songs;

    public Album(final String name, final String owner, final String description,
                 final int releaseYear, final List<Song> songInputs) {
        super(name, owner);
        this.description = description;
        this.releaseYear = releaseYear;
        this.songs = new ArrayList<>(songInputs);

    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public boolean matchesName(final String name) {
        return getName()
                .toLowerCase().startsWith(name.toLowerCase());
    }


    @Override
    public boolean matchesDescription(final String descriptionText) {
        return description != null
                && description.contains(descriptionText);
    }

    @Override
    public Song getTrackByIndex(final int index) {
        if (index >= 0 && index < songs.size()) {
            return songs.get(index);
        }
        return null;
    }
}

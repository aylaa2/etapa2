package app.audio.Collections;

import app.audio.Files.Song;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public final class Album extends AudioCollection {
    private final int releaseYear;
    private final String description;
    private final List<Song> songs;

    public Album(String name, String owner, String description, int releaseYear, List<Song> songInputs) {
        super(name, owner);
        this.description = description;
        this.releaseYear = releaseYear;
        this.songs = new ArrayList<>(songInputs);

    }
//    @Override
//    public Enums.PageType getPageType() {
//        // Determine what page type is appropriate for Playlist
//        // This is just an example. You should replace it with what makes sense for your application
//        return Enums.PageType.ARTISTPAGE;
//    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public boolean matchesName(String name) {
        return getName().toLowerCase().startsWith(name.toLowerCase());
    }


    @Override
    public boolean matchesDescription(String descriptionText) {
        return description != null && description.contains(descriptionText);
    }

    @Override
    public Song getTrackByIndex(int index) {
        if (index >= 0 && index < songs.size()) {
            return songs.get(index);
        }
        return null;
    }
}
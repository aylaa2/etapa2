package app.audio.Collections;

import app.audio.Files.Song;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public final class Album extends AudioCollection {
    private int releaseYear;
    private String description;
    private List<Song> songs; // Store Song objects instead of SongInput

    public Album(String name, String owner, String description, int releaseYear, List<SongInput> songInputs) {
        super(name, owner);
        this.description = description;
        this.releaseYear = releaseYear;
        this.songs = new ArrayList<>();

        // Convert SongInput objects to Song objects
        for (SongInput songInput : songInputs) {
            // Assuming Song constructor takes similar parameters as SongInput
            Song song = new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(), songInput.getTags(), songInput.getLyrics(), songInput.getGenre(), songInput.getReleaseYear(), songInput.getArtist());
            this.songs.add(song);
        }
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }


    public boolean matchesName(String searchText) {
        return getName().startsWith(searchText);
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

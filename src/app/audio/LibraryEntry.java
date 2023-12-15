package app.audio;

import lombok.Getter;
import java.util.ArrayList;

@Getter
public abstract class LibraryEntry {
    private final String name; // song name, album, playlist ...


    /**
     * Constructor for LibraryEntry.
     *
     * @param name The name of the library entry (e.g., song name, album, playlist).
     */
    public LibraryEntry(final String name) {
        this.name = name;
    }

    /**
     * Check if the library entry name matches the provided name.
     *
     * @param name The name to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesName(final String name) {
        return getName().toLowerCase().startsWith(name.toLowerCase());
    }

    /**
     * Check if the library entry matches the provided album.
     *
     * @param album The album to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesAlbum(final String album) {
        return false;
    }

    /**
     * Check if the library entry matches the provided tags.
     *
     * @param tags The tags to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesTags(final ArrayList<String> tags) {
        return false;
    }

    /**
     * Check if the library entry matches the provided lyrics.
     *
     * @param lyrics The lyrics to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesLyrics(final String lyrics) {
        return false;
    }

    /**
     * Check if the library entry matches the provided genre.
     *
     * @param genre The genre to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesGenre(final String genre) {
        return false;
    }

    /**
     * Check if the library entry matches the provided artist.
     *
     * @param artist The artist to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesArtist(final String artist) {
        return false;
    }

    /**
     * Check if the library entry matches the provided release year.
     *
     * @param releaseYear The release year to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesReleaseYear(final String releaseYear) {
        return false;
    }

    /**
     * Check if the library entry matches the provided owner.
     *
     * @param user The owner to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesOwner(final String user) {
        return false;
    }

    /**
     * Check if the library entry is visible to the provided user.
     *
     * @param user The user to check visibility for.
     * @return true if visible, false otherwise.
     */
    public boolean isVisibleToUser(final String user) {
        return false;
    }

    /**
     * Check if the library entry matches the provided followers.
     *
     * @param followers The followers to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesFollowers(final String followers) {
        return false;
    }

    /**
     * Check if the library entry matches the provided description.
     *
     * @param description The description to match against.
     * @return true if there is a match, false otherwise.
     */
    public boolean matchesDescription(final String description) {
        return false;
    }
}

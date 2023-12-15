package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

/**
 * Represents a playlist in the audio collection system.
 */
@Getter
public final class Playlist extends AudioCollection {
    private final ArrayList<Song> songs;
    private Enums.Visibility visibility;
    private Integer followers;
    private int timestamp;

    /**
     * Constructor for Playlist with default timestamp.
     *
     * @param name  The name of the playlist.
     * @param owner The owner of the playlist.
     */
    public Playlist(final String name, final String owner) {
        this(name, owner, 0);
    }

    /**
     * Constructor for Playlist.
     *
     * @param name      The name of the playlist.
     * @param owner     The owner of the playlist.
     * @param timestamp The timestamp of the playlist creation.
     */
    public Playlist(final String name, final String owner, final int timestamp) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.visibility = Enums.Visibility.PUBLIC;
        this.followers = 0;
        this.timestamp = timestamp;
    }

    /**
     * Checks if a song is contained in the playlist.
     *
     * @param song The song to check.
     * @return True if the song is in the playlist.
     */
    public boolean containsSong(final Song song) {
        return songs.contains(song);
    }

    /**
     * Adds a song to the playlist.
     *
     * @param song The song to add.
     */
    public void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * Removes a song from the playlist.
     *
     * @param song The song to remove.
     */
    public void removeSong(final Song song) {
        songs.remove(song);
    }

    /**
     * Removes a song from the playlist by index.
     *
     * @param index The index of the song to remove.
     */
    public void removeSong(final int index) {
        songs.remove(index);
    }

    /**
     * Switches the visibility of the playlist between public and private.
     */
    public void switchVisibility() {
        if (visibility == Enums.Visibility.PUBLIC) {
            visibility = Enums.Visibility.PRIVATE;
        } else {
            visibility = Enums.Visibility.PUBLIC;
        }
    }

    /**
     * Increases the number of followers of the playlist.
     */
    public void increaseFollowers() {
        followers++;
    }

    /**
     * Decreases the number of followers of the playlist.
     */
    public void decreaseFollowers() {
        followers--;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    @Override
    public boolean isVisibleToUser(final String user) {
        return this.getVisibility() == Enums.Visibility.PUBLIC
                || (this.getVisibility() == Enums.Visibility.PRIVATE
                && this.getOwner().equals(user));
    }

    @Override
    public boolean matchesFollowers(final String query) {
        return filterByFollowersCount(this.getFollowers(), query);
    }

    private static boolean filterByFollowersCount(final int count, final String query) {
        if (query.startsWith("<")) {
            return count < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return count > Integer.parseInt(query.substring(1));
        } else {
            return count == Integer.parseInt(query);
        }
    }


}

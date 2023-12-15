package app.player;

import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The main player class for audio playback.
 */
public class Player {
    private Enums.RepeatMode repeatMode;
    private boolean shuffle;
    private boolean paused;
    @Getter
    private PlayerSource source;
    @Getter
    private String type;
    private boolean isOnline = true;
    private final ArrayList<PodcastBookmark> bookmarks = new ArrayList<>();

    /**
     * Set the player's pause state.
     *
     * @param paused true to pause, false to resume.
     */
    public void setPaused(final boolean paused) {
        this.paused = paused;
    }

    /**
     * Instantiate a new Player.
     */
    public Player() {
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.paused = true;
    }

    /**
     * Stop playback and reset player state.
     */
    public void stop() {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        repeatMode = Enums.RepeatMode.NO_REPEAT;
        paused = true;
        source = null;
        shuffle = false;
    }

    private void bookmarkPodcast() {
        if (source != null && source.getAudioFile() != null) {
            PodcastBookmark currentBookmark = new PodcastBookmark(source
                    .getAudioCollection().getName(), source.getIndex(), source
                    .getDuration());
            bookmarks.removeIf(bookmark -> bookmark.getName().equals(currentBookmark.getName()));
            bookmarks.add(currentBookmark);
        }
    }

    /**
     * Create a player source based on the type and entry.
     *
     * @param type      the type of source (e.g., "song", "playlist", "podcast").
     * @param entry     the library entry.
     * @param bookmarks the list of podcast bookmarks.
     * @return the player source.
     */
    public static PlayerSource createSource(final String type, final LibraryEntry entry,
                                            final List<PodcastBookmark> bookmarks) {
        if ("song".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.LIBRARY, (AudioFile) entry);
        } else if ("playlist".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.PLAYLIST, (AudioCollection) entry);
        } else if ("podcast".equals(type)) {
            return createPodcastSource((AudioCollection) entry, bookmarks);
        }

        return null;
    }

    private static PlayerSource createPodcastSource(final AudioCollection collection,
                                                    final List<PodcastBookmark> bookmarks) {
        for (PodcastBookmark bookmark : bookmarks) {
            if (bookmark.getName().equals(collection.getName())) {
                return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection, bookmark);
            }
        }
        return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection);
    }

    /**
     * Set the player's source and type.
     *
     * @param entry the library entry.
     * @param type  the type of source (e.g., "song", "playlist", "podcast").
     */
    public void setSource(final LibraryEntry entry, final String type) {
        // Bookmark podcasts when the type is "podcast"
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }
        if ("playlist".equals(this.type)) {
            AudioCollection playlist = (AudioCollection) entry;
            if (!playlist.getName().isEmpty()) {
                this.source = new PlayerSource(Enums.PlayerSourceType.PLAYLIST, playlist);
            }
        }
        // Handling setting an album source
        if ("album".equals(type) && entry instanceof Album) {
            Album album = (Album) entry;
            if (!album.getSongs().isEmpty()) {
                // Set the first song of the album as the current track
                this.source = new PlayerSource(Enums.PlayerSourceType.ALBUM, album);

            }
        } else {
            // Handle other source types
            this.source = createSource(type, entry, bookmarks);
        }

        // Set the type, source, and player settings
        this.type = type;
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.shuffle = false;
        this.paused = true;

        // Update the active content based on the new source
        if (this.source != null) {
            updateActiveContent(this.source.getAudioFile(),
                    this.source.getAudioCollection());
        }
    }

    /**
     * Pause or resume playback depending on the online status.
     */
    public void pause() {
        if (isOnline) {
            paused = !paused;
        } else {
            paused = false;
        }
    }

    /**
     * Shuffle the current playlist or album.
     *
     * @param seed the seed for shuffling.
     */
    public void shuffle(final Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
        }

        if (source.getType() == Enums.PlayerSourceType.PLAYLIST) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
        if (source.getType() == Enums.PlayerSourceType.ALBUM) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
    }

    /**
     * Toggle the repeat mode.
     *
     * @return the current repeat mode.
     */
    public Enums.RepeatMode repeat() {
        if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
            if (source.getType() == Enums.PlayerSourceType.LIBRARY) {
                repeatMode = Enums.RepeatMode.REPEAT_ONCE;
            } else {
                repeatMode = Enums.RepeatMode.REPEAT_ALL;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
                repeatMode = Enums.RepeatMode.REPEAT_INFINITE;
            } else {
                if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                    repeatMode = Enums.RepeatMode.REPEAT_CURRENT_SONG;
                } else {
                    repeatMode = Enums.RepeatMode.NO_REPEAT;
                }
            }
        }

        return repeatMode;
    }

    /**
     * Simulate player playback for a specified duration.
     *
     * @param time the duration to simulate playback.
     */
    public void simulatePlayer(int time) {
        if (source != null && !paused) {
            while (time >= source.getDuration()) {
                time -= source.getDuration();
                next();
                if (paused) {
                    break;
                }
            }
            if (!paused) {
                source.skip(-time);
            }
        }
    }

    /**
     * Play the next track in the current playlist or album.
     */
    public void next() {
        paused = source.setNextAudioFile(repeatMode, shuffle);
        if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
            repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        if (source.getDuration() == 0 && paused) {
            stop();
        }
    }

    /**
     * Play the previous track in the current playlist or album.
     */
    public void prev() {
        source.setPrevAudioFile(shuffle);
        paused = false;
    }

    /**
     * Skip to the next podcast episode.
     */
    private void skip(final int duration) {
        source.skip(duration);
        paused = false;
    }

    /**
     * Skip to the next podcast episode (backward).
     */
    public void skipNext() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(-90);
        }
    }

    /**
     * Skip to the previous podcast episode (forward).
     */
    public void skipPrev() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(90);
        }
    }

    /**
     * Get the currently playing audio file.
     *
     * @return the current audio file.
     */
    public AudioFile getCurrentAudioFile() {
        if (source == null) {
            return null;
        }

        // Check if the source is of type Album and return the current track
        if (source.getType() == Enums.PlayerSourceType.ALBUM) {
            Album album = (Album) source.getAudioCollection();
            return album.getTrackByIndex(source.getIndex());
        }

        return source.getAudioFile();
    }

    /**
     * Check if the player is currently paused.
     *
     * @return true if paused, false otherwise.
     */
    public boolean getPaused() {
        return paused;
    }

    /**
     * Check if shuffle mode is enabled.
     *
     * @return true if shuffle is enabled, false otherwise.
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * Get the player's current stats.
     *
     * @return the player stats.
     */
    public PlayerStats getStats() {
        String filename = "";
        int duration = 0;
        if (source != null && source.getAudioFile() != null) {
            filename = source.getAudioFile().getName();
            duration = source.getDuration();
        } else {
            stop();
        }

        return new PlayerStats(filename, duration, repeatMode, shuffle, paused);
    }

    /**
     * Restore the player's state from a PlayerStats object.
     *
     * @param state the PlayerStats object representing the player's state.
     */
    public void restoreState(final PlayerStats state) {
        if (state != null) {
            this.repeatMode = Enums.RepeatMode.valueOf(state.getRepeat());
            this.shuffle = state.isShuffle();
            this.paused = state.isPaused();
            // Add any other state properties you need to restore
        }
    }

    /**
     * Check if the player is currently playing.
     *
     * @return true if playing, false otherwise.
     */
    public boolean isPlaying() {
        return !this.paused && this.source != null;
    }

    /**
     * Check if a specific audio file is the currently active source.
     *
     * @param file the audio file to check.
     * @return true if the file is the active source, false otherwise.
     */
    public boolean isSourceActive(final AudioFile file) {
        return isPlaying() && this.source.getAudioFile().equals(file);
    }

    /**
     * Check if a specific audio collection is the currently active source.
     *
     * @param collection the audio collection to check.
     * @return true if the collection is the active source, false otherwise.
     */
    public boolean isCollectionActive(final AudioCollection collection) {
        if (this.source == null || this.source.getAudioCollection() == null) {
            return false;
        }
        return this.source.getAudioCollection().equals(collection);
    }

    private static Set<AudioFile> activeSongs = new HashSet<>();
    private static Set<AudioCollection> activeAlbums = new HashSet<>();

    /**
     * Call this method whenever the currently playing song or album changes.
     *
     * @param newSong   the new currently playing audio file.
     * @param newAlbum  the new currently playing audio collection (album).
     */
    private void updateActiveContent(final AudioFile newSong, final AudioCollection newAlbum) {
        // Remove the previous song and album from the active sets
        if (this.source != null) {
            activeSongs.remove(this.source.getAudioFile());
            activeAlbums.remove(this.source.getAudioCollection());
        }

        // Add the new song and album to the active sets
        if (newSong != null) {
            activeSongs.add(newSong);
        }
        if (newAlbum != null) {
            activeAlbums.add(newAlbum);
        }
    }

    /**
     * Check if a specific audio file is currently active.
     *
     * @param song the audio file to check.
     * @return true if the file is active, false otherwise.
     */
    public static boolean isSongActive(final AudioFile song) {
        return activeSongs.contains(song);
    }

    /**
     * Check if a specific audio collection (album) is currently active.
     *
     * @param album the audio collection to check.
     * @return true if the album is active, false otherwise.
     */
    public static boolean isAlbumActive(final AudioCollection album) {
        return activeAlbums.contains(album);
    }
}

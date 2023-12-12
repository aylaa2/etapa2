package app.player;

import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public Player() {
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.paused = true;
    }

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
            PodcastBookmark currentBookmark = new PodcastBookmark(source.getAudioCollection().getName(), source.getIndex(), source.getDuration());
            bookmarks.removeIf(bookmark -> bookmark.getName().equals(currentBookmark.getName()));
            bookmarks.add(currentBookmark);
        }
    }

    public static PlayerSource createSource(String type, LibraryEntry entry, List<PodcastBookmark> bookmarks) {
        if ("song".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.LIBRARY, (AudioFile) entry);
        } else if ("playlist".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.PLAYLIST, (AudioCollection) entry);
        } else if ("podcast".equals(type)) {
            return createPodcastSource((AudioCollection) entry, bookmarks);
        }

        return null;
    }

    private static PlayerSource createPodcastSource(AudioCollection collection, List<PodcastBookmark> bookmarks) {
        for (PodcastBookmark bookmark : bookmarks) {
            if (bookmark.getName().equals(collection.getName())) {
                return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection, bookmark);
            }
        }
        return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection);
    }

    public void setSource(LibraryEntry entry, String type) {
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
            updateActiveContent(this.source.getAudioFile(), this.source.getAudioCollection());
        }
    }


    public void pause() {
        if (isOnline) {
            paused = !paused;
        } else {
           paused = false ;
        }
    }

    public void shuffle (Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
        }

        if (source.getType() == Enums.PlayerSourceType.PLAYLIST) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
    }

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

    public void next() {
        paused = source.setNextAudioFile(repeatMode, shuffle);
        if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
            repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        if (source.getDuration() == 0 && paused) {
            stop();
        }
    }

    public void prev() {
        source.setPrevAudioFile(shuffle);
        paused = false;
    }

    private void skip(int duration) {
        source.skip(duration);
        paused = false;
    }

    public void skipNext() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(-90);
        }
    }

    public void skipPrev() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(90);
        }
    }

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


    public boolean getPaused() {
        return paused;
    }

    public boolean getShuffle() {
        return shuffle;
    }

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


    // Method to restore the player's state
    public void restoreState(PlayerStats state) {
        if (state != null) {
            this.repeatMode = Enums.RepeatMode.valueOf(state.getRepeat());
            this.shuffle = state.isShuffle();
            this.paused = state.isPaused();
            // Add any other state properties you need to restore
        }
    }
    public boolean isPlaying() {
        return !this.paused && this.source != null;
    }

    public boolean isSourceActive(AudioFile file) {
        return isPlaying() && this.source.getAudioFile().equals(file);
    }

    public boolean isCollectionActive(AudioCollection collection) {
        if (this.source == null || this.source.getAudioCollection() == null) {
            return false;
        }
        return this.source.getAudioCollection().equals(collection);
    }


        private static Set<AudioFile> activeSongs = new HashSet<>();
        private static Set<AudioCollection> activeAlbums = new HashSet<>();

        // Call this method whenever the currently playing song or album changes
        private void updateActiveContent(AudioFile newSong, AudioCollection newAlbum) {
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


        public static boolean isSongActive(AudioFile song) {
            return activeSongs.contains(song);
        }

        public static boolean isAlbumActive(AudioCollection album) {
            return activeAlbums.contains(album);
        }



}

package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import lombok.Getter;

/**
 * Abstract class representing a collection of audio files.
 */
@Getter
public abstract class AudioCollection extends LibraryEntry {
    private final String owner;

    /**
     * Constructor for AudioCollection.
     *
     * @param name  The name of the audio collection.
     * @param owner The owner of the audio collection.
     */
    public AudioCollection(final String name, final String owner) {
        super(name);
        this.owner = owner;
    }

    /**
     * Gets the number of tracks in the audio collection.
     *
     * @return The number of tracks.
     */
    public abstract int getNumberOfTracks();

    /**
     * Retrieves a track by its index.
     *
     * @param index The index of the track.
     * @return The audio file at the specified index.
     */
    public abstract AudioFile getTrackByIndex(int index);

    /**
     * Checks if the user matches the owner of the audio collection.
     * Subclasses can override this method to provide custom owner matching logic.
     *
     * @param user The user to check.
     * @return True if the user matches the owner, false otherwise.
     */
    @Override
    public boolean matchesOwner(final String user) {
        return this.getOwner().equals(user);
    }
}

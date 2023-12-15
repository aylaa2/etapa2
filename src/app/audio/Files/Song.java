package app.audio.Files;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private Integer likes;

    /**
     * Constructor for Song.
     *
     * @param name        The name of the song.
     * @param duration    The duration of the song.
     * @param album       The album of the song.
     * @param tags        The tags associated with the song.
     * @param lyrics      The lyrics of the song.
     * @param genre       The genre of the song.
     * @param releaseYear The release year of the song.
     * @param artist      The artist of the song.
     */
    public Song(final String name, final Integer duration, final String album,
                final ArrayList<String> tags, final String lyrics, final String genre,
                final Integer releaseYear, final String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.likes = 0;
    }

    @Override
    public boolean matchesAlbum(final String albumName) {
        return this.album != null && this.album.equalsIgnoreCase(albumName);
    }

    @Override
    public boolean matchesTags(final ArrayList<String> songTags) {
        List<String> lowerCaseSongTags = new ArrayList<>();
        for (String tag : this.getTags()) {
            lowerCaseSongTags.add(tag.toLowerCase());
        }

        for (String tag : songTags) {
            if (!lowerCaseSongTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean matchesLyrics(final String lyricsText) {
        return this.getLyrics().toLowerCase().contains(lyricsText.toLowerCase());
    }

    @Override
    public boolean matchesGenre(final String genreName) {
        return this.getGenre().equalsIgnoreCase(genreName);
    }

    @Override
    public boolean matchesArtist(final String artistName) {
        return this.getArtist().equalsIgnoreCase(artistName);
    }

    @Override
    public boolean matchesReleaseYear(final String year) {
        return filterByYear(this.getReleaseYear(), year);
    }

    private static boolean filterByYear(final int year, final String query) {
        if (query.startsWith("<")) {
            return year < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return year > Integer.parseInt(query.substring(1));
        } else {
            return year == Integer.parseInt(query);
        }
    }

    /**
     * Set the number of likes for the song.
     *
     * @param numberOfLikes The number of likes to set.
     */
    public void setLikes(final Integer numberOfLikes) {
        this.likes = numberOfLikes;
    }

    /**
     * Increment the number of likes for the song.
     */
    public void like() {
        likes++;
    }

    /**
     * Decrement the number of likes for the song.
     */
    public void dislike() {
        likes--;
    }
}

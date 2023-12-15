package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.player.Player;
import app.utils.Enums;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Represents an artist user.
 */
public class Artist extends User {
    private final List<Album> albums;
    @Getter
    private  List<Song> songsForNewAlbum;
    /**
     * Retrieves a list of albums created by the artist.
     *
     * @return A list of albums.
     */
    public List<Album> getAlbums() {
        return albums;
    }
    /**
     * Constructor for creating an artist user.
     *
     * @param username The artist's username.
     * @param age      The artist's age.
     * @param city     The artist's city.
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        this.albums = new ArrayList<>();
        setUserType(Enums.UserType.ARTIST);
    }
    /**
     * Adds a new album for the artist.
     *
     * @param name        The name of the album.
     * @param releaseYear The release year of the album.
     * @param description The description of the album.
     * @param songInputs  The list of songs to be added to the album.
     * @return A message indicating the success or failure of the operation.
     */
    public String addAlbum(final String name, final int releaseYear,
                           final String description, final List<SongInput> songInputs) {
        setUserType(Enums.UserType.ARTIST);

        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(name)) {
                return getUsername() + " has another album with the same name.";
            }
        }

        // Check for duplicate songs within the provided list
        Set<String> uniqueSongTitles = new HashSet<>();
        for (SongInput songInput : songInputs) {
            if (!uniqueSongTitles.add(songInput.getName().toLowerCase())) {
                return getUsername() + " has the same song at least twice in this album.";
            }
        }

        // Create a new list of songs for this album
        songsForNewAlbum = new ArrayList<>();
        List<Song> adminSongList = Admin.getSongs();

        for (SongInput songInput : songInputs) {
            Song song = new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist());

            // Add to the album's song list
            songsForNewAlbum.add(song);

            // Also add to the global song list
            adminSongList.add(song);
        }

        // Update the global song list in Admin
        Admin.setSongList(adminSongList);

        // Create the new album with its specific list of songs
        Album newAlbum = new Album(name, this.getUsername(),
                description, releaseYear, songsForNewAlbum);
        albums.add(newAlbum);
        Admin.addGlobalAlbum(newAlbum);
        return getUsername() + " has added new album successfully.";
    }

    /**
     * Retrieves a list of albums created by the artist.
     *
     * @return A list of albums.
     */
    public List<Album> showAlbums() {
        return albums;
    }

    @Getter
    @Setter
    public static class PartialAlbum {
        private String name;
        private List<String> songs = new ArrayList<>();
    }
    /**
     * Retrieves a list of formatted albums with their song names.
     *
     * @return A list of formatted albums.
     */
    public List<PartialAlbum> getFormattedAlbums() {
        List<PartialAlbum> partialAlbums = new ArrayList<>();

        for (Album album : this.albums) {
            PartialAlbum albumsObject = new PartialAlbum();
            for (Song song : album.getSongs()) {
                albumsObject.getSongs().add(song.getName());
            }
            albumsObject.setName(album.getName());
            partialAlbums.add(albumsObject);
        }

        return partialAlbums;
    }
    /**
     * Removes an album created by the artist.
     *
     * @param albumName The name of the album to be removed.
     * @return A message indicating the success or failure of the operation.
     */
    public String removeAlbum(final String albumName) {
        // Check if the artist has an album with the given name
        Album albumToRemove = null;
        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(albumName)) {
                albumToRemove = album;
                break;
            }
        }
        if (albumToRemove == null) {
            return getUsername() + " doesn't have an album with the given name.";
        }

        List<Player> activePlayers = Admin.getActivePlayers();
        // Check if the album or its songs are being played
        if (Admin.isArtistContentBeingPlayed(Artist.this, activePlayers)) {
            return getUsername() + " can't delete this album.";
        }

        // Remove the album
        albums.remove(albumToRemove);
        // Optional: Remove songs from the global song list
        List<Song> adminSongList = Admin.getSongs();
        adminSongList.removeAll(albumToRemove.getSongs());
        Admin.setSongList(adminSongList);

        return getUsername() + " deleted the album successfully.";
    }

    private static List<Event> events = new ArrayList<>();
    /**
     * Retrieves a list of events associated with the artist.
     *
     * @return A list of events.
     */
    public static List<Event> getEvents() {
        return events;
    }
    /**
     * Adds a new event for the artist.
     *
     * @param name        The name of the event.
     * @param description The description of the event.
     * @param dateString  The date of the event as a string in "dd-MM-yyyy" format.
     * @return A message indicating the success or failure of the operation.
     */
    public String addEvent(final String name, final String description, final String dateString) {
        setUserType(Enums.UserType.ARTIST);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        Date eventDate;
        try {
            eventDate = sdf.parse(dateString);

            // Check for logical date constraints
            Calendar cal = Calendar.getInstance();
            cal.setTime(eventDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
            int day = cal.get(Calendar.DAY_OF_MONTH);

            if (year < 1900 || year > 2023 ||
                    month > 12 ||
                    (month == 2 && day > 28) ||
                    day > 31) {
                return getUsername() + " has an event with an invalid date.";
            }
        } catch (ParseException e) {
            return "Event for " + getUsername() + " does not have a valid date.";
        }

        // Check for existing event with the same name
        for (Event existingEvent : events) {
            if (existingEvent.getName().equalsIgnoreCase(name)) {
                return getUsername() + " has another event with the same name.";
            }
        }

        // Add the new event
        Event newEvent = new Event(name, description, eventDate);
        events.add(newEvent);
        return getUsername() + " has added new event successfully.";
    }

    @Getter
    @Setter
    public static class Event {
        private String name;
        private String description;
        private Date date;

        // Constructor
        public Event(final String name, final String description, final Date date) {
            this.name = name;
            this.description = description;
            this.date = date;
        }
    }
    /**
     * Removes an event associated with the artist.
     *
     * @param eventName The name of the event to be removed.
     * @return A message indicating the success or failure of the operation.
     */
    public String removeEvent(final String eventName) {
        Iterator<Event> iterator = events.iterator();

        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.getName().equalsIgnoreCase(eventName)) {
                iterator.remove();
                return getUsername() + " deleted the event successfully.";
            }
        }

        return getUsername() + " doesn't have an event with the given name.";
    }

    @Getter
    public class Merchandise {
        private String name;
        private String description;
        private double price;
        /**
         * Constructor for creating merchandise.
         *
         * @param name        The name of the merchandise.
         * @param description The description of the merchandise.
         * @param price       The price of the merchandise.
         */
        public Merchandise(final String name, final String description, final double price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }

    }

    private static List<Merchandise> merchList = new ArrayList<>();
    /**
     * Retrieves a list of merchandise associated with the artist.
     *
     * @return A list of merchandise.
     */
    public static List<Merchandise> getMerchList() {
        return merchList;
    }

    /**
     * Adds new merchandise for the artist.
     *
     * @param name        The name of the merchandise.
     * @param description The description of the merchandise.
     * @param price       The price of the merchandise.
     * @return A message indicating the success or failure of the operation.
     */
    public String addMerch(final String name, final String description,
                           final double price) {
        setUserType(Enums.UserType.ARTIST);
        setUserType(Enums.UserType.ARTIST);
        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }

        for (Merchandise merch : merchList) {
            if (merch.getName().equalsIgnoreCase(name)) {
                return getUsername() + " has merchandise with the same name.";
            }
        }

        merchList.add(new Merchandise(name, description, price));
        return getUsername() + " has added new merchandise successfully.";
    }
}

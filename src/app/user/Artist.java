package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.player.Player;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Artist extends User {
    private final List<Album> albums;
    @Getter
    public  List<Song> songsForNewAlbum;
    public List<Album> getAlbums() {
        return albums;
    }

    public Artist(String username, int age, String city) {
        super(username, age, city);
        this.albums = new ArrayList<>();
        setUserType(Enums.UserType.ARTIST);
    }

    public String addAlbum(String name, int releaseYear, String description, List<SongInput> songInputs) {
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
        Album newAlbum = new Album(name, this.getUsername(), description, releaseYear, songsForNewAlbum);
        albums.add(newAlbum);
        Admin.addGlobalAlbum(newAlbum);
        return getUsername() + " has added new album successfully.";
    }


    public List<Album> showAlbums() {
        return albums;
    }
    @Getter
    @Setter
    public static class PartialAlbum {
        private String name;
        private List<String> songs = new ArrayList<>();

    }
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

    public String removeAlbum(String albumName) {
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

        List<Player> activePlayers  = Admin.getActivePlayers();
        boolean isAlbumInUse = Admin.isArtistContentBeingPlayed(this, activePlayers); // Assuming activePlayers is a list of Player objects

        if (isAlbumInUse) {
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

    public static List<Event> getEvents() {
        return events;
    }

    public String addEvent(String name, String description, String dateString) {
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
    public static class Event{
        private String name;
        private String description;
        private Date date;

        // Constructor
        public Event(String name, String description, Date date) {
            this.name = name;
            this.description = description;
            this.date = date;
        }
    }
    @Getter
    public class Merchandise {
        private String name;
        private String description;
        private double price;

        public Merchandise(String name, String description, double price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }

    }
    private static  List<Merchandise> merchList = new ArrayList<>();

    public static List<Merchandise> getMerchList() {
        return merchList;
    }

    public String addMerch(String name, String description, double price) {
        setUserType(Enums.UserType.ARTIST);
        if (price < 0) {
            return  "Price for merchandise can not be negative.";
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



package app.user;

import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Artist extends User {
    private List<Album> albums;

    public Artist(String username, int age, String city) {
        super(username, age, city);
        this.albums = new ArrayList<>();
        setUserType(Enums.UserType.ARTIST);
    }

    // Method to add an album to the artist's profile
    public String addAlbum(String name, int releaseYear, String description, List<SongInput> songs) {
        // Check for an album with the same name
        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(name)) {
                return getUsername() + " has another album with the same name.";
            }
        }

        // Check for duplicate songs within the provided list
        Set<String> uniqueSongTitles = new HashSet<>();
        for (SongInput song : songs) {
            if (!uniqueSongTitles.add(song.getName().toLowerCase())) {
                return getUsername() + " has the same song at least twice in this album.";
            }
        }

        Album newAlbum = new Album(name, this.getUsername(), description, releaseYear, songs);
        albums.add(newAlbum);
        return getUsername() + " has added new album successfully.";
    }

    // Method to display all albums by the artist
    public List<Album> showAlbums() {
        return albums;
    }
    public ArrayNode getFormattedAlbums() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode albumsArray = objectMapper.createArrayNode();

        for (Album album : this.albums) {
            ObjectNode albumNode = objectMapper.createObjectNode();
            albumNode.put("name", album.getName());
            ArrayNode songsArray = objectMapper.createArrayNode();
            for (Song song : album.getSongs()) {
                songsArray.add(song.getName());
            }
            albumNode.set("songs", songsArray);
            albumsArray.add(albumNode);
        }

        return albumsArray;
    }
    // Method to remove an album from the artist's profile
    public String removeAlbum(String albumName) {
        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(albumName)) {
                albums.remove(album);
                return getUsername() + " deleted the album successfully.";
            }
        }
        return getUsername() + " doesn't have an album with the given name.";
    }
}

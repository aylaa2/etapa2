package Pagination;

import Pagination.Page;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;
import app.utils.Enums;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends Page {
    private List<Song> topLikedSongs; // Top 5 liked songs
    private List<Playlist> topPlaylists; // Top 5 playlists with most likes
    private Enums.UserType userType;
    private  User user;
    public HomePage(String owner, User user) {
        super(owner);
        this.topLikedSongs = new ArrayList<>();
        this.topPlaylists = new ArrayList<>();
        this.userType = Enums.UserType.USER;
        this.user = user;
    }

    @Override
    public String displayPage() {
        StringBuilder sb = new StringBuilder();
        List<String> userLikedSongs = user.getLikedSongs().stream()
                .map(Song::getName)
                .collect(Collectors.toList());

        sb.append("Liked songs:\n\t");
        sb.append(formatList(userLikedSongs));
        sb.append("\n\nFollowed playlists:\n\t");

        List<String> formattedPlaylists = user.getFollowedPlaylists().stream()
                .map(p -> p.getName()) // Assuming p.getName() gives the playlist's name
                .collect(Collectors.toList());
        sb.append(formatList(formattedPlaylists));
        return sb.toString();
    }
    private String formatList(List<String> items) {
        if (items.isEmpty()) {
            return "[]";
        } else {
            return "[" + String.join(", ", items) + "]";
        }
    }
    private String formatSongList(List<Song> songs) {
        // Format each song name in the list
        return "[" + songs.stream()
                .map(Song::getName)
                .collect(Collectors.joining(", ")) + "]";
    }

    private String formatPlaylistList(List<Playlist> playlists) {
        // Format each playlist as 'playlistName - ownerName'
        return "[" + playlists.stream()
                .map(p -> p.getName() + " - " + p.getOwner())
                .collect(Collectors.joining(", ")) + "]";
    }
}

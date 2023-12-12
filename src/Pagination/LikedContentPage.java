package Pagination;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;
import app.utils.Enums;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LikedContentPage extends Page {
    private List<Song> likedSongs; // All liked songs
    private List<Playlist> followedPlaylists; // All followed playlists
    private Enums.UserType usertype;
    private User user;

    public LikedContentPage(String owner, User user) {
        super(owner);
        this.likedSongs = new ArrayList<>();
        this.followedPlaylists = new ArrayList<>();
        this.usertype = Enums.UserType.USER;
        this.user = user;
    }

    @Override
    public String displayPage() {
        StringBuilder sb = new StringBuilder();
        List<String> userLikedSongss = user.getLikedSongs().stream()
                .map(Song::getName)
                .collect(Collectors.toList());

        sb.append("Liked songs:\n\t");
        sb.append(formatList(userLikedSongss));
        sb.append("\n\nFollowed playlists:\n\t");
        List<String> formattedPlaylistss = user.getFollowedPlaylists().stream()
                .map(p -> {
                    String playlistInfo = p.getName();
                    if (user.isOnline()) {
                        playlistInfo += " - " + p.getOwner();  // Append the owner's username
                    }
                    return playlistInfo;
                })
                .collect(Collectors.toList());
        sb.append(formatList(formattedPlaylistss));
        return "bla";
    }

    private String formatList(List<String> items) {
        if (items.isEmpty()) {
            return "[]";
        } else {
            return "[" + String.join(", ", items) + "]";
        }
    }
}


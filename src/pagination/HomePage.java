package pagination;

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

    public HomePage(final User owner) {
        super(owner);
        this.topLikedSongs = new ArrayList<>();
        this.topPlaylists = new ArrayList<>();
        this.userType = Enums.UserType.USER;
    }

    /**
     * Generates the content to be displayed on the user's home page.
     *
     * @return A string representing the content of the home page.
     */
    @Override
    public String displayPage() {
        StringBuilder sb = new StringBuilder();
        List<String> userLikedSongs = getOwner().getLikedSongs().stream()
                .map(Song::getName)
                .collect(Collectors.toList());

        sb.append("Liked songs:\n\t");
        sb.append(formatList(userLikedSongs));

        List<String> userFollowedPlaylists = getOwner().getFollowedPlaylists().stream()
                .map(Playlist::getName) // Convert to names
                .collect(Collectors.toList());

        sb.append("\n\nFollowed playlists:\n\t");

        if (userFollowedPlaylists.isEmpty()) {
            sb.append("[]");
        } else {
            sb.append(formatList(userFollowedPlaylists));
        }

        return sb.toString();
    }

    private String formatList(final List<String> items) {
        if (items.isEmpty()) {
            return "[]";
        } else {
            return "[" + String.join(", ", items) + "]";
        }
    }
}

package pagination;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;
import app.utils.Enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends Page {
    private List<Song> topLikedSongs; // Top 5 liked songs
    private List<Playlist> topPlaylists; // Top 5 playlists with most likes
    private Enums.UserType userType;
    private static final int LIMIT = 5;

    public HomePage(final User owner) {
        super(owner);
        this.topLikedSongs = new ArrayList<>();
        this.topPlaylists = owner.getFollowedPlaylists();
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

        ArrayList<Song> userLikedSongs = new ArrayList<>();
        userLikedSongs.addAll(getOwner().getLikedSongs());
        Collections.sort(userLikedSongs,
                Comparator.comparing(Song::getLikes).reversed());
        List<String> userLikedSongsTop5 = userLikedSongs.stream()
                .map(Song::getName)
                .limit(LIMIT)
                .collect(Collectors.toList());

        sb.append("Liked songs:\n\t");
        sb.append(formatList(userLikedSongsTop5));

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

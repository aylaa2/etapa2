package pagination;

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

    public LikedContentPage(final User owner) {
        super(owner);
        this.likedSongs = new ArrayList<>();
        this.followedPlaylists = new ArrayList<>();
        this.usertype = Enums.UserType.USER;
    }
    /**
     * Generates and returns a formatted display of the liked songs and followed playlists
     * associated with the owner of this LikedContentPage.
     *
     * @return A formatted string containing liked songs and followed playlists.
     */
    @Override
    public String displayPage() {
        StringBuilder sb = new StringBuilder();
        List<String> userLikedSongs1 = getOwner().getLikedSongs().stream()
                .map(song -> song.getName() + " - " + song.getArtist())
                .collect(Collectors.toList());

        sb.append("Liked songs:\n\t");
        sb.append(formatList(userLikedSongs1));
        sb.append("\n\nFollowed playlists:\n\t");

        // Formatting followed playlists
        List<String> formattedPlaylists1 = getOwner().getFollowedPlaylists().stream()
                .map(p -> {
                    String playlistInfo = p.getName();
                    if (getOwner().isOnline()) {
                        playlistInfo += " - " + p.getOwner();
                    }
                    return playlistInfo;
                })
                .collect(Collectors.toList());
        sb.append(formatList(formattedPlaylists1));
        return sb.toString();
    }

    private String formatList(final List<String> items) {  // Marked 'items' parameter as final
        if (items.isEmpty()) {
            return "[]";
        } else {
            return "[" + String.join(", ", items) + "]";
        }
    }
}

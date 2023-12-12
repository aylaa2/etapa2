package Pagination;

import app.Admin;
import app.audio.Collections.Album;
import app.user.Artist;
import app.user.Host;
import app.utils.Enums;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistPage extends Page {
    private List<Album> albums;
    private List<Artist.Merchandise> merchandise;
    private List<Artist.Event> events;
    private Enums.UserType userType;
    private Artist user;

    public ArtistPage(String owner, Artist artist) {
        super(owner);
        this.albums = new ArrayList<>();
        this.merchandise = new ArrayList<>();
        this.events = new ArrayList<>();
        this.user = artist;
        this.userType = Enums.UserType.ARTIST;
    }

    @Override
    public String displayPage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Podcasts:\n\t[");
        if (user.searchBar.getLastSelected() instanceof Host) {
            Host host1 = (Host) user.searchBar.getLastSelected() ;
            List<Host.PartialPodcast> formattedPodcasts = host1.getFormattedPodcastss();
            boolean isFirstCategory = true;

            for (Host.PartialPodcast partialPodcast : formattedPodcasts) {
                // Add a comma and newline before the start of the next category, except for the first
                if (!isFirstCategory) {
                    sb.append("\n, ");
                }
                isFirstCategory = false;

                sb.append(partialPodcast.getName()).append(":\n\t");
                sb.append(formatList(partialPodcast.getEpisodes()));  // This will add [ ] around the episodes
            }

            sb.append("\n]");
        }

        sb.append("\n\nAnnouncements:\n\t");
        List<Host.Announcement> announcementList = Host.getAnnouncements();

        if (announcementList != null && !announcementList.isEmpty()) {
            sb.append("["); // Start with an opening bracket
            for (int i = 0; i < announcementList.size(); i++) {
                Host.Announcement announcement = announcementList.get(i);
                sb.append(announcement.getName()).append(":\n\t")
                        .append(announcement.getDescription())
                        .append("\n");
                if (i < announcementList.size() - 1) {
                    sb.append("\n, "); // Separate each announcement with a comma and newline
                }
            }
            sb.append("]"); // Close with a bracket
        } else {
            sb.append("[]");
        }
        return sb.toString();
    }

    private String formatList(List<String> items) {
        if (items.isEmpty()) {
            return "[]";
        } else {
            return "[" + String.join(", ", items) + "]";
        }
    }

    // Additional methods to manage albums, merchandise, and events
}

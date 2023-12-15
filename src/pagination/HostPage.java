package pagination;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.Host;
import app.utils.Enums;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class HostPage extends Page {
    private List<Podcast> podcasts;
    private List<Host.Announcement> announcements;
    private Enums.UserType usertype;

    public HostPage(final Host owner) {
        super(owner);
        this.podcasts = new ArrayList<>();
        this.announcements = new ArrayList<>();
        this.usertype = Enums.UserType.HOST;
    }
    /**
     * Generates a formatted display of the host's page content
     * including podcasts and announcements.
     *
     * @return A formatted string containing information
     * about podcasts and announcements associated with the host.
     */
    @Override
    public String displayPage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Podcasts:\n\t[");
        List<Podcast> podcasts = Admin.getAllNewPodcasts();
        if (!podcasts.isEmpty()) {
            for (int j = 0; j < podcasts.size(); j++) {
                Podcast podcast = podcasts.get(j);
                // Append the name of each podcast
                sb.append(podcast.getName()).append(":\n\t[");

                if (!podcast.getEpisodes().isEmpty()) {
                    List<Episode> episodes = podcast.getEpisodes();
                    for (int i = 0; i < episodes.size(); i++) {
                        Episode episode = episodes.get(i);
                        sb.append(episode.getName()).append(" - ").append(episode.getDescription());
                        if (i < episodes.size() - 1) {
                            sb.append(", ");
                        }
                    }
                } else {
                    // If the podcast has no episodes
                    sb.append("No episodes available");
                }
                // Close the episodes list
                sb.append("]");

                // Append a comma and newline only if it's not the last podcast
                if (j < podcasts.size() - 1) {
                    sb.append("\n, ");
                }
            }
        }

        List<String> users = Admin.getAllUsers();
        String currentHostUsername = getOwner().getUsername();

        sb.append("\n]\n\nAnnouncements:\n\t");

        for (String user : users) {
            if (getOwner().getUsername().equals(user)) {

                List<Host.Announcement> announcementList
                        = ((Host) getOwner()).getAnnouncements();
                if (announcementList != null && !announcementList.isEmpty()) {
                    // Sort the announcements by name
                    announcementList.sort(Comparator.comparing(Host.Announcement::getName));

                    sb.append("[");
                    for (int i = 0; i < announcementList.size(); i++) {
                        Host.Announcement announcement = announcementList.get(i);
                        sb.append(announcement.getName()).append(":\n\t")
                                .append(announcement.getDescription())
                                .append("\n");
                        if (i < announcementList.size() - 1) {
                            sb.append("\n, ");
                        }
                    }
                    sb.append("]");
                } else {
                    sb.append("[]");
                }

                break;
            }
        }

        return sb.toString();
    }


}



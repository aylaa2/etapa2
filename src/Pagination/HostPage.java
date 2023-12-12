package Pagination;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.Host;
import app.user.User;
import app.utils.Enums;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HostPage extends Page {
    private List<Podcast> podcasts;
    private List<Host.Announcement> announcements;
    private Enums.UserType usertype;
    private Host user;
    public HostPage(String owner, Host host) {
        super(owner);
        this.podcasts = new ArrayList<>();
        this.announcements = new ArrayList<>();
        this.usertype = Enums.UserType.HOST;
        this.user = host;
    }

    @Override
    public String displayPage() {
        StringBuilder sb = new StringBuilder();
        if (this.user == null){
            return "no user";
        }
        sb.append("Podcasts:\n\t[");
        if (user instanceof Host) {
            List<Host.PartialPodcast> formattedPodcasts = user.getFormattedPodcastss();
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

    private String formatPodcasts(List<Podcast> podcasts) {
        StringBuilder podcastBuilder = new StringBuilder("[");
        for (int i = 0; i < podcasts.size(); i++) {
            Podcast podcast = podcasts.get(i);
            podcastBuilder.append(podcast.getName()).append(":\n\t");
            podcastBuilder.append(formatEpisodes(podcast.getEpisodes()));
            if (i < podcasts.size() - 1) {
                podcastBuilder.append(",\n\t");
            }
        }
        podcastBuilder.append("]");
        return podcastBuilder.toString();
    }

    private String formatEpisodes(List<Episode> episodes) {
        return "[" + episodes.stream()
                .map(e -> e.getName() + " - " + e.getDescription())
                .collect(Collectors.joining(", ")) + "]";
    }

    private String formatAnnouncements(List<Host.Announcement> announcements) {
        StringBuilder announcementBuilder = new StringBuilder("[");
        for (int i = 0; i < announcements.size(); i++) {
            Host.Announcement announcement = announcements.get(i);
            announcementBuilder.append(announcement.getName()).append("\n\t");
            announcementBuilder.append(announcement.getDescription());
            if (i < announcements.size() - 1) {
                announcementBuilder.append("\n, ");
            }
        }
        announcementBuilder.append("]");
        return announcementBuilder.toString();
    }
}



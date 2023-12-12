package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import app.utils.Enums;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

@Getter
public class Host extends User {
    private final List<Podcast> podcasts;
    public List<Episode> episodesForNewPodcast;
    public Host(String username, int age, String city) {
        super(username, age, city);
        this.episodesForNewPodcast = new ArrayList<>();
        this.podcasts = new ArrayList<>();
        setUserType(Enums.UserType.HOST);
    }


    public List<Podcast> showPodcasts() {
        return podcasts;
    }

    // Add a new podcast
    public String addPodcast(String name, List<EpisodeInput> episodeInputs) {
        setUserType(Enums.UserType.HOST);
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equalsIgnoreCase(name)) {
                return getUsername() + " has another podcast with the same name.";
            }
        }
        // Check for duplicate episodes within the provided list
        Set<String> uniqueEpisodeTitles = new HashSet<>();
        for (EpisodeInput episodeInput : episodeInputs) {
            if (!uniqueEpisodeTitles.add(episodeInput.getName().toLowerCase())) {
                return getUsername() + " has the same episode at least twice in this podcast.";
            }
        }
        // Create a new list of episodes for this podcast
        episodesForNewPodcast = new ArrayList<>();
        List<Episode> adminEpisodeList = Admin.getEpisode();

        for (EpisodeInput episodeInput : episodeInputs) {
            Episode episode = new Episode(episodeInput.getName(), episodeInput.getDuration(), episodeInput.getDescription());
            episodesForNewPodcast.add(episode);
            adminEpisodeList.add(episode);
        }


        Admin.setEpisodetList(adminEpisodeList);

        Podcast newPodcast = new Podcast(name, this.getUsername(), episodesForNewPodcast);
        podcasts.add(newPodcast);
        Admin.addGlobalPodcast(newPodcast);
        Admin.addNewPodcast(newPodcast);
        return getUsername() + " has added new podcast successfully.";
    }

    @Getter
    public static class Announcement {
        private String name;
        private String description;

        public Announcement(String name, String description) {
            this.name = name;
            this.description = description;
        }
        @Override
        public String toString() {
            return name + ":\n\t" + description + "\n";
        }
    }

    public static List<Announcement> announcements = new ArrayList<>();

    public static List<Announcement> getAnnouncements() {

        return announcements;
    }

    public static List<Announcement> getAllAnnouncements() {
        return announcements;
    }



    public String addAnnouncement(String name, String description) {
        setUserType(Enums.UserType.HOST);
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return getUsername() + " has already added an announcement with this name.";
            }
        }

        announcements.add(new Announcement(name, description));
        return getUsername() + " has successfully added new announcement.";
    }

    public String removeAnnouncement(String name) {
        setUserType(Enums.UserType.HOST);
        Iterator<Announcement> iterator = announcements.iterator();
        while (iterator.hasNext()) {
            Announcement announcement = iterator.next();
            if (announcement.getName().equals(name)) {
                iterator.remove();
                return getUsername() + " has successfully deleted the announcement.";
            }
        }
        return getUsername() + " has no announcement with the given name.";
    }


    @Getter
    @Setter
    public static class PartialPodcast {
        private String name;
        private List<String> episodes = new ArrayList<>();

    }

    public List<PartialPodcast> getFormattedPodcasts() {
        List<PartialPodcast> partialPodcasts = new ArrayList<>();

        for (Podcast podcast : this.podcasts) { // Access the instance field
            PartialPodcast partialPodcast = new PartialPodcast();
            partialPodcast.setName(podcast.getName());

            for (Episode episode : podcast.getEpisodes()) {
                partialPodcast.getEpisodes().add(episode.getName());
            }

            partialPodcasts.add(partialPodcast);
        }

        return partialPodcasts;
    }

    public List<PartialPodcast> getFormattedPodcastss() {
        List<PartialPodcast> partialPodcasts = new ArrayList<>();

        for (Podcast podcast : this.podcasts) { // Access the instance field
            PartialPodcast partialPodcast = new PartialPodcast();
            partialPodcast.setName(podcast.getName());

            for (Episode episode : podcast.getEpisodes()) {
                partialPodcast.getEpisodes().add(episode.getName() + " - " + episode.getDescription());
            }

            partialPodcasts.add(partialPodcast);
        }

        return partialPodcasts;
    }


    public String removePodcast(String podcastName) {
        setUserType(Enums.UserType.HOST);

        // Check if the host has a podcast with the given name
        Podcast podcastToRemove = null;
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equalsIgnoreCase(podcastName)) {
                podcastToRemove = podcast;
                break;
            }
        }

        if (podcastToRemove == null) {
            return getUsername() + " doesn't have a podcast with the given name.";
        }

        List<Player> activePlayers  = Admin.getActivePlayers();
        boolean isPodcastInUse = Admin.isHostContentBeingPlayed(this, activePlayers);

        if (isPodcastInUse) {
            return getUsername() + " can't delete this podcast.";
        }

        // Remove the podcast
        podcasts.remove(podcastToRemove);
        // Optional: Remove episodes from the global episode list
        // Implement this as needed

        return getUsername() + " deleted the podcast successfully.";
    }


}

package app.user;

import app.Admin;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.player.Player;
import app.utils.Enums;
import fileio.input.EpisodeInput;
import lombok.Getter;
import lombok.Setter;


import java.util.*;

/**
 * Represents a user with the role of a host who can manage podcasts and announcements.
 */
@Getter
public class Host extends User {
    private final List<Podcast> podcasts;
    private List<Episode> episodesForNewPodcast;
    @Getter
    private  List<Announcement> announcements;
    /**
     * Initializes a new Host instance.
     *
     * @param username The username of the host.
     * @param age      The age of the host.
     * @param city     The city of the host.
     */
    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        this.episodesForNewPodcast = new ArrayList<>();
        this.podcasts = new ArrayList<>();
        this.announcements = new ArrayList<>();
        setUserType(Enums.UserType.HOST);
    }

    /**
     * Returns the list of podcasts associated with this host.
     *
     * @return The list of podcasts.
     */
    public List<Podcast> showPodcasts() {
        return podcasts;
    }

    /**
     * Adds a new podcast to the host's list of podcasts.
     *
     * @param name          The name of the podcast.
     * @param episodeInputs The list of episode inputs for the new podcast.
     * @return A message indicating the result of the operation.
     */
    public String addPodcast(final String name, final List<EpisodeInput> episodeInputs) {
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
            Episode episode = new Episode(episodeInput.getName(),
                    episodeInput.getDuration(), episodeInput.getDescription());
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
    /**
     * Represents an announcement made by the host.
     */
    @Getter
    public static class Announcement {
        private String name;
        private String description;
        private User user;
        /**
         * Initializes a new Announcement instance.
         *
         * @param name        The name of the announcement.
         * @param description The description of the announcement.
         * @param user        The host who made the announcement.
         */
        public Announcement(final String name, final String description, final User user) {
            this.user = user;
            this.name = name;
            this.description = description;
        }

        /**
         * Returns a formatted string representation of the announcement.
         *
         * @return The formatted announcement.
         */
        @Override
        public String toString() {
            return name + ":\n\t" + description + "\n";
        }
    }


    /**
     * Returns the list of announcements made by this host.
     *
     * @return The list of announcements.
     */
    public  List<Announcement> getAnnouncements() {
        return announcements;
    }
    /**
     * Adds a new announcement made by this host.
     *
     * @param name        The name of the announcement.
     * @param description The description of the announcement.
     * @return A message indicating the result of the operation.
     */
    public String addAnnouncement(final String name, final String description) {
        setUserType(Enums.UserType.HOST);

        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return getUsername() + " has already added an announcement with this name.";
            }
        }

        Announcement newAnnouncement = new Announcement(name, description, this);
        announcements.add(newAnnouncement);

        return getUsername() + " has successfully added new announcement.";
    }
    /**
     * Removes an announcement made by this host.
     *
     * @param host      The host who made the announcement.
     * @param nameAnounc The name of the announcement to be removed.
     * @return A message indicating the result of the operation.
     */
    public String removeAnnouncement(final Host host, final String nameAnounc) {

        for (Announcement announcementss : host.getAnnouncements()) {
            if (announcementss.getName().equals(nameAnounc)) {
                announcements.remove(announcementss);
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
    /**
     * Retrieves a list of formatted podcasts with only their names.
     *
     * @return A list of PartialPodcast instances with names only.
     */
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
    /**
     * Retrieves a list of formatted podcasts with names and descriptions.
     *
     * @return A list of PartialPodcast instances with names and descriptions.
     */
    public List<PartialPodcast> getFormattedPodcastss() {
        List<PartialPodcast> partialPodcasts = new ArrayList<>();

        for (Podcast podcast : this.podcasts) { // Access the instance field
            PartialPodcast partialPodcast = new PartialPodcast();
            partialPodcast.setName(podcast.getName());

            for (Episode episode : podcast.getEpisodes()) {
                partialPodcast.getEpisodes().add(episode.getName()
                        + " - " + episode.getDescription());
            }

            partialPodcasts.add(partialPodcast);
        }

        return partialPodcasts;
    }

    /**
     * removePodcast
     *
     * @return A podcastName
     */
    public String removePodcast(final String podcastName) {
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

        List<Player> activePlayers = Admin.getActivePlayers();
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

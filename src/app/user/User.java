package app.user;

import Pagination.*;
import app.Admin;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User extends LibraryEntry {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private final Player player;
    public final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    protected Enums.UserType userType;
    private boolean isOnline;
    private Page CurrentPage;
    public void deletefollowedplaylist(){
        followedPlaylists.clear();
    }
    public boolean hasAudioCollection(AudioCollection collection) {
        return collection.matchesOwner(this.getUsername());
    }
    public boolean hasAudioFile(AudioFile collection) {
        return collection.matchesOwner(this.getUsername());
    }
    public Enums.UserType getUserType() {
        return userType;
    }

    public boolean isOnline() {
        return isOnline;
    }
    public void setUserType(Enums.UserType userType) {
        this.userType = userType;
    }
    public User(String username, int age, String city) {
        super(username);
        this.username = username;
        this.age = age;
        this.city = city;
        this.isOnline = true;
        setUserType(Enums.UserType.USER);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        this.setCurrentPageType("HomePage");
    }

    @Override
    public boolean matchesName(String name) {
        return this.username.toLowerCase().startsWith(name);
    }
    public ArrayList<String> search(Filters filters, String type) {

        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }
        return results;
    }

    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }
        lastSearched = false;
        LibraryEntry selected = searchBar.select(itemNumber);

        // Check if selected is null before proceeding
        if (selected == null) {
            return "The selected ID is too high.";
        }
        lastSearched = false;

        String lastSearchType = searchBar.getLastSearchType();

        if (lastSearchType != null) {
            if ("host".equals(lastSearchType)) {
                setCurrentPageType("HostPage");
                return "Successfully selected %s's page.".formatted(selected.getName());
            } else if ("artist".equals(lastSearchType)) {
                setCurrentPageType("ArtistPage");
                return "Successfully selected %s's page.".formatted(selected.getName());
            }
        }

        if (selected == null) {
            return "The selected ID is too high.";
        }

        return "Successfully selected %s.".formatted(selected.getName());
    }

    //player.getType().equals("playlist")
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());

        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }



    public String playPause() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before attempting to pause or resume playback.";

        player.pause();

        if (player.getPaused())
            return "Playback paused successfully.";
        else
            return "Playback resumed successfully.";
    }

    public String repeat() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before setting the repeat status.";

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch(repeatMode) {
            case NO_REPEAT -> repeatStatus = "no repeat";
            case REPEAT_ONCE -> repeatStatus = "repeat once";
            case REPEAT_ALL -> repeatStatus = "repeat all";
            case REPEAT_INFINITE -> repeatStatus = "repeat infinite";
            case REPEAT_CURRENT_SONG -> repeatStatus = "repeat current song";
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    public String shuffle(Integer seed) {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before using the shuffle function.";

        if (!player.getType().equals("playlist"))
            return "The loaded source is not a playlist or an album.";

        player.shuffle(seed);

        if (player.getShuffle())
            return "Shuffle function activated successfully.";
        return "Shuffle function deactivated successfully.";
    }

    public String forward() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before attempting to forward.";

        if (!player.getType().equals("podcast"))
            return "The loaded source is not a podcast.";

        player.skipNext();

        return "Skipped forward successfully.";
    }

    public String backward() {
        if (player.getCurrentAudioFile() == null)
            return "Please select a source before rewinding.";

        if (!player.getType().equals("podcast"))
            return "The loaded source is not a podcast.";

        player.skipPrev();

        return "Rewound successfully.";
    }

    public String like() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before liking or unliking.";

        if (!player.getType().equals("song") && !player.getType().equals("playlist"))
            return "Loaded source is not a song.";

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    public String next() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before skipping to the next track.";

        player.next();

        if (player.getCurrentAudioFile() == null)
            return "Please load a source before skipping to the next track.";

        return "Skipped to next track successfully. The current track is %s.".formatted(player.getCurrentAudioFile().getName());
    }

    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        // Update this line to handle when the source is an album
        String currentTrackName = player.getCurrentAudioFile() != null ? player.getCurrentAudioFile().getName() : "No track";
        return "Returned to previous track successfully. The current track is %s.".formatted(currentTrackName);
    }


    public String createPlaylist(String name, int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name)))
            return "A playlist with the same name already exists.";

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    public String addRemoveInPlaylist(int Id) {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before adding to or removing from the playlist.";

        if (player.getType().equals("podcast"))
            return "The loaded source is not a song.";

        if (Id > playlists.size())
            return "The specified playlist does not exist.";

        Playlist playlist = playlists.get(Id - 1);

        if (playlist.containsSong((Song)player.getCurrentAudioFile())) {
            playlist.removeSong((Song)player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song)player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    public String switchPlaylistVisibility(Integer playlistId) {
        if (playlistId > playlists.size())
            return "The specified playlist ID is too high.";

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null)
            return "Please select a source before following or unfollowing.";

        if (!type.equals("playlist"))
            return "The selected source is not a playlist.";

        Playlist playlist = (Playlist)selection;

        if (playlist.getOwner().equals(username))
            return "You cannot follow or unfollow your own playlist.";

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }
    public void deleteFollowedPlaylist(ArrayList<Playlist> playlistsToDelete) {

        for (Playlist playlist : playlistsToDelete) {
            if (followedPlaylists.contains(playlist)) {
                followedPlaylists.remove(playlist); // Remove the playlist from followedPlaylists
                playlist.decreaseFollowers();       // Decrease followers of the playlist
            }
        }
    }


    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    public void simulateTime(int time) {
        player.simulatePlayer(time);
    }


    public void switchStatus() {
        this.isOnline = !this.isOnline;
        if (!isOnline) {
            this.player.pause();
        }
    }

    public static String addUser(String username, int age, String city, Enums.UserType userType) {
        // Check if user already exists
        User existingUser = Admin.getUser(username);
        if (existingUser != null) {
            return "The username " + username + " is already taken.";
        }

        User newUser;
        switch (userType) {
            case ARTIST:
                newUser = new Artist(username, age, city);
                break;
            case HOST:
                newUser = new Host(username, age, city);
                break;
            case USER:
            default:
                newUser = new User(username, age, city);
                break;
        }
        Admin.addUser(newUser);
        newUser.setCurrentPageType("HomePage");

        return "The username " + username + " has been added successfully.";
    }
    @Setter
    @Getter
    private  String currentPageType;
//    public Enums.PageType getCurrentPageType() {
////        if (this.getUserType() == Enums.UserType.ARTIST) {
////            this.setCurrentPageType(Enums.PageType.ARTISTPAGE);
////        } else if (this.getUserType() == Enums.UserType.HOST) {
////            this.setCurrentPageType(Enums.PageType.HOSTPAGE); // Set to HOSTPAGE for HOST users
////        } else if (this.getUserType() == Enums.UserType.USER) {
////            this.setCurrentPageType(Enums.PageType.HOMEPAGE);
////        }
//        return this.currentPageType;
//    }

    public String getCurrentPageType() {
        return currentPageType;
    }

    public void setCurrentPageType(String currentPageType) {
        this.currentPageType = currentPageType;
    }



    public String printCurrentPage(User user, Enums.UserType userType) {
        String pageContent = "";
        if (currentPageType == null) {
            return "Page type is not set.";
        }

        switch (user.getCurrentPageType()) {
            case "ArtistPage":
                Artist artist = (Artist) user;
                ArtistPage artistPage = new ArtistPage(user.getUsername(), artist);
                // Populate artistPage with necessary data
                pageContent = artistPage.displayPage();

                break;
            case "homePage":
                HomePage homePage = new HomePage(user.getUsername(), user);
                // Populate homePage with liked songs and followed playlists
                // For example: homePage.setLikedSongs(...), homePage.setFollowedPlaylists(...), etc.
                pageContent = homePage.displayPage();
                break;
            case "LikedContentPage":
                LikedContentPage likedContentPage = new LikedContentPage(user.getUsername(), user);
                // Populate likedContentPage with necessary data
                pageContent = likedContentPage.displayPage();
                break;
            case "HostPage":
                Host host = (Host) user;
                HostPage hostPage = new HostPage(user.getUsername(), host);
                // Populate hostPage with necessary data
                pageContent = hostPage.displayPage();
                break;

        }
        return pageContent;
    }


    public String printCurrentPagess(User user) {

        if (currentPageType == null) {
            return "Page type is not set.";
        }
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }
        StringBuilder sb = new StringBuilder();

        switch (user.getCurrentPageType()) {
            case "HomePage":
                // Retrieve liked songs specific to the user
                List<String> userLikedSongs = user.getLikedSongs().stream()
                        .map(Song::getName)
                        .collect(Collectors.toList());

                sb.append("Liked songs:\n\t");
                sb.append(formatList(userLikedSongs));
                sb.append("\n\nFollowed playlists:\n\t");

                List<String> formattedPlaylists = user.getFollowedPlaylists().stream()
                        .map(p -> p.getName())
                        .collect(Collectors.toList());
                sb.append(formatList(formattedPlaylists));

                break;

            case "LikedContentPage":

                // Formatting liked songs to include the artist's name
                List<String> userLikedSongs1 = user.getLikedSongs().stream()
                        .map(song -> song.getName() + " - " + song.getArtist()) // Assuming getArtistName() exists
                        .collect(Collectors.toList());

                sb.append("Liked songs:\n\t");
                sb.append(formatList(userLikedSongs1));
                sb.append("\n\nFollowed playlists:\n\t");

                // Formatting followed playlists
                List<String> formattedPlaylists1 = user.getFollowedPlaylists().stream()
                        .map(p -> {
                            String playlistInfo = p.getName();
                            if (user.isOnline) {
                                playlistInfo += " - " + p.getOwner();  // Append the owner's username
                            }
                            return playlistInfo;
                        })
                        .collect(Collectors.toList());
                sb.append(formatList(formattedPlaylists1));
                break;

            case "ArtistPage":
                sb.append("Albums:\n\t");
                List<Album> albums = Admin.getAlbums();
                if (!albums.isEmpty()) {
                    Album firstAlbum = albums.get(0); // Get the first album
                    sb.append("[").append(firstAlbum.getName()).append("]"); // Append the name of the first album
                } else {
                    sb.append("[]");
                }
                // Merchandise
                sb.append("\n\nMerch:\n\t");
                List<Artist.Merchandise> merchList = Artist.getMerchList();
                if (merchList != null && !merchList.isEmpty()) {
                    sb.append("["); // Start with an opening bracket
                    for (Artist.Merchandise merch : merchList) {
                        String formattedPrice;
                        if (merch.getPrice() == (long) merch.getPrice()) {
                            formattedPrice = String.format("%d", (long) merch.getPrice());
                        } else {
                            formattedPrice = String.format("%.2f", merch.getPrice());
                        }
                        sb.append(merch.getName()).append(" - ")
                                .append(formattedPrice).append(":\n\t")
                                .append(merch.getDescription()).append(", ");
                    }
                    sb.setLength(sb.length() - 2); // Remove the last comma and space
                    sb.append("]"); // Close with a bracket
                } else {
                    sb.append("[]");
                }

                // Events
                sb.append("\n\nEvents:\n\t");
                List<Artist.Event> eventList = Artist.getEvents(); // Fetch the events specific to the artist
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                if (eventList != null && !eventList.isEmpty()) {
                    sb.append("["); // Start with an opening bracket
                    for (Artist.Event event : eventList) {
                        sb.append(event.getName()).append(" - ")
                                .append(sdf.format(event.getDate()))
                                .append(":\n\t").append(event.getDescription()).append(", ");
                    }
                    sb.setLength(sb.length() - 2); // Remove the last comma and space
                    sb.append("]"); // Close with a bracket
                } else {
                    sb.append("[]");
                }
                break;

            case "HostPage":

                sb.append("Podcasts:\n\t[");
                List<Podcast> podcasts = Admin.getAllNewPodcasts();
                if (!podcasts.isEmpty()) {
                    for (int j = 0; j < podcasts.size(); j++) {
                        Podcast podcast = podcasts.get(j);
                        // Append the name of each podcast
                        sb.append(podcast.getName()).append(":\n\t[");

                        // Check if the podcast has episodes and append them
                        if (!podcast.getEpisodes().isEmpty()) {
                            List<Episode> episodes = podcast.getEpisodes();
                            for (int i = 0; i < episodes.size(); i++) {
                                Episode episode = episodes.get(i);
                                // Append each episode's name and description
                                sb.append(episode.getName()).append(" - ").append(episode.getDescription());
                                // Append comma only if it's not the last episode
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



                sb.append("\n]\n\nAnnouncements:\n\t");
                List<Host.Announcement> announcementList = Host.getAllAnnouncements();

                if (announcementList != null && !announcementList.isEmpty()) {
                    sb.append("["); // Start with an opening bracket
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




}



package app.user;

import pagination.*;
import app.Admin;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type User.
 */
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
    private String lastSearchedName;
    @Getter
    protected Enums.UserType userType;
    @Getter
    private Page currentPage;
    @Getter
    private User currPageOwner;
    private boolean isOnline;


    /**
     * Gets the user type.
     *
     * @return The user type.
     */
    public Enums.UserType getUserType() {
        return userType;
    }
    /**
     * Sets the last searched name.
     *
     */
    public String getLastSearchedName() {
        return lastSearchedName;
    }
    /**
     * Sets the last searched name.
     *
     * @param lastSearchedName The last searched name to set (final).
     */
    public void setLastSearchedName(final String lastSearchedName) {
        this.lastSearchedName = lastSearchedName;
    }
    /**
     * Checks if the user is currently online.
     *
     * @return True if the user is online, false otherwise.
     */
    public boolean isOnline() {
        return isOnline;
    }
    /**
     * Sets the user's type.
     *
     * @param userType The user's type to set (final).
     */
    public void setUserType(final Enums.UserType userType) {
        this.userType = userType;
    }

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
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
        lastSearchedName = "";
        this.setCurrentPageType("HomePage", this);
    }
    /**
     * Checks if the user's name matches the provided name.
     *
     * @param name The name to compare.
     * @return True if the names match, false otherwise.
     */
    @Override
    public boolean matchesName(final String name) {
        return this.username.toLowerCase().startsWith(name);
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
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

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
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

        User owner = Admin.getUser(selected.getName());
        if (lastSearchType != null) {
            if ("host".equals(lastSearchType)) {
                setCurrentPageType("HostPage", owner);
                //setCurrentPage(HostPage);
                return "Successfully selected %s's page.".formatted(selected.getName());
            } else if ("artist".equals(lastSearchType)) {
                setCurrentPageType("ArtistPage", owner);
                return "Successfully selected %s's page.".formatted(selected.getName());
            }
        }

        if (selected == null) {
            return "The selected ID is too high.";
        }

        return "Successfully selected %s."
                .formatted(selected.getName());
    }


    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected())
                .getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        if (searchBar.getLastSearchType().equals("album")) {
            AudioCollection selectedAlbum = (AudioCollection) searchBar.getLastSelected();
            if (selectedAlbum.getNumberOfTracks() == 0) {
                return "You can't load an empty album!";
            }
        }

        player.setSource(searchBar.getLastSelected(), searchBar
                .getLastSearchType());

        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }


    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch(repeatMode) {

            case NO_REPEAT:
                repeatStatus = "no repeat";
                break;
            case REPEAT_ONCE:
                repeatStatus = "repeat once";
                break;
            case REPEAT_ALL:
                repeatStatus = "repeat all";
                break;
            case REPEAT_INFINITE:
                repeatStatus = "repeat infinite";
                break;
            case REPEAT_CURRENT_SONG:
                repeatStatus = "repeat current song";
                break;
            default:
                break;
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);

    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }
        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }
        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }
        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }
        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }
        if (!player.getType().equals("song") && !player.getType().equals("playlist")) {
            return "Loaded source is not a song.";
        }
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

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }
        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        // Update this line to handle when the source is an album
        String currentTrackName = player.getCurrentAudioFile() != null ? player.
                getCurrentAudioFile().getName() : "No track";
        return "Returned to previous track successfully. The current track is %s."
                .formatted(currentTrackName);
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }
        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }
    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }
    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }
    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }
    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }
    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
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
    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time);
    }

    /**
     * Switches the status of the user between active and inactive.
     *
     */
    public void switchStatus() {
        this.isOnline = !this.isOnline;
        if (!isOnline) {
            this.player.pause();
        }
    }
    /**
     * Adds a new user with the specified username, age, city, and user type.
     *
     * @param username The username of the new user.
     * @param age The age of the new user.
     * @param city The city of the new user.
     * @param userType The type of the new user (ARTIST, HOST, or USER).
     * @return A message indicating the result of the operation.
     */
    public static String addUser(final String username,
                                 final int age, final String city, final Enums.UserType userType) {
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
        return "The username " + username + " has been added successfully.";
    }

    @Setter
    @Getter
    private  String currentPageType;
    /**
     * Gets the current page type of the user.
     *
     * @return The current page type.
     */
    public String getCurrentPageType() {
        return currentPageType;
    }

    /**
     * Sets the current page type and page owner for the user.
     *
     * @param currentPageType The type of the current page (e.g., "ArtistPage").
     * @param pageOwner The owner of the current page.
     */
    public void setCurrentPageType(final String currentPageType, final User pageOwner) {
        this.currentPageType = currentPageType;
        this.currPageOwner = pageOwner;
    }


    public void setFollowedPlaylists(final ArrayList<Playlist> followedPlaylists) {
        this.followedPlaylists = followedPlaylists;
    }
    /**
     * Prints the content of the current page based on the user's role.
     *
     * @param user The user for whom to print the current page.
     * @param userRole The role of the user (e.g., Enums.UserType.ARTIST).
     * @return The content of the current page as a string.
     */
    public String printCurrentPage(final User user, final Enums.UserType userRole) {
        String pageContent = "";
        if (currentPageType == null) {
            return "Page type is not set.";
        }

        switch (user.getCurrentPageType()) {
            case "ArtistPage":
                Artist artist = (Artist) currPageOwner;
                ArtistPage artistPage = new ArtistPage(artist);
                pageContent = artistPage.displayPage();
                break;
            case "HomePage":
                User homepageuser = currPageOwner;
                HomePage homePage = new HomePage(homepageuser);
                pageContent = homePage.displayPage();
                break;
            case "LikedContentPage":
                User likedcontentusser = currPageOwner;
                LikedContentPage likedContentPage = new LikedContentPage(likedcontentusser);
                pageContent = likedContentPage.displayPage();
                break;
            case "HostPage":
                Host host = (Host) currPageOwner;
                HostPage hostPage = new HostPage(host);
                pageContent = hostPage.displayPage();
                break;
            default:
                break;

        }
        return pageContent;
    }



}



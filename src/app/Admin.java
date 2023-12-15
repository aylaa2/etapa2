package app;


import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.utils.Enums;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import fileio.input.EpisodeInput;


import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Admin.
 */
public final class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;
    private static List<Album> albums = new ArrayList<>();
    private static List<Episode> episodes = new ArrayList<>();
    private static final List<Podcast> allpodcast = new ArrayList<>();

    private Admin() {
    }

    public static List<Host> getHosts() {
        return users.stream()
                .filter(u -> u instanceof Host)
                .map(u -> (Host) u)
                .collect(Collectors.toList());
    }

    public static List<Artist> getArtists() {
        return users.stream().filter(u -> u.getUserType()
                        .equals(Enums.UserType.ARTIST)).map(u -> (Artist) u)
                .collect(Collectors.toList());

    }

    public static void setAlbums(final List<Album> albums) {
        Admin.albums = albums;
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }
    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodeList = new ArrayList<>(); // Renamed variable
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodeList.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(), episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodeList));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return  new ArrayList<>(songs);
    }
    public static List<Episode> getEpisode() {
        return  new ArrayList<>(episodes);
    }
    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }
    public static List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }
    /**
     * Gets addGlobalAlbum.
     *
     * @return albums
     */
    public static void addGlobalAlbum(final Album album) {
        albums.add(album);
    }
    /**
     * Gets addGlobalPodcast.
     *
     * @return podcasts
     */
    public static void addGlobalPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }
    /**
     * Gets addNewPodcast.
     *
     * @return podcasts
     */
    public static void addNewPodcast(final Podcast podcast) {
        allpodcast.add(podcast);
    }
    /**
     * setSongList
     */
    public static void setSongList(final List<Song> songList) {
        songs = songList;
    }
    /**
     * setEpisodetList
     */
    public static void setEpisodetList(final List<Episode> episodetList) {
        episodes = episodetList;
    }
    /**
     * getAllNewPodcasts
     */
    public static List<Podcast> getAllNewPodcasts() {
        List<Podcast> allpodcasts = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType().equals(Enums.UserType.HOST)) {
                allpodcasts.addAll(((Host) user).getPodcasts());
            }
        }
        return allpodcasts;
    }
    /**
     * Gets getAlbumss.
     *
     * @return the List<Album>
     */
    public static List<Album> getAlbumss() {
        List<Album> allAlbums = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType().equals(Enums.UserType.ARTIST)) {
                allAlbums.addAll(((Artist) user).getAlbums());
            }
        }
        return allAlbums;
    }
    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername() != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    /**
     * addUser
     *
     * @param user the user
     */
    public static void addUser(final User user) {
        users.add(user);
    }
    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            if (user.isOnline()) {
                user.simulateTime(elapsed);
            }
        }
    }
    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        final int songLimit = 5;
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= songLimit) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 albums.
     *
     * @return the top 5 albums
     */
    public static List<String> getTop5Albums() {
        // Create a map to hold the total likes for each album
        Map<Album, Integer> albumLikes = new HashMap<>();
        List<Album> globalAlbums = getAlbumss();

        // Calculate the total likes for each album
        for (Album album : globalAlbums) {
            int totalLikes = 0;
            for (Song song : album.getSongs()) {
                totalLikes += song.getLikes();
            }
            albumLikes.put(album, totalLikes);
        }

        // Convert the entry set to a list for sorting
        List<Map.Entry<Album, Integer>> sortedEntries = new ArrayList<>(albumLikes.entrySet());

        // Sort the entries by total likes (descending) and then
        // by album name (lexicographical order)
        sortedEntries.sort(Map.Entry.<Album, Integer>comparingByValue().reversed()
                .thenComparing(entry -> entry.getKey().getName()));

        // Select the top 5 unique album names
        List<String> topAlbums = new ArrayList<>();
        sortedEntries.stream()
                .map(entry -> entry.getKey().getName())
                .distinct()
                .limit(LIMIT)
                .forEach(topAlbums::add);

        return topAlbums;
    }
    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }

            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }
    /**
     * Retrieves a list of the top 5 artists based on the total number
     * of likes for all their songs.
     * This method aggregates the likes of all songs across all albums
     * for each artist and sorts
     * the artists based on these totals. It returns the names of
     * the top 5 artists in terms of likes.
     *
     * @return A list of strings representing the names of the top 5 artists with the most likes.
     */
    public static List<String> getTop5Artists() {
        List<Artist> artists = new ArrayList<>();
        for (User user: users) {
            if (user instanceof Artist) {
                artists.add((Artist) user);
            }
        }

        Map<Artist, Integer> artistLikes = new HashMap<>();

        // Calculate total likes for each artist
        for (Artist artist : artists) {
            int totalLikes = artist.getAlbums().stream()
                    .flatMap(album -> album.getSongs().stream())
                    .mapToInt(Song::getLikes)
                    .sum();

            artistLikes.put(artist, totalLikes);
        }

        // Sort artists by total likes and get top 5
        return artistLikes.entrySet().stream()
                .sorted(Map.Entry.<Artist, Integer>comparingByValue().reversed())
                .map(entry -> entry.getKey().getName())
                .limit(LIMIT)
                .collect(Collectors.toList());
    }
    /**
     * getAllonlineUsers.
     */
    public static List<String> getOnlineUsers() {
        final int limits = 10;

        List<String> onlineUsernames = new ArrayList<>();
        int count = 0;

        for (User user : users) {
            if (user.getUserType() == Enums.UserType.USER && user.isOnline()) {
                onlineUsernames.add(user.getUsername());
                count++;

                if (count >= limits) {
                    break;
                }
            }
        }

        return onlineUsernames;
    }
    /**
     * getAllUsers.
     */
    public static List<String> getAllUsers() {
        Comparator<User> byUserType = Comparator.comparing(u -> u.getUserType());

        return users.stream()
                .sorted(byUserType)
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }

    /**
     *  deletes User
     *
     * @return userToDelete
     */
    public static String deleteUser(final User userToDelete) {
        if (userToDelete == null) {
            return "User does not exist.";
        }

        String username = userToDelete.getUsername();
        if (hasActiveInteractions(userToDelete)) {
            return username + " can't be deleted.";
        }

        removeUser(username);
        return username + " was successfully deleted.";
    }
    /**
     *  checks if user hasActiveInteractions
     *
     * @return true/false
     */
    public static boolean hasActiveInteractions(final User userToDelete) {
        List<Player> activePlayers = Admin.getActivePlayers();

        Enums.UserType userType = userToDelete.getUserType();

        switch (userType) {
            case ARTIST:
                Artist artist = (Artist) userToDelete;


                // if artist page is the current page of a user,
                for (User user : getNormalUsers()) {
                    if (user.getCurrPageOwner() == userToDelete) {
                        return true;
                    }

                }

                return isArtistContentBeingPlayed(artist, activePlayers);
            case HOST:
                Host host = (Host) userToDelete;

                // check if any user is in the page
                for (String username : Admin.getAllUsers()) {
                    User user = Admin.getUser(username);
                    if (user.searchBar != null && user.getLastSearchedName() != null
                            && user.getLastSearchedName().equals(host.getUsername())) {
                        return true;
                    }

                }

                for (Player player : activePlayers) {
                    if (player.getSource().getAudioCollection() != null
                            && player.getSource().getAudioCollection()
                            .getOwner().equals(host.getName())) {
                        return true;
                    }
                }

                // if host page is the current page of a user,
                for (User user : getNormalUsers()) {
                    if (user.getCurrPageOwner() == userToDelete) {
                        return true;
                    }

                }

                return isHostContentBeingPlayed(host, activePlayers);
            case USER:
                return isUserPlaylistBeingPlayed(userToDelete, activePlayers);

            default:
                return false;
        }
    }

    /**
     *  checks if user hasActiveInteractions
     *
     * @return true/false
     */
    public static boolean isArtistContentBeingPlayed(final Artist artist,
                                                     final List<Player> activePlayers) {
        for (Album album : artist.getAlbums()) {
            for (Player player : activePlayers) {
                if (player.getCurrentAudioFile().equals(album)) {
                    return true;
                }
            }
            for (Song song : album.getSongs()) {
                for (Player player : activePlayers) {
                    if (player.getCurrentAudioFile().equals(song)) {
                        return true;
                    }
                }
            }
        }
        for (Album album : artist.getAlbums()) {
            for (Player player : activePlayers) {
                if (player.getSource().getAudioFile().matchesName(artist.getName())) {
                    return true;
                }
            }
        }
        // Check if a playlist contains a song from any of the artist's albums
        return isAlbumInAnyPlaylist(artist);
    }
    /**
     *  checks if user hasActiveInteractions
     *  Additional method to check if any playlist contains
     *  a song from the artist's albums
     * @return true/false
     */
    private static boolean isAlbumInAnyPlaylist(final Artist artist) {
        List<Playlist> allPlaylists = Admin.getPlaylists();
        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                for (Playlist playlist : allPlaylists) {
                    if (playlist.containsSong(song)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *  checks if  isHostContentBeingPlayed
     *
     * @return true/false
     */
    public static boolean isHostContentBeingPlayed(final Host host,
                                                   final List<Player> activePlayers) {
        for (Podcast podcast : host.getPodcasts()) {
            for (Player player : activePlayers) {
                if (player.getSource().getAudioCollection().matchesName(podcast.getName())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     *  checks if  isUserPlaylistBeingPlayed
     *
     * @return true/false
     */
    public static boolean isUserPlaylistBeingPlayed(final User user,
                                                    final List<Player> activePlayers) {
        // Assuming you have a method in User class to get the playlists created by the user
        List<Playlist> userPlaylists = Admin.getPlaylists();

        for (Playlist userPlaylist : userPlaylists) {
            if (isPlaylistBeingPlayed(userPlaylist, activePlayers)) {
                return true;
            }
        }
        return false;
    }
    /**
     *  checks if  isPlaylistBeingPlayed
     *
     * @return true/false
     */
    private static boolean isPlaylistBeingPlayed(final Playlist playlist,
                                                 final List<Player> activePlayers) {
        for (Player player : activePlayers) {
            if (player.isPlaying() && player.getSource() != null) {
                AudioCollection currentAudioCollection = player.getSource()
                        .getAudioCollection();
                if (currentAudioCollection != null && currentAudioCollection.equals(playlist)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     *  checks if  getActivePlayers
     *
     * @return List<Player>
     */
    public static List<Player> getActivePlayers() {
        List<Player> activePlayers = new ArrayList<>();

        for (User user : users) {
            Player player = user.getPlayer();
            if (player != null && player.isPlaying()) {
                activePlayers.add(player);
            }
        }

        return activePlayers;
    }
    /**
     *  checks if  getNormalUsers
     *
     * @return List<User>
     */
    public static List<User> getNormalUsers() {
        return users.stream().filter(user ->
                        user.getUserType().equals(Enums.UserType.USER)).
                collect(Collectors.toList());
    }
    /**
     *   removes content from USERS
     */
    public static void removeUser(final String username) {
        User user = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return;
        }

        switch (user.getUserType()) {
            case ARTIST:
                removeArtistContent((Artist) user);
                break;
            case HOST:
                removeHostContent((Host) user);
                break;
            case USER:
                List<Song> likedSongs = user.getLikedSongs();
                for (Song song : likedSongs) {
                    int currentLikes = song.getLikes();
                    song.setLikes(currentLikes - 1);
                }

                for (Playlist playlist : user.getFollowedPlaylists()) {
                    playlist.decreaseFollowers();
                }

                ArrayList<Playlist> ownedPlaylists = user.getPlaylists();

                for (Playlist playlist : ownedPlaylists) {

                    getPlaylists().remove(playlist);


                    for (User follower : getNormalUsers()) {
                        if (follower.getFollowedPlaylists().contains(playlist)) {
                            follower.getFollowedPlaylists().remove(playlist);
                        }
                    }

                    playlist.decreaseFollowers();
                }

                // Clear the user's list of owned playlists
                user.getPlaylists().clear();

                break;
            default:
                break;

        }
        users.remove(user);
    }

    /**
     *   removes content from USERS
     */
    private static void removeHostContent(final Host host) {
        List<Podcast> hostPodcasts = new ArrayList<>(host.getPodcasts());
        for (Podcast podcast : hostPodcasts) {
            // Remove the podcast from the global list
            podcasts.remove(podcast);
            // Optional: Remove episodes from the global episode list
            episodes.removeAll(podcast.getEpisodes());
        }

        for (Iterator<Host.Announcement> iterator = host
                .getAnnouncements().iterator(); iterator.hasNext();) {
            Host.Announcement announcement = iterator.next();
            if (announcement.getUser().equals(host)) {
                iterator.remove(); // Remove the specific announcement
            }
        }

        host.getAnnouncements().clear();

    }

    /**
     *   removes content from USERS
     */
    private static void removeArtistContent(final Artist artist) {
        // First, remove likes associated with any songs from the artist's albums
        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                removeLikesFromAllUsers(song);
            }
        }

        // Remove artist's albums and their songs from the global list
        albums.removeIf(album -> {
            boolean isArtistAlbum = album.getName().equals(artist.getUsername());
            if (isArtistAlbum) {
                removeAlbumSongsFromGlobalList(album);
            }
            return isArtistAlbum;
        });


        songs.removeIf(song -> song.getArtist().equals(artist.getUsername()));

        List<Artist.Merchandise> merchList = Artist.getMerchList();
        merchList.clear();

        List<Artist.Event> eventList = Artist.getEvents();
        eventList.clear();
    }

    /**
     *   removes content from USERS
     */
    public static void removeLikesFromAllUsers(final Song song) {
        for (User user : users) {
            user.getLikedSongs().remove(song);
        }
    }
    /**
     *   removes content from USERS
     */
    public static void removeAlbumSongsFromGlobalList(final Album album) {
        List<Song> albumSongs = album.getSongs();
        songs.removeAll(albumSongs);
    }


}

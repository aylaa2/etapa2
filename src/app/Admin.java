package app;

import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import app.player.PlayerSource;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.utils.Enums;
import fileio.input.*;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Collectors;

public class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<Album> albums = new ArrayList<>();
    private static List<Episode> episodes = new ArrayList<>();

    public static List<Host> getHosts() {
        return users.stream()
                .filter(u -> u instanceof Host)
                .map(u -> (Host) u)
                .collect(Collectors.toList());
    }

    public static boolean removeUser(String username) {
        User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
        if (user == null) {
            return false;
        }
        ArrayList<Playlist> followedPlaylists = user.getFollowedPlaylists(); // Assuming such a method exists

        if (user instanceof Artist) {
            Artist artist = (Artist) user;
            removeArtistContent(artist);
        }
        if (followedPlaylists != null) {
           //followedPlaylists.clear();
           followedPlaylists = null;
        }

        return users.remove(user);
    }


    private static void removeArtistContent(Artist artist) {
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
    }



    private static void removeLikesFromAllUsers(Song song) {
        for (User user : users) {
            user.getLikedSongs().remove(song);
        }
    }







    private static void removeAlbumSongsFromGlobalList(Album album) {
        List<Song> albumSongs = album.getSongs();
        songs.removeAll(albumSongs);
    }


    public static List<Artist> getArtists() {
        return users.stream().filter(u -> u instanceof Artist).map(u -> (Artist) u).collect(Collectors.toList());

    }

    public static void setAlbums(List<Album> albums) {
        Admin.albums = albums;
    }

    private static int timestamp = 0;

    public static void setUsers(List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    public static void setSongs(List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    public static void setPodcasts(List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(), episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    public static List<Song> getSongs() {
        return  new ArrayList<>(songs);
    }
    public static List<Episode> getEpisode() {
        return  new ArrayList<>(episodes);
    }
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }
    public static List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }
    public static void addGlobalAlbum(Album album) {
        albums.add(album);
    }
    public static void addGlobalPodcast(Podcast podcast) {
        podcasts.add(podcast);
    }
    private static final List<Podcast> allNewPodcasts = new ArrayList<>();
    public static void addNewPodcast(Podcast podcast) {
        allNewPodcasts.add(podcast);
    }

    public static List<Podcast> getAllNewPodcasts() {
        List<Podcast> allpodcasts = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Host) {
                allpodcasts.addAll(((Host) user).getPodcasts());
            }
        }
        return allpodcasts;
    }
    public static List<Album> getAlbumss() {
        List<Album> allAlbums = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Artist) {
                allAlbums.addAll(((Artist) user).getAlbums());
            }
        }
        return allAlbums;
    }


    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    public static User getUser(String username) {
        for (User user : users) {
            if (user.getUsername() != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void updateTimestamp(int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= 5) break;
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    public static List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(albums);
        sortedAlbums.sort(Comparator.comparingInt(Album::getReleaseYear).reversed());
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= 5) break;
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= 5) break;
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    public static List<String> getOnlineUsers() {
        List<String> onlineUsernames = new ArrayList<>();
        int count = 0;

        for (User user : users) {
            if (user.getUserType() == Enums.UserType.USER && user.isOnline()) {
                onlineUsernames.add(user.getUsername());
                count++;

                if (count >= 10) {
                    break;
                }
            }
        }

        return onlineUsernames;
    }
    public static List<String> getAllUsers() {
        Comparator<User> byUserType = Comparator.comparing(u -> u.getUserType());

        return users.stream()
                .sorted(byUserType)
                .map(User::getUsername)
                .collect(Collectors.toList());
    }


    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }

    public static void setSongList(List<Song> songList){
        songs = songList;
    }
    public static void setEpisodetList (List<Episode> episodetList)
    { episodes = episodetList;}

    public static String deleteUser(User userToDelete) {
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


    public static boolean hasActiveInteractions(User userToDelete) {
        List<Player> activePlayers = Admin.getActivePlayers();

        Enums.UserType userType = userToDelete.getUserType();

        switch (userType) {
            case ARTIST:
                Artist artist = (Artist) userToDelete;
                return isArtistContentBeingPlayed(artist, activePlayers);
            case HOST:
                Host host = (Host) userToDelete;
                return isHostContentBeingPlayed(host, activePlayers);
            case USER:
                return isUserPlaylistBeingPlayed(userToDelete, activePlayers);
            default:
                return false;
        }
    }


    public static boolean isArtistContentBeingPlayed(Artist artist, List<Player> activePlayers) {
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
        return false;
    }


    public static boolean isHostContentBeingPlayed(Host host, List<Player> activePlayers) {
        for (Podcast podcast : host.getPodcasts()) {
            for (Player player : activePlayers) {
                if (player.getCurrentAudioFile().equals(podcast)) {
                    return true;
                }
            }
            for (Episode episode : podcast.getEpisodes()) {
                for (Player player : activePlayers) {
                    if (player.getCurrentAudioFile().equals(episode)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    public static boolean isUserPlaylistBeingPlayed(User user, List<Player> activePlayers) {
        // Assuming you have a method in User class to get the playlists created by the user
        List<Playlist> userPlaylists = Admin.getPlaylists();

        for (Playlist userPlaylist : userPlaylists) {
            if (isPlaylistBeingPlayed(userPlaylist, activePlayers)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPlaylistBeingPlayed(Playlist playlist, List<Player> activePlayers) {
        for (Player player : activePlayers) {
            if (player.isPlaying() && player.getSource() != null) {
                AudioCollection currentAudioCollection = player.getSource().getAudioCollection();
                // Add null check here
                if (currentAudioCollection != null && currentAudioCollection.equals(playlist)) {
                    return true;
                }
            }
        }
        return false;
    }


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


}

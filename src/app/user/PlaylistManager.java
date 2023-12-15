package app.user;

import app.audio.Collections.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private static PlaylistManager instance;
    private List<Playlist> globalPlaylists;

    private PlaylistManager() {
        globalPlaylists = new ArrayList<>();
    }

    public static synchronized PlaylistManager getInstance() {
        if (instance == null) {
            instance = new PlaylistManager();
        }
        return instance;
    }

    public List<Playlist> getGlobalPlaylists() {
        return globalPlaylists;
    }

    // Other methods to manipulate playlists (add, remove, find, etc.)
}


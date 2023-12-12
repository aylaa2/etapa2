package app.searchBar;


import app.Admin;
import app.audio.Collections.Album;
import app.audio.LibraryEntry;
import app.user.User;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.*;
import static app.searchBar.FilterUtils.filterByFollowers;

public class SearchBar {
    private List<LibraryEntry> results;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    private static SearchBar instance;
    private String lastSearchType;

    @Getter
    private LibraryEntry lastSelected;

    public SearchBar(String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }


    public  String getLastSearchType() {
        return lastSearchType;
    }


    public void setLastSearchType(String type) {
        this.lastSearchType = type;
    }

    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }
    public List<LibraryEntry> search(Filters filters, String type) {
        List<LibraryEntry> entries;

        switch (type) {
            case "song":
                entries = new ArrayList<>(Admin.getSongs());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(Admin.getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(Admin.getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "album":
                entries = new ArrayList<>(Admin.getAlbumss());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getDescription() != null) {
                    entries = filterByDescription(entries, filters.getDescription());
                }
                break;
            case "artist":
                entries = new ArrayList<>(Admin.getArtists());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                break;
            case "host":
                entries = new ArrayList<>(Admin.getHosts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                break;
            default:
                entries = new ArrayList<>();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;
        return this.results;
    }
    private Album lastSelectedAlbum;
    public LibraryEntry select(int itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected =  this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }

}

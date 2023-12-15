package app.searchBar;


import app.Admin;
import app.audio.LibraryEntry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.*;


/**
 * A SearchBar class for performing searches and managing search results.
 */
public class SearchBar {
    private List<LibraryEntry> results;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    private String lastSearchType;

    @Getter
    private LibraryEntry lastSelected;

    /**
     * Constructs a SearchBar object with the specified user.
     *
     * @param user The user associated with the SearchBar.
     */
    public SearchBar(final String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    /**
     * Gets the last search type used.
     *
     * @return The last search type.
     */
    public String getLastSearchType() {
        return lastSearchType;
    }

    /**
     * Clears the selection and last search type.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }

    /**
     * Performs a search based on the specified filters and type.
     *
     * @param filters The filters to apply to the search.
     * @param type    The type of search to perform.
     * @return A list of LibraryEntry objects matching the search criteria.
     */
    public List<LibraryEntry> search(final Filters filters, final String type) {
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
    /**
     * Selects an item from the search results based on the item number.
     *
     * @param itemNumber The item number to select.
     * @return The selected LibraryEntry object.
     */
    public LibraryEntry select(final int itemNumber) {
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

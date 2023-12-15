package pagination;

import app.Admin;
import app.audio.Collections.Album;
import app.user.Artist;
import app.utils.Enums;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ArtistPage extends Page {
    private List<Album> albums;
    private List<Artist.Merchandise> merchandise;
    private List<Artist.Event> events;
    private Enums.UserType userType;

    /**
     * Constructs a new ArtistPage for the given artist.
     *
     * @param owner The artist for whom this page is created.
     */
    public ArtistPage(final Artist owner) {
        super(owner);
        this.albums = new ArrayList<>();
        this.merchandise = new ArrayList<>();
        this.events = new ArrayList<>();
        this.userType = Enums.UserType.ARTIST;
    }
    /**
     * Generates the content to be displayed on the artist's page.
     *
     * @return A string representing the content of the artist's page.
     */
    @Override
    public String displayPage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Albums:\n\t");
        List<Album> albums = Admin.getAlbums();
        if (!albums.isEmpty()) {
            Album firstAlbum = albums.get(0); // Get the first album
            sb.append("[").append(firstAlbum.getName()).append("]");
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
        List<Artist.Event> eventList = Artist.getEvents();
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
        return sb.toString();
    }

    private String formatList(final List<String> items) {
        if (items.isEmpty()) {
            return "[]";
        } else {
            return "[" + String.join(", ", items) + "]";
        }
    }

}

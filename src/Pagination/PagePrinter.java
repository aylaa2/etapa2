package Pagination;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.searchBar.SearchBar;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.utils.Enums;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class PagePrinter {

    private Enums.PageType currentPageType;
    public void printPage(Page currentPage) {
        if (currentPage != null) {
            currentPage.displayPage();
        } else {
            System.out.println("No current page to display.");
        }
    }
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public PagePrinter(Enums.PageType pageType) {
        this.currentPageType = pageType;
    }

//    public String printCurrentPage(User user) {
//        if (currentPageType == null) {
//            return "Page type is not set.";
//        }
//        if (!user.isOnline()) {
//            return user.getUsername() + " is offline.";
//        }
//
//
//        switch (currentPageType) {
//            case HOMEPAGE:
//                return printHomePage(user);
//            case LIKEDCONTENTPAGE:
//                return printLikedContentPage(user);
//            case ARTISTPAGE:
//                return printArtistPage();
//            case HOSTPAGE:
//                return printHostPage();
//            default:
//                return "Unknown page.";
//        }
//    }


    private String printHomePage(User user) {
        StringBuilder sb = new StringBuilder();
        List<String> userLikedSongs = user.getLikedSongs().stream()
                .map(Song::getName)
                .collect(Collectors.toList());

        sb.append("Liked songs:\n\t");
        sb.append(formatList(userLikedSongs));
        sb.append("\n\nFollowed playlists:\n\t");
        // Assuming user.getFollowedPlaylists() retrieves the playlists followed by the user
        sb.append(formatList(user.getFollowedPlaylists().stream()
                .map(p -> p.getName() + " - " + p.getOwner())
                .collect(Collectors.toList())));
        return sb.toString();
    }

    private static String printLikedContentPage(User user) {
        // Implementation for LIKEDCONTENTPAGE
        return "Liked Content Page";
    }

    private static String printArtistPage() {
        StringBuilder sb = new StringBuilder();
        appendAlbums(sb);
        appendMerchandise(sb);
        appendEvents(sb);
        return sb.toString();
    }

    private static void appendAlbums(StringBuilder sb) {
        sb.append("Albums:\n\t");
        List<Album> albums = Admin.getAlbums();
        if (!albums.isEmpty()) {
            Album firstAlbum = albums.get(0); // Get the first album
            sb.append("[").append(firstAlbum.getName()).append("]"); // Append the name of the first album
        } else {
            sb.append("[]");
        }
    }

    private static void appendMerchandise(StringBuilder sb) {
        sb.append("\n\nMerch:\n\t");
        List<Artist.Merchandise> merchList = Artist.getMerchList();
        if (merchList != null && !merchList.isEmpty()) {
            sb.append("[");
            for (Artist.Merchandise merch : merchList) {
                String formattedPrice = String.format(merch.getPrice() % 1.0 == 0 ? "%.0f" : "%.2f", merch.getPrice());
                sb.append(merch.getName()).append(" - ")
                        .append(formattedPrice).append(":\n\t")
                        .append(merch.getDescription()).append(", ");
            }
            sb.setLength(sb.length() - 2); // Remove the last comma and space
            sb.append("]");
        } else {
            sb.append("[]");
        }
    }

    private static void appendEvents(StringBuilder sb) {
        sb.append("\n\nEvents:\n\t");
        List<Artist.Event> eventList = Artist.getEvents();
        if (eventList != null && !eventList.isEmpty()) {
            sb.append("[");
            for (Artist.Event event : eventList) {
                sb.append(event.getName()).append(" - ")
                        .append(sdf.format(event.getDate()))
                        .append(":\n\t").append(event.getDescription()).append(", ");
            }
            sb.setLength(sb.length() - 2); // Remove the last comma and space
            sb.append("]");
        } else {
            sb.append("[]");
        }
    }

//    private static String printHostPage() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Podcasts:\n\t[");
//
//        List<Host.PartialPodcast> formattedPodcasts = us.getFormattedPodcasts();
//        boolean isFirstCategory = true;
//
//        for (Host.PartialPodcast partialPodcast : formattedPodcasts) {
//            // Add a comma and newline before the start of the next category, except for the first
//            if (!isFirstCategory) {
//                sb.append("\n, ");
//            }
//            isFirstCategory = false;
//
//            sb.append(partialPodcast.getName()).append(":\n\t");
//            sb.append(formatList(partialPodcast.getEpisodes()));  // This will add [ ] around the episodes
//        }
//
//        sb.append("\n]");
//        sb.append("\n\nAnnouncements:\n\t[");
////        for (int i = 0; i < announcements.size(); i++) {
////            Host.Announcement announcement = announcements.get(i);
////            sb.append(announcement.getName()).append(":\n\t").append(announcement.getDescription());
////            if (i < announcements.size() - 1) {
////                sb.append(",\n\t");
////            }
////        }
//        sb.append("\n]");
//        // Hardcoded Announcements
//       // sb.append("\n\nAnnouncements:\n\t[Announcement1:\n\tPrimul anunt adaugat!\n]");
//        return sb.toString();
//    }

    private static String formatList(List<String> items) {
        if (items.isEmpty()) {
            return "[]";
        } else {
            return "[" + String.join(", ", items) + "]";
        }
    }
}


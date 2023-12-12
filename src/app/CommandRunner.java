package app;

import app.audio.Collections.Album;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import Pagination.PagePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandRunner {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode search(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null ? "No user found" : user.getUsername() + " is offline.");
            objectNode.put("results", objectMapper.createArrayNode()); // Empty array for results
            return objectNode;
        }

        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = user.search(filters, type);
        String message = "Search returned " + results.size() + " results";
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }


    public static ObjectNode select(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Enums.UserType userType = user.getUserType();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());



        if (user == null) {
            String message = "User not found for username: " + commandInput.getUsername();
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.select(commandInput.getItemNumber());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode load(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.load();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode playPause(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.playPause();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode repeat(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.repeat();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode shuffle(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("message", message);
            return objectNode;
        }

        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode forward(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.forward();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode backward(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode like(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null || !user.isOnline()) {
            String message = commandInput.getUsername() + " is offline." ;
            objectNode.put("message", message);
            objectNode.put("user", commandInput.getUsername());
            return objectNode;
        }

        String message = user.like();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode next(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.next();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode prev(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());

        if (user == null) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", message);
            return objectNode;
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode createPlaylist(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.createPlaylist(commandInput.getPlaylistName(), commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode addRemoveInPlaylist(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode switchVisibility(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        if (user == null) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", message);
            return objectNode;
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode showPlaylists(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        if (user == null) {
            objectNode.put("user", commandInput.getUsername());
            return objectNode;
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    public static ObjectNode follow(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());

        // Check if user is null before calling follow()
        if (user == null) {
            objectNode.put("message", "User not found");
            return objectNode;
        }

        // If user is not null, call follow() and proceed
        String message = user.follow();
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }


    public static ObjectNode status(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            if (user == null) {
                objectNode.put("user", commandInput.getUsername());
                return objectNode;
            }
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", message);
            return objectNode;
        }

        PlayerStats stats = user.getPlayerStats();
        if (!user.isOnline()){
            stats.setPaused(false); // Set paused to false when the user is offline
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    public static ObjectNode showLikedSongs(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        if (user == null) {
            objectNode.put("user", commandInput.getUsername());
            return objectNode;
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    public static ObjectNode getPreferredGenre(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", message);
            return objectNode;
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    public static ObjectNode getTop5Songs(CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    public static ObjectNode getTop5Playlists(CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
    public static ObjectNode switchConnectionStatus(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user.getUserType() != Enums.UserType.USER) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", commandInput.getUsername() + " is not a normal user.");
            return objectNode;
        }

        user.switchStatus(); // Toggle the user's online/offline status

        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", commandInput.getUsername() + " has changed status successfully.");

        return objectNode;
    }

    public static ObjectNode getOnlineUsers(CommandInput commandInput) {
        List<String> users = Admin.getOnlineUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }

    public static ObjectNode addUser(CommandInput commandInput) {
        User existingUser = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        // If user exists, return a message indicating the username is already taken
        if (existingUser != null) {
            return objectNode.put("message", "The username " + commandInput.getUsername() + " is already taken.");
        }
        Enums.UserType type;
        switch(commandInput.getType()){
            case "artist":
                type = Enums.UserType.ARTIST;
                break;
            case "user":
                type =Enums.UserType.USER;
                break;
            case "host":
                type =Enums.UserType.HOST;
                break;
            default:
                type = Enums.UserType.USER;
                break;
        }
        // Add new user
        String addUserResult = User.addUser(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity(), type);

        // Add the result message to the objectNode
        objectNode.put("message", addUserResult);

        return objectNode;
    }

    public static ObjectNode addAlbum(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        if (user == null) {
            objectNode.put("message", "The username" + commandInput.getUsername() + "doesn't exist.");
            return objectNode;
        }
        if(!user.getUserType().equals(Enums.UserType.ARTIST)){
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        // String results = ((Artist)user).addAlbum(commandInput.getName(), commandInput.getReleaseYear(), commandInput.getDescription(), Admin.getSongs());
        String results = ((Artist)user).addAlbum(commandInput.getName(), commandInput.getReleaseYear(), commandInput.getDescription(), commandInput.getSongs());
        objectNode.put("message", results);

        return objectNode;
    }
    public static ObjectNode showAlbums(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", "User not found.");
            return objectNode;
        }

        if (!(user instanceof Artist artist)) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", "User is not an artist.");
            return objectNode;
        }

        List<Artist.PartialAlbum> partialAlbums = artist.getFormattedAlbums();
        ArrayNode albumsArray = objectMapper.valueToTree(partialAlbums);
        objectNode.put("user", commandInput.getUsername());
        objectNode.set("result", albumsArray);

        return objectNode;
    }

//    public static ObjectNode printCurrentPage(CommandInput commandInput) {
//        User user = Admin.getUser(commandInput.getUsername());
//        String offline = "is offline";
//
//        String pageContent = "";
//
//
//
//        if (user != null) {
//            Enums.PageType pageType = user.getCurrentPageType();
//            PagePrinter pagePrinter = new PagePrinter(pageType);
//            pageContent = pagePrinter.printCurrentPage(user);
//
//        } else {
//            pageContent = "User not found.";
//        }
//        ObjectNode objectNode = objectMapper.createObjectNode();
//        if (!user.isOnline()){
//            objectNode.put("message", commandInput.getUsername() + offline);
//        }
//        objectNode.put("command", commandInput.getCommand());
//        objectNode.put("user", commandInput.getUsername());
//        objectNode.put("timestamp", commandInput.getTimestamp());
//        objectNode.put("message", pageContent);
//
//        return objectNode;
//    }



    public static ObjectNode printCurrentPage(CommandInput commandInput) {
        // Retrieve the user based on the username
        User user = Admin.getUser(commandInput.getUsername());

        // Initialize the response object
        ObjectNode objectNode = objectMapper.createObjectNode();

        // Check if the user exists and is online
        if (user != null && user.isOnline()) {

            // Generate the page content
            String pageContent = user.printCurrentPagess(user);

            // Set the generated content in the response
            objectNode.put("message", pageContent);
        } else {
            // Set a user not found or offline message
            String offlineMessage = (user == null) ? "User not found." : commandInput.getUsername() + " is offline.";
            objectNode.put("message", offlineMessage);
        }

        // Add command, user, and timestamp info to the response
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        return objectNode;
    }





    public static ObjectNode addEvent(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        // Check if the user is an artist
        if (user instanceof Artist) {
            Artist artist = (Artist) user;
            // Call addEvent method and store the message
            String message = artist.addEvent(commandInput.getName(),
                    commandInput.getDescription(),
                    commandInput.getDate());
            objectNode.put("message", message);
        } else if(user == null ){
            // User is not an artist or does not exist
            objectNode.put("message", "The username " + commandInput.getUsername() +
                    " doesn't exist.");
        } else {
            objectNode.put("message",commandInput.getUsername() + " is not an artist.");

        }
        return objectNode;
    }
    public static ObjectNode getAllUsers(CommandInput commandInput) {
        List<String> users = Admin.getAllUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }
    public static ObjectNode deleteUser(CommandInput commandInput) {
        User existingUser = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        if (existingUser == null) {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            String deleteMessage = Admin.deleteUser(existingUser);
            objectNode.put("message", deleteMessage);
        }

        return objectNode;
    }



    public static ObjectNode addMerch(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (!(user instanceof Artist)) {
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        Artist artist = (Artist) user;
        String message = artist.addMerch(commandInput.getName(), commandInput.getDescription(), commandInput.getPrice());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode addPodcast(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        // Check if the user exists and is a host
        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }
        if (!user.getUserType().equals(Enums.UserType.HOST)) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        // Extract podcast name and episodes from the command input
        String podcastName = commandInput.getName();
        List<EpisodeInput> episodeInputs = commandInput.getEpisodes();

        // Call the addPodcast method of the Host class
        String results = ((Host) user).addPodcast(podcastName, episodeInputs);
        objectNode.put("message", results);

        return objectNode;
    }

    public static ObjectNode addAnnouncement(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (!(user instanceof Host)) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        String results = ((Host)user).addAnnouncement(commandInput.getName(), commandInput.getDescription());
        objectNode.put("message", results);

        return objectNode;
    }
    public static ObjectNode removeAnnouncement(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (!(user instanceof Host)) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        String results = ((Host)user).removeAnnouncement(commandInput.getName());
        objectNode.put("message", results);

        return objectNode;
    }

    public static ObjectNode showPodcasts(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            objectNode.put("message", "User not found.");
            return objectNode;
        }

        if (!(user instanceof Host host)) {
            objectNode.put("message", "User is not a host.");
            return objectNode;
        }
        Host host1 = (Host) user;
        List<Host.PartialPodcast> partialPodcasts = host1.getFormattedPodcasts();
        ArrayNode podcastArray = objectMapper.valueToTree(partialPodcasts);
        objectNode.set("result", podcastArray);

        return objectNode;
    }

    public static ObjectNode removeAlbum(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            String message = "The username " + commandInput.getUsername() + " doesn't exist.";
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
            return objectNode;
        }

        // Assuming the user is an Artist, you can call the removeAlbum method
        if (user instanceof Artist) {
            Artist artist = (Artist) user;
            String albumName = commandInput.getName(); // Assuming the album name is passed in the commandInput

            String message = artist.removeAlbum(albumName); // Call the removeAlbum method
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
        } else {
            // Handle the case where the user is not an Artist
            String message = user.getUsername() + " is not an artist.";
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
        }

        return objectNode;
    }

    public static ObjectNode changePage(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String nextPage = commandInput.getNextPage();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (nextPage != null) {
            if (nextPage.equals("LikedContent")){
                user.setCurrentPageType("LikedContentPage");
            } else if (nextPage.equals("Home")){
                user.setCurrentPageType("HomePage");
            }
            String message = user.getUsername() + " accessed " + nextPage + " successfully.";
            objectNode.put("message", message);
        } else {
            String message = user.getUsername() + " is trying to access a non-existent page.";
            objectNode.put("message", message);
        }


        return objectNode;
    }

    public static ObjectNode removePodcast(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            String message = "No user found for username: " + commandInput.getUsername();
            objectNode.put("message", message);
            return objectNode;
        }


        if (user instanceof Host) {
            Host host = (Host) user;
            String podcastName = commandInput.getName();

            String message = host.removePodcast(podcastName);
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
        } else {
            // Handle the case where the user is not an Artist
            String message = user.getUsername() + " is not an host.";
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
        }

        return objectNode;
    }

    public static ObjectNode getTop5Albums(CommandInput commandInput) {
        List<String> songs = Admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }
}

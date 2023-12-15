package app;


import app.audio.Collections.PlaylistOutput;

import app.player.PlayerStats;
import app.searchBar.Filters;

import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private CommandRunner() {
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null || !user.isOnline()) {
            objectNode.put("message", user == null
                    ? "No user found" : user.getUsername() + " is offline.");
            objectNode.put("results", objectMapper.createArrayNode());
            return objectNode;
        }

        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = user.search(filters, type);
        String message = "Search returned " + results.size() + " results";
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));
        if (commandInput.getType()
                .equals("artist")) {
            user.setLastSearchedName(commandInput.getUsername());

        } else if (commandInput.getType()
                .equals("host")) {
            user.setLastSearchedName(commandInput.getUsername());
        }
        return objectNode;
    }


    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
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

        String type = commandInput.getType();
        if (type != null) {
            if (type.equals("Artist")) {
                user.setLastSearchedName(commandInput.getUsername());
            } else if (type.equals("Host")) {
                user.setLastSearchedName(commandInput.getUsername());
            }
        }
        return objectNode;
    }


    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
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


    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
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


    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
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

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
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


    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
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

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null || !user.isOnline()) {
            String message = commandInput.getUsername() + " is offline.";
            objectNode.put("message", message);
            objectNode.put("user", commandInput.getUsername());
            return objectNode;
        }

        String message = user.like();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
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

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
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

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
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

        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
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



    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
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

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
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

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
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


    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
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
        if (!user.isOnline()) {
            stats.setPaused(false);
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("stats", objectMapper.valueToTree(stats));
        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
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

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
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

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
    /**
     * switch Connection Status of the user
     *
     * @param commandInput the command input
     * @return online/offline
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
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
    /**
     * switch Connection Status of the user
     *
     * @param commandInput the command input
     * @return OnlineUsers
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> users = Admin.getOnlineUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }
    /**
     * addUser
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        User existingUser = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());


        if (existingUser != null) {
            //Admin.deleteUser(existingUser);
            return objectNode.put("message",
                    "The username " + commandInput.getUsername() + " is already taken.");
        }

        // If user exists, return a message indicating the username is already taken
        Enums.UserType type;
        switch (commandInput.getType()) {
            case "artist":
                type = Enums.UserType.ARTIST;
                break;
            case "user":
                type = Enums.UserType.USER;
                break;
            case "host":
                type = Enums.UserType.HOST;
                break;
            default:
                type = Enums.UserType.USER;
                break;
        }
        // Add new user
        String addUserResult = User.addUser(commandInput.getUsername(),
                commandInput.getAge(), commandInput.getCity(), type);

        // Add the result message to the objectNode
        objectNode.put("message", addUserResult);

        return objectNode;
    }
    /**
     * addAlbum
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        if (user == null) {
            objectNode.put("message", "The username"
                    + commandInput.getUsername() + "doesn't exist.");
            return objectNode;
        }
        if (!user.getUserType().equals(Enums.UserType.ARTIST)) {
            objectNode.put("message",
                    commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        String results = ((Artist) user).addAlbum(commandInput.getName(),
                commandInput.getReleaseYear(), commandInput.getDescription(),
                commandInput.getSongs());
        objectNode.put("message", results);

        return objectNode;
    }
    /**
     * showAlbums
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
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

    /**
     * printCurrentPage
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        // Retrieve the user based on the username
        User user = Admin.getUser(commandInput.getUsername());


        ObjectNode objectNode = objectMapper.createObjectNode();
        Enums.UserType userType = user.getUserType();
        // Check if the user exists and is online
        if (user != null && user.isOnline()) {

            // Generate the page content
            String pageContent = user.printCurrentPage(user, userType);

            // Set the generated content in the response
            objectNode.put("message", pageContent);
        } else {
            // Set a user not found or offline message
            String offlineMessage = (user == null)
                    ? "User not found." : commandInput.getUsername() + " is offline.";
            objectNode.put("message", offlineMessage);
        }

        // Add command, user, and timestamp info to the response
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        return objectNode;
    }

    /**
     * addEvent
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        // Check if the user is an artist
        if (user instanceof Artist) {
            Artist artist = (Artist) user;
            String message = artist.addEvent(commandInput.getName(),
                    commandInput.getDescription(),
                    commandInput.getDate());
            objectNode.put("message", message);
        } else if (user == null) {
            objectNode.put("message", "The username "
                    + commandInput.getUsername()
                    + " doesn't exist.");
        } else {
            objectNode.put("message", commandInput.getUsername() + " is not an artist.");

        }
        return objectNode;
    }
    /**
     * getAllUsers
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> users = Admin.getAllUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }

    /**
     * deleteUser
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        User existingUser = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        if (existingUser == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
        } else {
            String deleteMessage = Admin.deleteUser(existingUser);
            objectNode.put("message", deleteMessage);
        }

        return objectNode;
    }
    /**
     * addMerch
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (!(user instanceof Artist)) {
            objectNode.put("message",
                    commandInput.getUsername() + " is not an artist.");
            return objectNode;
        }

        Artist artist = (Artist) user;
        String message = artist.addMerch(commandInput.getName(),
                commandInput.getDescription(), commandInput.getPrice());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * addPodcast
     *
     * @param commandInput the command input
     * @return Podcast
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        // Check if the user exists and is a host
        if (user == null) {
            objectNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
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
    /**
     * addAnnouncement
     *
     * @param commandInput the command input
     * @return Announcement
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            objectNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (!(user.getUserType().equals(Enums.UserType.HOST))) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        String results = ((Host) user).addAnnouncement(commandInput.getName(),
                commandInput.getDescription());
        objectNode.put("message", results);

        return objectNode;
    }
    /**
     * removeAnnouncement
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            objectNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (!(user.getUserType().equals(Enums.UserType.HOST))) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
            return objectNode;
        }

        Host host = (Host) user;
        String results = host.removeAnnouncement(host, commandInput.getName());
        objectNode.put("message", results);

        return objectNode;
    }
    /**
     * showPodcasts
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
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
    /**
     * removeAlbum
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
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
        if (user.getUserType().equals(Enums.UserType.ARTIST)) {
            Artist artist = (Artist) user;
            String albumName = commandInput.getName();

            String message = artist.removeAlbum(albumName);
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
    /**
     * changePage
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String nextPage = commandInput.getNextPage();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (nextPage != null) {
            if (nextPage.equals("LikedContent")) {
               // user.getFollowedPlaylists().clear();;
                user.setCurrentPageType("LikedContentPage");
                user.setCurrentPageType("LikedContentPage", user);
            } else if (nextPage.equals("Home")) {
                user.getFollowedPlaylists().clear();;
                user.setCurrentPageType("HomePage");
                user.setCurrentPageType("HomePage", user);
            }
            String message = user.getUsername()
                    + " accessed " + nextPage + " successfully.";
            objectNode.put("message", message);
        } else {
            String message = user.getUsername() + " is trying to access a non-existent page.";
            objectNode.put("message", message);
        }


        return objectNode;
    }
    /**
     * removePodcast
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            String message = "No user found for username: "
                    + commandInput.getUsername();
            objectNode.put("message", message);
            return objectNode;
        }


        if (user.getUserType().equals(Enums.UserType.HOST)) {
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
    /**
     * getTop5Albums
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }
    /**
     * RemoveEvent
     *
     * @param commandInput the command input
     * @return a
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        if (user == null) {
            objectNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (!(user instanceof Artist)) {
            objectNode.put("message", commandInput.getUsername() + " is not a artist.");
            return objectNode;
        }

        String results = ((Artist) user).removeEvent(commandInput.getName());
        objectNode.put("message", results);

        return objectNode;
    }
}

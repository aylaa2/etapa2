package app;

import app.audio.Collections.Album;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.Song;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.Artist;
import app.user.User;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
            objectNode.put("message", "The username" + commandInput.getUsername() + "is not an artist.");
            return objectNode;
        }

        String results = ((Artist)user).addAlbum(commandInput.getName(), commandInput.getReleaseYear(), commandInput.getDescription(), commandInput.getSongs());
        objectNode.put("message", results);

        return objectNode;
    }
    public static ObjectNode showAlbums(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
       // System.out.println("Retrieved user class: " + (user != null ? user.getClass().getName() : "null"));
        if (user == null) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", "User not found.");
            return objectNode;
        }

        if (!(user instanceof Artist)) {
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("message", "User is not an artist.");
            return objectNode;
        }

        List<Album> albums = ((Artist) user).showAlbums();
        List<PartialAlbum> partialAlbums = new ArrayList<>();

        for( Album album : albums) {
            PartialAlbum albumsObject= new PartialAlbum();
            for(Song song : album.getSongs()){
                albumsObject.songs.add(song.getName());
            }
            albumsObject.name = album.getName();
            partialAlbums.add(albumsObject);
        }
        objectNode.put("user", commandInput.getUsername());
        objectNode.set("result", objectMapper.valueToTree(partialAlbums));

        return objectNode;
    }
    @Getter
    @Setter
    public static class PartialAlbum {
        private String name;
        private List<String> songs = new ArrayList<>();

    }

}

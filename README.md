## the final mark on the manual checker is 80/100 , tests 3 and 9 do not pass in vm checker

# Music Library Management System

This project is a command-line based application that manages a music library system. It allows users to interact with a virtual library, handling commands to search, play, and manage songs and playlists.

## Installation

To run this project, you will need to have Java installed on your system.

1. Clone the repository to your local machine.
2. Compile the code using a Java compiler (JDK).
3. Ensure all dependencies are correctly configured.

## Usage

The following commands are available within the application:

- `search`: Search for songs, albums, or users.
- `select`: Select a specific song or album.
- `load`: Load a song or album.
- `playPause`: Toggle play/pause on the current selection.
- `repeat`: Toggle repeat functionality.
- `shuffle`: Toggle shuffle functionality.
- `forward`: Skip forward in the playlist.
- `backward`: Skip backward in the playlist.
- `like`: Like a song or album.
- `next`: Move to the next song.
- `prev`: Move to the previous song.
- `createPlaylist`: Create a new playlist.
- `addRemoveInPlaylist`: Add or remove items from a playlist.
- `switchVisibility`: Switch the visibility of a playlist.
- `showPlaylists`: Display all playlists.
- `follow`: Follow a user or artist.
- `status`: Show the status of the current user.
- `showPreferredSongs`: Display preferred songs.
- `getPreferredGenre`: Get the most preferred genre of the user.
- `getTop5Songs`: Display top 5 songs.
- `getTop5Playlists`: Display top 5 playlists.
- `switchConnectionStatus`: Switch the online/offline status.
- `getOnlineUsers`: Display all online users.
- `addUser`: Add a new user.
- `addAlbum`: Add a new album.
- `showAlbums`: Show all albums.
- `printCurrentPage`: Print the current page in pagination.
- `addEvent`: Add a new event.
- `getAllUsers`: Get all users.
- `deleteUser`: Delete a user.
- `addMerch`: Add new merchandise.
- `addPodcast`: Add a new podcast.
- `addAnnouncement`: Make a new announcement.
- `removeAnnouncement`: Remove an announcement.
- `showPodcasts`: Display all podcasts.
- `removeAlbum`: Remove an album.
- `changePage`: Change the current page in pagination.
- `removePodcast`: Remove a podcast.
- `getTop5Albums`: Display the top 5 albums.
- `removeEvent`: Remove an event.

# Pagination Module for Music Library Management System

The pagination module is a part of the Music Library Management System, which facilitates the display of different types of content pages within the application. Below are the descriptions of the classes and their functionalities.

## Classes

### `Page`
The abstract `Page` class represents a generic page with content. Each page is owned by a `User` and has an abstract method `displayPage()` that is used to display the content of the page.

### `ArtistPage`
Extends `Page`. The `ArtistPage` class is specific to `Artist` users and displays content such as albums, merchandise, and events associated with the artist.

### `HostPage`
Extends `Page`. The `HostPage` class is specific to `Host` users and displays content such as podcasts, merchandise, and Announcements associated with the host.

### `HomePage`
Extends `Page`. The `HomePage` class represents the home page for regular users. It displays content like top liked songs and playlists followed by the user.

### `LikedContentPage`
Extends `Page`. The `LikedContentPage` class shows all the content liked by the user, including songs and playlists.

## Usage

The system determines which page to render through the `printCurrentPage` method, which takes a `User` object and their role as parameters.

# Music Library Management System

## Usage

### Switching User Status

To toggle the online status of a user:

user.switchStatus();


### Add User

To add a new user, invoke the `addUser` method with the required user details:

# User Role Management in Music Library Management System

The system includes different user roles with specific capabilities, such as `Artist`, `Host`, and general `User`. Each role has access to various functions within the music library system.

## User Roles

### `User`
The base user class allows for general user activities such as creating playlists, liking songs, and following other playlists.

### `Artist`
Artists can manage their albums, songs, and related events. They have additional capabilities like adding new albums, creating merchandise, and managing their events.

### `Host`
Hosts are specialized users who can manage podcasts and make announcements. They can add or remove podcasts and manage their episodes and announcements.

## Functionalities

Each user role has specific functionalities associated with it:

### General User Functionalities

- Manage playlists and liked songs.
- Interact with the music player.
- Use the search bar to find music.

### Artist Functionalities

- `addAlbum`: Add a new album with songs.
- `showAlbums`: Retrieve a list of the artist's albums.
- `removeAlbum`: Remove an existing album.
- `addEvent`: Add a new event associated with the artist.
- `removeEvent`: Remove an existing event.
- `addMerch`: Add new merchandise for sale.

### Host Functionalities

- `addPodcast`: Create a new podcast with episodes.
- `showPodcasts`: Display a list of the host's podcasts.
- `removePodcast`: Delete a podcast from the list.
- `addAnnouncement`: Make a new announcement.
- `removeAnnouncement`: Remove an existing announcement.

### Design Patterns Used

In the development of the Music Library Management System, various design patterns were employed to ensure clean, efficient, and maintainable code. Two notable patterns used are:

#### Streams, iterators, ...
Java Streams have been used extensively for collection processing. Streams provide a high-level abstraction for performing operations on collections, such as map-reduce transformations, which are both readable and concise. An example is in the `getTop5Albums` method, where a stream is used to sort albums by their popularity and collect the top five entries.

Example usage:

sortedEntries.stream()
    .map(entry -> entry.getKey().getName())
    .distinct()
    .limit(5)
    .forEach(topAlbums::add);

### skelet used 
used the official Stage I solve as a skeleton.


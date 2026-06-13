import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OOP12MutableObjectsAndDefensiveCopies {
    /*
     * Mutable objects can change state. Defensive copies prevent outside code
     * from accidentally changing internal collections.
     */
    static class Playlist {
        private final List<String> songs;

        Playlist(List<String> songs) {
            this.songs = new ArrayList<>(songs);
        }

        void addSong(String song) {
            songs.add(song);
        }

        List<String> songs() {
            return Collections.unmodifiableList(new ArrayList<>(songs));
        }
    }

    public static void main(String[] args) {
        List<String> input = new ArrayList<>();
        input.add("Intro to Java");

        Playlist playlist = new Playlist(input);
        input.add("External change ignored");
        playlist.addSong("OOP Concepts");

        System.out.println(playlist.songs());
    }
}

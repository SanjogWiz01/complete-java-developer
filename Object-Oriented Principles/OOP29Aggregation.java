import java.util.ArrayList;
import java.util.List;

public class OOP29Aggregation {
    /*
     * Aggregation is a weak whole-part relationship. Parts can continue to
     * exist even if the whole object is removed.
     */
    static class Player {
        private final String name;

        Player(String name) {
            this.name = name;
        }

        String name() {
            return name;
        }
    }

    static class Team {
        private final List<Player> players = new ArrayList<>();

        void addPlayer(Player player) {
            players.add(player);
        }

        int size() {
            return players.size();
        }
    }

    public static void main(String[] args) {
        Player player = new Player("Aarav");
        Team team = new Team();
        team.addPlayer(player);

        System.out.println(player.name() + " belongs to a team of " + team.size());
    }
}

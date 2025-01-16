package JavaBeta;

/* RoundRobin
 * Round Robin is a tournament. Every team plays each other once. The winner has the best record
 */

// Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;

public class RoundRobin extends Tournament {

    // Constructors
    // Creating new round robin tournament
    public RoundRobin(int id, int nTeams, String title, String white, Admin admin, String prize){
        super(id, nTeams, title, white, admin, prize);
    }

    // Creating existing tournament
    public RoundRobin(int id, int nTeams, String prize, String title, String white, ArrayList<Team> teams, Queue<Game> games, ArrayList<Record> records, int count, Admin admin, boolean start){
        super(id, nTeams, prize, title, white, teams, games, records, count, admin, start);
    }


    // Funcitons
    public ArrayList<Record> sortStandings() {
        // Sorting by W:L Ratio, Followed by number of wins, Followed by number of losses
        // Future implementations should be able to edit the way we sort the standings, but for now this will suffice
        Collections.sort(getRecords(), Comparator.comparingDouble(Record::wlratio).thenComparing(Record :: getnWin).reversed());
        return getRecords();
    }

    // Rotates the teams by 1 space
    private void rotate(ArrayList<Team> te) {
        Team temp = te.get(0);
        te.remove(0);
        te.add(temp);
    }

    // Overwritten Methods
    @Override
    public void makeGames() {
        // Generate the number of rouds to compete in
        int rounds = getnTeams() - 1;

        // Randomize the order of the teams
        randomize();

        // Fix the location of one of the teams
        Team t1 = getTeams().get(0);

        // Make a copy of all the teams minus the fixed team
        @SuppressWarnings("unchecked")
        ArrayList<Team> unfixed = (ArrayList<Team>) getTeams().clone();
        unfixed.remove(0);

        // Generate the matches
        Game g;
        Queue<Game> games = getGames();
        for (int i = 0; i < rounds; i++) {
            // Team 1 vs First in Rotated Team List
            g = new Game(newID(), t1, unfixed.get(0), findRecord(t1), findRecord(unfixed.get(0)));
            games.add(g);

            // 2nd in Rotated List vs Last in Rotated List, 3rd vs 2nd Last, etc. 
            int c = 1;
            for (int j = rounds - 1; j > rounds/2; j--) {
                g = new Game(newID(), unfixed.get(c), unfixed.get(j), findRecord(unfixed.get(c)), findRecord(unfixed.get(j)));
                c++;
                games.add(g);
            }

            // Rotate the teams
            rotate(unfixed);
        }

    }

    // Is the tournament finished
    @Override
    public void updateStatus() {
        if (!getGames().isEmpty()) setStatus(false); // There is still games so not complete
        setStatus(true);
    }

    // Generate the Text for the standings
    @Override
    public String standings() {
        String s = "Standings" + ":\n";

        // Display standings
        ArrayList<Record> rec = sortStandings();
        for (Record r : rec) {
            s = s + r.toString() + "\n";
        }

        return s;
    }
    
}

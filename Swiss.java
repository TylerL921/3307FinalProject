package JavaBeta;

/* Swiss
 * Swiss is another form of tournament. 
 * Every team will keep playing a team with a similar record to each other.
 * Winner is the one with the best record
 */

// Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;

public class Swiss extends Tournament {
    // Variables
    private int round, maxround;

    // Constructors
    // Creating a new Swiss tournament
    public Swiss(int id, int nTeams, String title, String white, Admin admin, String prize) {
        super(id, nTeams, title, white, admin, prize);
        round = 0;
        maxround = howManyRounds();
    }

    // Create a new Swiss tournament but you want a specific number of rounds
    public Swiss (int id, int nTeams, String title, String white, Admin admin, String prize, int mRound) {
        super(id, nTeams, title, white, admin, prize);
        round = 0;

        int max = howManyRounds();
        if (mRound < max) maxround = mRound;
        else maxround = max;
    }

    // Create existing Swiss tournament
    public Swiss (int id, int nTeams, String prize, String title, String white, ArrayList<Team> teams, Queue<Game> games, ArrayList<Record> records, int count, Admin admin, boolean start, int round, int maxround) {
        super(id, nTeams, prize, title, white, teams, games, records, count, admin, start);
        this.round = round;
        this.maxround = maxround;
    }

    // Fucntions
    // Calculate how many rounds of swiss should be completed
    private int howManyRounds(){
        int result = (int)(Math.log(getnTeams()) / Math.log(2));
        return result;
    }

    // Overwritten Methods
    @Override
    public ArrayList<Record> sortStandings() {
        // Sorting by W:L Ratio, Followed by number of wins, Followed by number of losses
        // Future implementations should be able to edit the way we sort the standings, but for now this will suffice
        Collections.sort(getRecords(), Comparator.comparingDouble(Record::wlratio).thenComparing(Record :: getnWin).reversed());
        return getRecords();
    }

    @Override
    public void makeGames() {
        if (getStatus()) return; //Tournament is complete
        if (!getGames().isEmpty()) return; // Must wait for all games to complete

        int seed = 0;
        int matches = getnTeams()/2;
        ArrayList<Record> r = sortStandings();
        Queue<Game> gm = getGames();

        for(int i = 1; i <= matches; i++) {
            // Team seed 1 v team seed 2, team seed 3 v team seed 4, etc.
            // If odd, bottom seed gets a bi 
            Game g = new Game(newID(), DataManager.findTeam(r.get(seed).getID()[1], getTeams()), DataManager.findTeam(r.get(seed + 1).getID()[1], getTeams()), r.get(seed), r.get(seed + 1));
            gm.add(g);
            seed = seed + 2;
        }

        round++;
    }

    // Determine is tournament is complete
    @Override
    public void updateStatus() {
        if (!getGames().isEmpty()) setStatus(false); // Not all games completed
        if (round != maxround) setStatus(false); // Not all rounds are complete
        setStatus(true);
    }

    // Generate String of standings
    @Override
    public String standings() {
        String s = "Swiss Standings Round " + round + ":\n";

        // Display standings
        ArrayList<Record> rec = sortStandings();
        for (Record r : rec) {
            s = s + r.toString() + "\n";
        }

        // Check status
        if (getStatus()) s = s + "Final Standings";
        else {
            int ro = maxround - round;
            s = s + ro + " rounds remain.";
        }
        return s;
    }

    // Getters and Setters
    public int getRound() {
        return round;
    }
    public int getMaxround() {
        return maxround;
    }
    public void setRound(int round) {
        this.round = round;
    }
    public void setMaxround(int maxround) {
        this.maxround = maxround;
    }
}

package JavaBeta;

/* DoubleElim
 * A popular style of Tournament. Teams that lose 2 games are eliminated.
 */

// Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;

public class DoubleElim extends Tournament {
    // Variables
    private Boolean seeded;
    private ArrayList<Team> upper, lower;

    // Constructors
    // Creating a brand new Tournament.
    public DoubleElim (int id, int nTeams, String title, String white, Admin admin, String prize, boolean seed) {
        super(id, nTeams, title, white, admin, prize);
        seeded = seed;
        upper = new ArrayList<Team>();
        lower = new ArrayList<Team>();
    }

    // Creating an existing Tournament.
    @SuppressWarnings("unchecked")
    public DoubleElim (int id, int nTeams, String prize, String title, String white, ArrayList<Team> teams, Queue<Game> games, ArrayList<Record> records, int count, Admin admin, boolean start, boolean seed) {
        super(id, nTeams, prize, title, white, teams, games, records, count, admin, start);
        seeded = seed;
        upper = (ArrayList<Team>) teams.clone();
        lower = (ArrayList<Team>) teams.clone();
        eliminate();
    }

    // Functions

    /** Elimiate
     * This code is used to eliminate the teams that are out of contention.
     * Teams not in contention do not have anymore games.
     * For double elimination that means 2 losses and you are out.
     */
    private void eliminate() {
        for (Record r:getRecords()) {
            if (r.getnLoss() == 1) {
                upper.remove(DataManager.findTeam(r.getID()[1], getTeams()));
                lower.add(DataManager.findTeam(r.getID()[1], getTeams()));
            }
            if (r.getnLoss() > 1) {
                lower.remove(DataManager.findTeam(r.getID()[1], getTeams()));
            }
        }
    }

    // Adds fake teams until the number of teams is base 2.
    // Every time a matchup is against bi its a free win
    private void base2() {
        Team bi = new Team("BI", "NULL", 0);
        while (!isBase2(upper.size())) {
            upper.add(bi);
        }
    }
    // Checks if tournament is in base 2. 
    private Boolean isBase2(int x){
        return (x & (x - 1)) == 0;
    }

    // Sort the teams for bracket creation
    private void bracketMake(){
        // Variables
        ArrayList<Team> teams = getTeams();
        ArrayList<Team> reordered;
        int size = teams.size();
        Team temp;
        
        for (int i = 1; Math.pow(2, i) < size; i++) { // Log_2(size) Resorts are required
            reordered = new ArrayList<Team>();

            // Every resort round the grouping of 
            for (int j = 0; j < size/Math.pow(2, i); j++) { 
                int k, c;
                c = 0;
                for (k = i; k > 0; k--){
                    temp = teams.get(c++);
                    reordered.add(temp);
                    temp = teams.get(teams.size() - k);
                    reordered.add(temp);
                }

                for (k = i; k > 0; k--){
                    teams.remove(0);
                    teams.remove((teams.size() - 1));
                }
            }
            teams = reordered;
        }
        upper = teams;
    }

    private void reorganize() {
        eliminate();

        ArrayList<Team> reordered = new ArrayList<>();
        Team temp;
        int c = 0;

        for (int j = lower.size() - 1; j >= lower.size()/2; j--) {
            temp = lower.get(j);
            reordered.add(temp);
            temp = lower.get(c);
            reordered.add(temp);
            c++;
        }

        lower = reordered;
    }

    // Overwritten Methods
    @Override
    public void makeGames() {
        Queue<Game> games = getGames();

        // Only Generate More Games Once Old Ones Are Done
        if (!games.isEmpty()) return;

        // Randomize Seeding If Not Seeded or all ready Started
        if (!seeded && !getStarted()) randomize();

        // Make sure number of teams still in is base 2
        base2();

        // Need to reorganize for bracket if not done allready
        if(!getStarted()) bracketMake();
        setStarted(true);

        // Generate Matches
        reorganize();
        Game g;

        // Upper Bracket Matches
        for (int i = 0; i < upper.size(); i++) {
            Team t1 = upper.get(i);
            Team t2 = upper.get(i + 1);
            g = new Game(newID(), t1, t2, findRecord(t1), findRecord(t2));
            games.add(g);
            i++;
        }

        // Lower Bracket Matches
        for (int i = 0; i < lower.size(); i++) {
            Team t1 = lower.get(i);
            Team t2 = lower.get(i + 1);
            g = new Game(newID(), t1, t2, findRecord(t1), findRecord(t2));
            games.add(g);
            i++;
        }

    }

    @Override
    public void updateStatus() {
        // If only one team is not eliminated then tournament complete
        if (getGames().size() > 0) setStatus(false);
        else if (upper.size() > 1) setStatus(false);
        else if (lower.size() > 1) setStatus(false);
        else setStatus(true);
    }

    // Generate String for standings
    @Override
    public String standings() {
        String s = "Standings:\n";

        // Display standings
        ArrayList<Record> rec = sortStandings();
        int w = rec.get(0).getnWin();
        int place = 1;
        for (Record r : rec) {
            if (w != r.getnWin()) place++;
            s = s + "Place: " + place + ": " + r.toString() + "\n";
        }
        return s;
    }

    // Sort Standings
    @Override
    public ArrayList<Record> sortStandings() {
        // Sort by Losses followed by Wins.
        Collections.sort(getRecords(), Comparator.comparingInt(Record::getnLoss).reversed().thenComparingInt(Record::getnWin).reversed());
        return getRecords();
    }

    // Getters and Setters
    public Boolean getSeeded() {
        return seeded;
    }
    public void setSeeded(Boolean seeded) {
        this.seeded = seeded;
    }
    
}

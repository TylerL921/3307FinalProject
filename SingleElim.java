package JavaBeta;

/* SingleElim
 * Single Elimintaiton is another popular tournament format.
 * Every team is placed into a bracket. If you lose you are out. Last one standing is the winner
 */

// Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;

public class SingleElim extends Tournament {
    // Variables
    private Boolean seeded;
    private ArrayList<Team> notElim;

    // Constructors
    // Creating a new single elim tournament
    public SingleElim (int id, int n, String t, String w, Admin a, String p, boolean s) {
        super(id, n, t, w, a, p);
        seeded = s;
        notElim = new ArrayList<Team>();
    }

    // Creating an existing tournament
    @SuppressWarnings("unchecked")
    public SingleElim (int id, int nTeams, String prize, String title, String white, ArrayList<Team> teams, Queue<Game> games, ArrayList<Record> records, int count, Admin admin, boolean start, boolean seed) {
        super(id, nTeams, prize, title, white, teams, games, records, count, admin, start);
        seeded = seed;
        notElim = (ArrayList<Team>) teams.clone();
        eliminate();
    }

    // Functions
    // Remove all teams that have been eliminated in the bracket
    private void eliminate() {
        for (Record r:getRecords()) {
            if (r.getnLoss() > 0) {
                notElim.remove(DataManager.findTeam(r.getID()[1], notElim));
            }
        }
    }

    // Adds fake teams until the number of teams is base 2.
    // Every time a matchup is against bi its a free win
    private void base2() {
        Team bi = new Team("BI", "NULL", 0);
        while (!isBase2(notElim.size())) {
            notElim.add(bi);
        }
    }
    // Checks if tournament is in base 2. 
    private Boolean isBase2(int x){
        return (x & (x - 1)) == 0;
    }

    // Sort the teams for bracket creation
    @SuppressWarnings("unchecked")
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
        notElim = (ArrayList<Team>) teams.clone();
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
        Game g;
        for (int i = 0; i < notElim.size(); i++) {
            g = new Game(newID(), notElim.get(i), notElim.get(i + 1), getRecords().get(i), getRecords().get(i + 1));
            games.add(g);
            i++;
        }
    }

    // Display status
    @Override
    public void updateStatus() {
        // If only one team is undefeated tournament is complete
        if (notElim.size() > 1) setStatus(false);
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
    
    //Getters and Setters
    public Boolean getSeeded() {
        return seeded;
    }
    public void setSeeded(Boolean seeded) {
        this.seeded = seeded;
    }

}

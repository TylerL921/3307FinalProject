package JavaBeta;

/* Tournament
 * Generic object for tournament. All tournaments have a name, ID, list of teams, list of games, etc.
 * Whitelist tournaments will require a password to join.
 */

// Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public abstract class Tournament implements iTournament {
    // Variables
    public static int N_MATCHES = 1000; // Upper Limit of matches For a tournament (Used for game codes) 
    private int tournamentID, nTeams, count;
    private String prize, title, whitelist;
    private ArrayList<Team> teams;
    private ArrayList<Record> records;
    private Queue<Game> games;
    private Admin admin;
    private boolean status, started;

    // Constructors
    // Create a new tournament
    public Tournament(int id, int nTeams, String title, String white, Admin admin, String prize) {
        count = 0;
        tournamentID = id;
        started = false;
        this.nTeams = nTeams;
        this.title = title;
        this.prize = prize;
        this.admin = admin;
        whitelist = white;
        teams = new ArrayList<Team>();
        records = new ArrayList<Record>();
        games = new LinkedList<Game>();
    }

    // Create an existing tournament
    public Tournament(int id, int nTeams, String prize, String title, String white, ArrayList<Team> teams, Queue<Game> games, ArrayList<Record> records, int count, Admin admin, boolean start) {
        tournamentID = id;
        whitelist = white;
        started = start;
        this.nTeams = nTeams;
        this.prize = prize;
        this.title = title;
        this.teams = teams;
        this.games = games;
        this.records = records;
        this.count = count;
        this.admin = admin;
    }

    // Fucntions
    // Add team to tournament
    public Boolean addTeam (Team t, String password){
        if (whitelist.equals("")) return true;
        if (!whitelist.equals(password)) return false;

        if (teams.size() < nTeams) {
            teams.add(t);
            t.addTournament(this);

            int[] id = {gettID(), t.getID()};
            Record record = new Record(t.getName(), getTitle(), id);

            t.addRecord(record);

            return true;
        }
        return false;
    }

    // Remove team from tournament
    public void removeTeam (Team t){
        teams.remove(t);
    }

    // Generates a new ID for a game
    public int newID(){
        return (tournamentID * N_MATCHES) + count++;
    }

    // Randomize the order of the teams
    public void randomize() {
        Collections.shuffle(teams, new Random());
    }

    // Find a record given a team
    public Record findRecord(Team team) {
        for (Record r: records){
            if (DataManager.findTeam(r.getID()[1], teams) == team) {
                return r;
            }
        }
        return null;
    }

    
    // Will be overwritten by specified tournaments
    public void makeGames(){started = true;}
    public String standings(){return "";}
    public void updateStatus(){}
    public ArrayList<Record> sortStandings(){return null;}

    // Getters and Setters
    public int gettID() {
        return tournamentID;
    }
    public Queue<Game> getGames() {
        return games;
    }
    public String getPrize() {
        return prize;
    }
    public ArrayList<Team> getTeams() {
        return teams;
    }
    public String getTitle() {
        return title;
    }
    public String getWhitelist() {
        return whitelist;
    }
    public int getnTeams() {
        return nTeams;
    }
    public ArrayList<Record> getRecords() {
        return records;
    }
    public int getCount() {
        return count;
    }
    public boolean getStatus () {
        return status;
    }
    public Admin getAdmin() {
        return admin;
    }
    public boolean getStarted(){
        return started;
    }
    public void setGames(Queue<Game> games) {
        this.games = games;
    }
    public void setPrize(String prize) {
        this.prize = prize;
    }
    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
    }
    public void setnTeams(int nTeams) {
        this.nTeams = nTeams;
    }
    public void settID(int tID) {
        this.tournamentID = tID;
    }
    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }
}

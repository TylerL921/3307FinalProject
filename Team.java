package JavaBeta;

/* Team
 * Teams have players. They compete in tournaments and have a record.
 * Active members and inactive members are stored here.
 */

// Imports
import java.util.ArrayList;

public class Team {
    // Variables
    private int ID;
    private String name, managerID;
    private ArrayList<Tournament> tourIn;
    private ArrayList<Record> appear;
    private ArrayList<Player> active, inactive;
    private Boolean status;

    // Constructors
    // New Team
    public Team(String n, String mID, int tID) {
        name = n;
        ID = tID;
        managerID = mID;
        status = true;
        appear = new ArrayList<Record>();
        active = new ArrayList<Player>();
        inactive = new ArrayList<Player>();
        tourIn = new ArrayList<Tournament>();
    }

    // Existing Team
    public Team(String n, String ID, ArrayList<Record> a, ArrayList<Player> p, Boolean s, ArrayList<Player> i){
        name = n;
        managerID = ID;
        appear = a;
        active = p;
        status = s;
        inactive = i;
        tourIn = new ArrayList<Tournament>();
    }

    // Fucntions
    // Add player to roster
    public boolean addPlayer(Player p) {
        if(UserActions.findPlayer(p.getUsername(), active) != null) return false;
        active.add(p);
        return true;
    }

    // Remove player from active roster
    public boolean removePlayer(String u){
        int size = active.size();
        active.remove(UserActions.findPlayer(u, active));

        if (size == active.size()) return false;
        return true;
    }

    public void removeTournament(Tournament tournament) {
        tourIn.remove(tournament);
    }

    // Set all active members to inactive
    public boolean disbandTeam(){
        if(tourIn.size() > 0) return false;

        for(Player p: active) {
            inactive.add(p);
            active.remove(p);
        }
        status = false;
        return true;
    }

    // Add Tournament to participating in
    public void addTournament(Tournament t) {
        tourIn.add(t);
    }

    public void addRecord(Record record) {
        appear.add(record);

        for (Player player : active) {
            player.addRecord(record);
        }
    }


    // Getters and Setters
    public ArrayList<Record> getAppear() {
        return appear;
    }
    public String getManagerID() {
        return managerID;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Player> getActive() {
        return active;
    }
    public int getID() {
        return ID;
    }
    public Boolean getStatus() {
        return status;
    }
    public ArrayList<Player> getInactive() {
        return inactive;
    }
    public ArrayList<Tournament> getTourIn() {
        return tourIn;
    }
    public void setAppear(ArrayList<Record> appear) {
        this.appear = appear;
    }
    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setActive(ArrayList<Player> players) {
        this.active = players;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public void setInactive(ArrayList<Player> inactive) {
        this.inactive = inactive;
    }
    public void setTourIn(ArrayList<Tournament> in) {
        this.tourIn = in;
    }
}

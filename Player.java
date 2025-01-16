package JavaBeta;

/* Player
 * Player is a type of user. They join teams.
 */

// Imports
import java.util.ArrayList;

public class Player  extends User{
    // Variables
    private ArrayList<Record> accolades;
    private Team team;

    // Constructors
    // New Player
    public Player(String u, String p, String n) {
        super(u, p, n);
        accolades = new ArrayList<Record>();
        team = null;
    }

    // Funtions
    public void addRecord(Record record){
        accolades.add(record);
    }

    public String toString() {
        String s = getName() + " aka. " + getUsername() + "\n Tournament Appearances: \n";
        for (Record record : accolades){
            s = s + record.toString() + "\n";
        }
        return s;
    }
    
    // Getters and Setters
    public ArrayList<Record> getAccolades() {
        return accolades;
    }
    public Team getTeam() {
        return team;
    }
    public void setAccolades(ArrayList<Record> accolades) {
        this.accolades = accolades;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
}

package JavaBeta;

/* Admin
 * This is a type of user. Admins have the power to create tournaments 
 * They are responsible for reporting the scores and generating games.
 */

// Imports
import java.util.*;

public class Admin extends User{
    // Variables
    private ArrayList<Tournament> tournaments;

    // Constructor
    public Admin(String u, String p, String n) {
        super(u, p, n);
        tournaments = new ArrayList<>();
    }
    
    // Functions
    // Add Tournament to list
    public void addTournament(Tournament tournament) {
        tournaments.add(tournament);
    }

    // Remove Tournament from list
    public void removeTournament(Tournament tournament) {
        tournaments.remove(tournament);
    }

    // Getters and Setters
    public ArrayList<Tournament> getTournaments() {
        return tournaments;
    }
    public void setTournaments(ArrayList<Tournament> tournaments) {
        this.tournaments = tournaments;
    }
}

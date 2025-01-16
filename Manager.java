package JavaBeta;

/* Manager
 * Is a type of User. Managers controls teams. They can add or remove players to a roster.
 * Managers also register teams into tournaments.
 */

// Imports
import java.util.ArrayList;

public class Manager extends User{
    // Variables
    private ArrayList<Team> teams;

    // Constructor
    public Manager(String u, String p, String n) {
        super(u, p, n);
        teams = new ArrayList<Team>();
    }
    
    // Functions
    // Makes team
    public void makeTeam(String name, int ID){
        Team team = new Team(name, getUsername(), ID);
        teams.add(team);
    }

    // Add team to teams
    public void addTeam(Team team) {
        teams.add(team);
    }

    // Add a player to a specific team
    public boolean addPlayer(int teamID, Player p){
        // Search for team
        Team t = DataManager.findTeam(teamID, teams);

        // Add player to team. Returns False if player allready in team
        if (t == null) return false;
        if (t.getID() == teamID) return t.addPlayer(p);
        return false;
    }

    // Getters and Setters
    public ArrayList<Team> getTeams(){
        return teams;
    }
    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }
}

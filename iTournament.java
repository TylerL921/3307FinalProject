package JavaBeta;

/* iTournament
 * Every tournament has games. But the matchmaking differs between them.
 * Other things that differ is the display and organization of the standings.
 */

// Imports 
import java.util.ArrayList;

public interface iTournament {
    public void makeGames();
    public String standings();
    public void updateStatus();
    public ArrayList<Record> sortStandings();
}

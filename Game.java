package JavaBeta;

/* Game
 * Tournaments have matches or games. This object represents them. 
 * Games have an ID to identify it, two teams, a winner and loser, and a score.
 */

public class Game {
    // Variables
    private int gID; // Game ID
    private int[] score; // Score
    private Team[] teams; // Teams involved
    private Record[] records; // Records for the teams

    // Constructors
    // Generating a new game
    public Game(int id, Team t1, Team t2, Record r1, Record r2){
        gID = id;
        score = new int[2];
        teams = new Team[2];
        records = new Record[2];
        teams[0] = t1;
        teams[1] = t2;
        records[0] = r1;
        records[1] = r2;
    }

    // Generating an existing game
    public Game(int id, int[]s, Team[] t, Record[] r) {
        gID = id;
        score = s;
        teams = t;
        records = r;
    }

    // Functions
    // Determine winner
    public Team winner() {
        if (score[0] > score[1]){ // Team 1 won
            records[0].win();
            records[1].loss();
            return teams[0];
        } else if (score[0] < score[1]) { // Team 2 won
            records[1].win();
            records[0].loss();
            return teams[1];
        } 

        // Draw
        return null;
    }

    // Resets the scores back to 0-0
    public void resetScore(){
        score[0] = 0;
        score[1] = 0;
    }

    // Creates a string of the final score
    public String finalScore() {
        String s = teams[0] + " " + score[0] + " - " + teams[1] + score[1];
        return s;
    }

    // Overwritten
    @Override
    public String toString(){
        String s = "Game: " + gID + " ";
        s = s + teams[0] + " vs. " + teams[1];
        return s;
    }

    // Getters and Setters
    public void setRecords(Record[] records) {
        this.records = records;
    }
    public void setScore(int[] score) {
        this.score = score;
    }
    public void setTeams(Team[] teams) {
        this.teams = teams;
    }
    public void setgID(int gID) {
        this.gID = gID;
    }
    public Record[] getRecords() {
        return records;
    }
    public int[] getScore() {
        return score;
    }
    public Team[] getTeams() {
        return teams;
    }
    public int getgID() {
        return gID;
    }
}

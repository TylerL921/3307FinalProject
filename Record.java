package JavaBeta;

/* Record
 * Every tournament a team participates in has a record.
 * This holds the data about how they perfomed in the tournament.
 */
public class Record {
    // Variables
    private String teamName, tourName;
    private int nWin, nLoss, place;
    private int[] ID;

    // Constructors
    // Create new record
    public Record(String team, String tour, int[] id){
        nWin = 0;
        nLoss = 0;
        place = 0;
        ID = id;
        tourName = tour;
        teamName = team;
    }

    public Record(int w, int l, int p, String tour, String team, int[] id){
        nWin = w;
        nLoss = l;
        place = p;
        tourName = tour;
        teamName = team;
        ID = id;
    }

    // Funcitons
    public void win() {
        nWin++;
    }
    public void loss(){
        nLoss++;
    }

    public double wlratio() {
        if (nLoss == 0) return 1;
        return nWin/nLoss;
    }

    public boolean same(int[] i) {
        if (i[0] == ID[0] && i[1] == ID[1]) return true;
        else return false;
    }
    
    // Overwritten Methods
    @Override
    public String toString(){
        String s = teamName + ": " + tourName + ": ";
        s = s + nWin + " W - " + nLoss + " L";
        return s;
    }

    // Getters and Setters
    public int getnLoss() {
        return nLoss;
    }
    public int getnWin() {
        return nWin;
    }
    public int getPlace() {
        return place;
    }
    public int[] getID() {
        return ID;
    }
    public String getTeamName() {
        return teamName;
    }
    public String getTourName() {
        return tourName;
    }
    public void setnLoss(int nLoss) {
        this.nLoss = nLoss;
    }
    public void setnWin(int nWin) {
        this.nWin = nWin;
    }
    public void setPlace(int place) {
        this.place = place;
    }
    public void setID(int[] iD) {
        ID = iD;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public void setTourName(String tourName) {
        this.tourName = tourName;
    }
}

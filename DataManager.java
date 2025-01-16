package JavaBeta;

/* DataManager
 * This Class is responsible for the reading, writing, and storage of data.
 * Without this the system will not be save any data created durring its previous session.
 * Changes should be made for scalability. 
 */

// Imports
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataManager {
    // Variables
    JSONParser parser = new JSONParser();
    private ArrayList<Team> teams;
    private ArrayList<User> users;
    private ArrayList<Tournament> tournaments;
    private ArrayList<Record> records;

    // File Locations
    private String masterPage;
    private String fUser = ""; // File of User data
    private String fTeam = ""; // File of Team data
    private String fRec = ""; // File of Record data
    private String fTour = ""; // File of Tournament and Game data

    // Constructor
    public DataManager(String m) {
        teams = new ArrayList<Team>();
        users = new ArrayList<User>();
        tournaments = new ArrayList<Tournament>();
        records = new ArrayList<Record>();
        masterPage = m;

        readMaster();
        read();
    }

    // READING FUNCTIONS

    // Get the filepaths of all the other files
    private void readMaster() {
        try {
            File obj = new File(masterPage);
            Scanner r = new Scanner(obj);

            fUser = r.nextLine();
            fTeam = r.nextLine();
            fRec = r.nextLine();
            fTour = r.nextLine();

            r.close();
        } catch (FileNotFoundException e) {
        }
    }

    // Converts all JSON Objects into the proper objects
    public void read(){
        // Variables
        int i;
        JSONObject o;
        JSONArray JA;

        // Try Reading Files and converting them to JSON Arrays
        try {
            // Create JSON Arrays for each file
            JSONArray jTeam = (JSONArray) parser.parse(new FileReader(fTeam));
            JSONArray jUser = (JSONArray) parser.parse(new FileReader(fUser));
            JSONArray jTour = (JSONArray) parser.parse(new FileReader(fTour));
            JSONArray jRec = (JSONArray) parser.parse(new FileReader(fRec));

            // Create all Records
            Record r;
            for (i = 0; i < jRec.size(); i++) {
                o = (JSONObject) jRec.get(i);
                r = new Record((int) o.get("win"), (int) o.get("loss"), (int) o.get("place"), (String) o.get("tourName"), (String) o.get("teamName"), (int[]) o.get("id"));
                records.add(r);
            }

            // Create all user objects
            User u = null;
            JSONObject obj;
            for (i = 0; i < jUser.size(); i++) {
                o = (JSONObject) jUser.get(i);

                switch ((String) o.get("type")) {
                    case "Admin": // Admin
                        u = new Admin((String) o.get("username"), (String) o.get("password"), (String) o.get("name"));
                        break;
                    case "Manager": // Manager
                        u = new Manager((String) o.get("username"), (String) o.get("password"), (String) o.get("name"));
                        break;
                    case "Player": // Player
                        u = new Player((String) o.get("username"), (String) o.get("password"), (String) o.get("name"));

                        // Link Records to Player
                        JA = (JSONArray) o.get("records");
                        Player p = (Player) u;
                        
                        for (int j = 0; j < JA.size(); j++) {
                            obj = (JSONObject) JA.get(i);
                            p.addRecord(findRecord((int[]) obj.get("id"), records));
                        }

                        break;
                }

                users.add(u);
            }


            // Create all teams
            Team team;
            ArrayList<Player> active, inactive;
            ArrayList<Record> rList;
            for (i = 0; i < jTeam.size(); i++) {
                o = (JSONObject) jTeam.get(i);

                JA = (JSONArray) o.get("active");
                active = JAtoPlayer(JA);

                JA = (JSONArray) o.get("inactive");
                inactive = JAtoPlayer(JA);

                JA = (JSONArray) o.get("records");
                rList = JAtoRecord(JA);
                                
                team = new Team((String) o.get("name"), (String) o.get("mID"), rList, active, (boolean) o.get("status"), inactive);

                // Link manager to team
                Manager manager = (Manager) findUser(team.getManagerID(), users);
                manager.addTeam(team);

                // Link players to team
                for (Player player: active) {
                    player.setTeam(team);
                }

                teams.add(team);
            }

            // Create all tournaments and games
            Tournament tournament = null;
            ArrayList<Team> tList;
            Queue<Game> gList;

            for (i = 0; i < jTour.size(); i++) {
                o = (JSONObject) jTour.get(i);

                // Create games from tournament
                tList = generateTeams(stringToIntA((String) o.get("teams")));

                JA = (JSONArray) o.get("games");
                gList = JAtoGame(JA);

                JA = (JSONArray) o.get("records");
                rList = JAtoRecord(JA);

                Admin admin = (Admin) findUser((String) o.get("admin"), users);

                switch ((int) (long) o.get("type")) {
                    case 0: // Single Elim
                    tournament = new SingleElim((int) (long) o.get("id"), (int) (long) o.get("nTeams"), (String) o.get("prize"), (String) o.get("title"), (String) o.get("white"), tList, gList, rList, (int) (long) o.get("count"), admin, (boolean) o.get("started"), (boolean) o.get("seed"));
                    break;
                
                    case 1: // Double Elim
                        tournament = new DoubleElim((int) (long) o.get("id"), (int) (long) o.get("nTeams"), (String) o.get("prize"), (String) o.get("title"), (String) o.get("white"), tList, gList, rList, (int) (long) o.get("count"), admin, (boolean) o.get("started"), (boolean) o.get("seed"));
                        break;

                    case 2: // Swiss
                        tournament = new Swiss((int) (long) o.get("id"), (int) (long) o.get("nTeams"), (String) o.get("prize"), (String) o.get("title"), (String) o.get("white"), tList, gList, rList, (int) (long) o.get("count"), admin, (boolean) o.get("started"), (int) (long) o.get("round"), (int) (long) o.get("maxround"));
                        break;

                    case 3: // Round Robin
                        tournament = new RoundRobin((int) (long) o.get("id"), (int) (long) o.get("nTeams"), (String) o.get("prize"), (String) o.get("title"), (String) o.get("white"), tList, gList, rList, (int) (long) o.get("count"), admin, (boolean) o.get("started"));
                        break;
                }

                tournaments.add(tournament);
            }

        } catch (FileNotFoundException e) {
        } catch (IOException | ParseException e) {
        }

    }

    // WRITING FUNCTIONS

    // Writes all the JSON Files
    public void save (){
        writeRecords();
        writeUsers();
        writeTeams();
        writeTournaments();
    }

    // Converts list of users into JSON Array for storage
    @SuppressWarnings("unchecked")
    private void writeUsers() {
        // Variables
        JSONArray JA = new JSONArray();

        // Convert every User into JSON Object
        for (User user : users) {
            JSONObject j = new JSONObject();
            j.put("username", user.getUsername());
            j.put("password", user.getPassword());
            j.put("name", user.getName());
            
            if (user instanceof Player) {
                JSONArray JA1 = new JSONArray();
                Player player = (Player) user;
                for (Record record : player.getAccolades()) {
                    JSONObject j1 = new JSONObject();
                    j1.put("id", record.getID());
                    JA1.add(j1);
                }

                j.put("records", JA1);
                j.put("type", "Player");

            } else if (user instanceof Manager) { 
                j.put("type", "Manager");
            } else if (user instanceof Admin) {
                j.put("type", "Admin");
            }

            // Add to JSON Array
            JA.add(j);
        }

        // Write the data
        write(JA, fUser);
    }

    // Converts list of records into JSON Array for storage
    @SuppressWarnings("unchecked")
    private void writeRecords() {
        // Variables
        JSONArray JA = new JSONArray();

        // Convert every Record into JSON Object
        for (Record record : records) {
            JSONObject j = new JSONObject();
            j.put("id", record.getID());
            j.put("win", record.getnWin());
            j.put("loss", record.getnLoss());
            j.put("place", record.getPlace());
            j.put("tourName", record.getTourName());
            j.put("teamName", record.getTeamName());

            // Add to JSON Array
            JA.add(j);
        }

        // Write the data
        write(JA, fRec);
    }


    // Converts list of teams into JSON Array for storage
    @SuppressWarnings("unchecked")
    private void writeTeams() {
        // Variables
        JSONArray JA = new JSONArray();

        // Convert every Team into JSON Object
        for (Team team : teams) {
            JSONObject j = new JSONObject();
            j.put("name", team.getName());
            j.put("mID", team.getManagerID());
            j.put("status", team.getStatus());


            // NOTE: Could benifit from iterator
            JSONArray JA1 = new JSONArray();
            for (Record record : team.getAppear()) {
                JSONObject j1 = new JSONObject();
                j1.put("id", record.getID());
                JA1.add(j1);
            }
            j.put("records", JA1);

            for (Player player : team.getActive()) {
                JSONObject j1 = new JSONObject();
                j1.put("id", player.getUsername());
                JA1.add(j1);
            }
            j.put("active", JA1);

            for (Player player : team.getInactive()) {
                JSONObject j1 = new JSONObject();
                j1.put("id", player.getUsername());
                JA1.add(j1);
            }
            j.put("inactive", JA1);

            // Add to JSON Array
            JA.add(j);
        }

        // Write the data
        write(JA, fTeam);
    }

    // Converts Tournaments into JSON Array for storage. Games is nested JSON Array
    @SuppressWarnings("unchecked")
    private void writeTournaments() {
        // Variables
        JSONArray JA = new JSONArray();
        JSONObject j = new JSONObject();

        for (Tournament tournament : tournaments) {
            j.put("id", tournament.gettID());
            j.put("nTeams", tournament.getnTeams());
            j.put("prize", tournament.getPrize());
            j.put("title", tournament.getTitle());
            j.put("white", tournament.getWhitelist());
            j.put("count", tournament.getCount());
            j.put("admin", tournament.getAdmin().getUsername());
            j.put("started", tournament.getStarted());

            JSONArray JA1 = new JSONArray();
            Queue<Game> games = tournament.getGames();
            Game game;
            while (!games.isEmpty()) {
                JSONObject j1 = new JSONObject();
                game = games.poll();

                j1.put("id", game.getgID());
                j1.put("s1", game.getScore()[0]);
                j1.put("s2", game.getScore()[1]);
                j1.put("r1tour", game.getRecords()[0].getID()[0]);
                j1.put("r2tour", game.getRecords()[1].getID()[0]);
                j1.put("r1team", game.getTeams()[0].getID());
                j1.put("r2team", game.getTeams()[1].getID());

                JA1.add(j1);
            }
            j.put("games", JA1);

            JSONArray JA2 = new JSONArray();
            for (Record record : tournament.getRecords()) {
                JSONObject j2 = new JSONObject();
                j2.put("id", record.getID());
                JA1.add(j2);
            }
            j.put("records", JA2);


            ArrayList<Team> t = tournament.getTeams();
            int[] teamNums = new int[t.size()];
            int i = 0;
            for (Team team : t) {
                teamNums[i] = team.getID();
                i++;
            }
            j.put("teams", intAtoString(teamNums));

            // Add Tournament specific data
            if (tournament instanceof Swiss) {
                Swiss swiss = (Swiss) tournament;
                j.put("round", swiss.getRound());
                j.put("maxround", swiss.getMaxround());
                j.put("type", 2);
            } else if (tournament instanceof SingleElim) {
                SingleElim single = (SingleElim) tournament;
                j.put("type", 0);
                j.put("seed", single.getSeeded());
            } else if (tournament instanceof DoubleElim) {
                DoubleElim doubleE = (DoubleElim) tournament;
                j.put("type", 1);
                j.put("seed", doubleE.getSeeded());
            } else if (tournament instanceof RoundRobin) j.put("type", 3);

            // Add to JSONArray
            JA.add(j);
        }

        // Write the data
        write(JA, fTour);
    }


    // Wrties finalized JSON Arrays into the files
    private void write(JSONArray j, String filename) {
        try {
            FileWriter file = new FileWriter(filename);
            file.write(j.toJSONString());
            file.close();

        } catch (IOException e) {
        }
    }

    // HELPER FUNCTIONS

    // Converts the list of player IDS from a json array into a list of players
    private ArrayList<Player> JAtoPlayer(JSONArray JA) {
        ArrayList<Player> players = new ArrayList<Player>();
        Player player;

        for (int i = 0; i < JA.size(); i++){
            JSONObject o = (JSONObject) JA.get(i);
            player = (Player) findUser((String) o.get("id"), users);
            players.add(player);
        }

        return players;
    }

    // Converts the list of record IDS from a json array into a list of records
    private ArrayList<Record> JAtoRecord(JSONArray JA) {
        ArrayList<Record> records = new ArrayList<Record>();

        for (int i = 0; i < JA.size(); i++){
            JSONObject o = (JSONObject) JA.get(i);

            records.add(findRecord((int[]) o.get("id"), records));
        }

        return records;
    }

    // Converts int array into a list of teams
    private ArrayList<Team> generateTeams(int[] t) {
        ArrayList<Team> teams = new ArrayList<Team>();

        for (int i = 0; i < t.length; i++){
            Team team = findTeam(t[i], teams);
            teams.add(team);
        }

        return teams;
    }

    // Converts the list of games into game objects
    private Queue<Game> JAtoGame(JSONArray JA) {
        Queue<Game> games = new LinkedList<Game>();

        for (int i = 0; i < JA.size(); i++) {
            JSONObject o = (JSONObject) JA.get(i);
            int[] score = new int[2];
            Team[] tm = new Team[2];
            Record[] recs = new Record[2];

            score[0] = (int) o.get("s1");
            score[1] = (int) o.get("s2");
            tm[0] = findTeam((int) o.get("t1"), teams);
            tm[1] = findTeam((int) o.get("t2"), teams);

            int[] r1 = {(int) o.get("r1tour"), (int) o.get("r1team")};
            int[] r2 = {(int) o.get("r2tour"), (int) o.get("r2team")};
            recs[0] = findRecord(r1, records);
            recs[1] = findRecord(r2, records);

            Game game = new Game((int) o.get("id"), score, tm, recs);
            games.add(game);
        }

        return games;
    }

    /** Converts string into a int array
     * 
     * NOTE: Logic From Stack Overflow (Link Below)
     * https://stackoverflow.com/questions/7646392/convert-string-to-int-array-in-java
     */
    private int[] stringToIntA(String s) {
        String[] items = s.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

        int[] results = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
            }
        }
        
        return results;
    }

    // Converts Int array to String for storage
    private String intAtoString(int[] a){
        String s = "[";
        for (int i = 0; i < a.length; i++) {
            s = s + a[i];

            if (i + 1 != a.length) s = s + ",";
        }
        s = s + "]";
        return s;
    }

    // Generates ID's
    public static int newID() {
        int max = 99999;
        int min = 10000;

        // Generate a unique random key
        // Too late to implement, but this would benefit from using an itorator
        // Check uniquness in a different method for now
        return 1000 + (int)(Math.random() * ((max - min) + 1));
    }


    // Find Functions Used by multiple classes
    public static Team findTeam(int ID, ArrayList<Team> teams) {
        for (Team team: teams) {
            if (team.getID() == ID) return team;
        }
        return null;
    }

    public static User findUser(String ID, ArrayList<User> users) {
        for (User user: users) {
            if (user.getUsername().equals(ID)) return user;
        }
        return null;
    }

    public static Record findRecord(int[] ID, ArrayList<Record> records) {
        for (Record record : records) {
            if (record.same(ID)) return record;
        }
        return null;
    }
    

    // Getters and Setters
    // NOTE: File page addresses are not needed for anything else
    public ArrayList<Team> getTeams() {
        return teams;
    }
    public ArrayList<Tournament> getTournaments() {
        return tournaments;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<Record> getRecords() {
        return records;
    }
    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }
    public void setTournaments(ArrayList<Tournament> tournaments) {
        this.tournaments = tournaments;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }
}

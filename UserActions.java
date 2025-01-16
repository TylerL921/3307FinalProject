package JavaBeta;

/* ActionHandler
 * Deals with all actions for Users. Facade design pattern.
 * This class is designed so that future additions to the users can use these funcitons
 */

// Imports
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class UserActions {
    public static boolean logoutFlag = false;
    private static Scanner scanner = new Scanner(System.in);

    // Constructor
    private UserActions(){}

    // Handles all User Actions
    public static void prompt(User user, DataManager dm){
        // Variables
        String option, option1;
        int ID1, ID2;
        Team team;
        Player player;
        Tournament tournament;
        ArrayList<Tournament> tournaments;
        ArrayList<Team> teams;
        ArrayList<Player> players;
        Queue<Game> games;
        
        // Reset Logout Flag
        logoutFlag = false;

        while (!logoutFlag) {
            tournaments = null;
            teams = null;
            players = null;

            System.out.println("\n\nAction Menu:");

            // Prompt for universal actions
            System.out.println("Enter \"Logout\" to logout.");
            System.out.println("Enter \"Change\" to change password.");

            // Prompt for user specific actions
            if (user instanceof Admin) { // Admin Actions
                tournaments = ((Admin) user).getTournaments();

                System.out.println("Enter \"Schedule\" to schedule games.");
                System.out.println("Enter \"Report\" to report games.");
                System.out.println("Enter \"End\" to end a tournament.");
                System.out.println("Enter \"New\" to create a new tournament");
                System.out.println("Enter \"List\" to view all your active tournaments.");

            } else if (user instanceof Manager) { // Manager Actions
                teams = ((Manager) user).getTeams();

                System.out.println("Enter \"Create\" to create a new team");
                System.out.println("Enter \"Register\" to register team into a tournament.");
                System.out.println("Enter \"Remove\" to remove a player from a team.");
                System.out.println("Enter \"Add\" to add a player to a team.");
                System.out.println("Enter \"Disband\" to disband a team.");
                System.out.println("Enter \"Accolades\" to display a teams accolades.");
                System.out.println("Enter \"View\" to view all your active teams,");

            } else { // Player Actions
                System.out.println("Enter \"Appear\" to show your tournament appearences");

            }

            System.out.print("\n\nWhat would you like to do?: ");
            option = scanner.next();

            // Make sting full lowercase
            option = option.toLowerCase();

            switch (option) {
                // Universal Actions
                case "logout": // Log out
                    logoutFlag = true;
                    break;

                case "change": // Change password
                    System.out.print("\n\nEnter old password: ");
                    option = scanner.next();

                    System.out.print("\nEnter new password: ");
                    option1 = scanner.next();

                    boolean worked = PasswordManager.changePassword(option, option1, user);
                    if (worked) System.out.println("\nPassword successfully changed");
                    else System.out.println("\nERROR. PASSWORD REMAINS THE SAME");

                    break;
            
                // Admin Actions
                case "schedule": // Schedule Games
                    System.out.print("\n\nEnter tournament ID: ");
                    ID1 = scanner.nextInt();

                    tournament = findTournament(ID1, tournaments);
                    if (tournament == null) {
                        System.out.println("\nINVALID TOURNAMENT ID");
                        break;
                    }

                    games = scheduleGames(findTournament(ID1, tournaments));
                    displayGames(games);
                    break;

                case "end": // End Tournament
                    System.out.print("\n\nEnter tournament ID: ");
                    ID1 = scanner.nextInt();

                    boolean end = endTournament(findTournament(ID1, tournaments), (Admin) user);
                    if (!end) System.out.println("\nERROR tournament is not complete or tournament does not exist");
                    break;
                
                case "new": // Create new tournament
                    System.out.println("\n\nSingle Elim = 0, Double Elim = 1, Swiss = 2, Round Robin = 3");
                    System.out.print("\nWhat kind of tournament do you want to create: ");
                    ID1 = scanner.nextInt();

                    tournament = TournamentFactory.makeTournament((Admin) user, ID1, dm.getTournaments(), scanner);
                    dm.getTournaments().add(tournament);
                    tournaments.add(tournament);

                    System.out.println("\nSuccessfully Created Tournament.");
                    System.out.println("\nTournaments ID: " + tournament.gettID());

                    break;

                case "report": // Report scores
                    System.out.print("\n\nEnter tournament ID: ");
                    ID1 = scanner.nextInt();
                    tournament = findTournament(ID1, tournaments);

                    if (tournament == null) {
                        System.out.println("\nERROR. INVALID TOURNAMENT ID");
                        break;
                    }

                    games = tournament.getGames();
                    reportScores(games);
                    break;

                case "list":
                    System.out.println("");
                    for (Tournament t : tournaments) {
                        System.out.println(t.getTitle() + " ID: " + t.gettID());
                    }

                    break;

                // Manager Actions
                case "view": // List all teams
                    // NOTE: View and list are identical exect one is teams and the other is tournaments. If I had more time I would use the itorator pattern to reduce duplicate code
                    System.out.println("");
                    for (Team t : teams) {
                        System.out.println(t.getName() + " ID: " + t.getID());
                    }

                    break;

                case "create": // Create a new team
                    System.out.print("\n\nWhat is this teams name?: ");
                    option = scanner.next();

                    team = new Team(option, user.getUsername(), newTeamID(teams));
                    teams.add(team);

                    System.out.println("\nSuccessfully made team " + team.getName() + " ID: " + team.getID());
                    break;

                case "register": // Register for tournament
                    tournaments = dm.getTournaments();

                    System.out.print("\n\nEnter tournament ID: ");
                    ID1 = scanner.nextInt();

                    System.out.print("\nEnter team ID: ");
                    ID2 = scanner.nextInt();

                    tournament = findTournament(ID1, tournaments);
                    team = DataManager.findTeam(ID2, teams);

                    if (tournament == null) { // Tournament not found
                        System.out.println("\nINVALID TOURNAMENT ID");
                    } else if (tournament.getStarted()) { // Tournament allready started
                        System.out.println("\nTournament has allready started. Cannot join");
                    } else if (team == null) { // Team not found
                        System.out.println("\nTEAM DOES NOT EXIST");
                    } else { // Attempt to register team
                        // Get whitelist password if needed
                        option = "";
                        if(!tournament.getWhitelist().equals("")) {
                            System.out.print("\nEnter Password: ");
                            option = scanner.next();
                        }

                        // Register Team
                        if (!tournament.addTeam(team, option)) {
                            System.out.println("\nAN ERROR OCCURED REGISTERING FOR TOURNAMENT");
                            System.out.println("Either Tournament is full or Incorrect Password");
                        } else {
                            System.out.println("\nSuccessfully Registered Team!");
                        }

                    }

                    break;
                case "remove":
                    System.out.print("\n\nEnter team ID: ");
                    ID1 = scanner.nextInt();

                    System.out.print("\nEnter player username to remove: ");
                    option = scanner.next();

                    // Find Team
                    team = DataManager.findTeam(ID1, teams);

                    if (team == null) { // Team not found
                        System.out.println("\nTEAM DOES NOT EXIST");

                    } else { // Attempt to Remove Player
                        boolean f = false;

                        // Check if player is in an active tournament
                        for (Tournament t : team.getTourIn()) {
                            if (!t.getStatus()) f = true;
                            break;
                        }

                        if (f) System.out.println("\nCANNOT REMOVE PLAYER IN ACTIVE TOURNAMENT");
                        else {
                            boolean r = team.removePlayer(option);
                            if (r) System.out.println("\nSuccessfully removed player");
                            else System.out.println("\nPlayer was not appart of team");
                        }                        
                    }
                    break;

                case "add":
                    System.out.print("\n\nEnter team ID: ");
                    ID1 = scanner.nextInt();

                    System.out.print("\nEnter player username to add: ");
                    option = scanner.next();

                    // Find Team
                    team = DataManager.findTeam(ID1, teams);
                    players = onlyPlayers(dm.getUsers());
                    player = findPlayer(option, players);

                    if (team == null) { // Team not found
                        System.out.println("\nTEAM DOES NOT EXIST");

                    } else if (player == null){
                        System.out.println("\nPLAYER DOES NOT EXIST");

                    }else { // Attempt to Add Player
                        boolean f = false;

                        // Check if team is in an active tournament
                        for (Tournament t : team.getTourIn()) {
                            if (!t.getStatus()) f = true;
                            break;
                        }

                        if (f) System.out.println("\nCANNOT ADD PLAYER IN ACTIVE TOURNAMENT");
                        else {
                            boolean r = team.addPlayer(player);
                            if (r) System.out.println("\nSuccessfully added player");
                            else System.out.println("\nAN ERROR OCCURED");
                        }                        
                    }
                    break;
                    
                case "disband":
                    System.out.print("Enter team ID: ");
                    ID1 = scanner.nextInt();

                    // Find Team
                    team = DataManager.findTeam(ID1, teams);

                    if (team == null) { // Team not found
                        System.out.println("\nTEAM DOES NOT EXIST");
                    } else { // Attempt to disband team
                        boolean r = team.disbandTeam();
                        if (r) System.out.println("\nSuccessfully disbanded team");
                        else System.out.println("\nCannot disband team.");
                    }
                    break;

                case "accolades":
                    System.out.print("\n\nEnter team ID: ");
                    ID1 = scanner.nextInt();

                    // Find Team
                    team = DataManager.findTeam(ID1, teams);

                    if (team == null) { // Team not found
                        System.out.println("\nTEAM DOES NOT EXIST");
                    } else { // Display Team Accolades
                        option = team.getName();
                        System.out.println("\n" + showRecords(option, team.getAppear()));;
                    }
                    break;

                // Player Actions
                case "appear":
                    player = (Player) user;
                    System.out.println(player.toString());
                    break;

            }
        }
    }
    
    // Action Functions

    // Schedule Games (For Admins)
    private static Queue<Game> scheduleGames(Tournament tournament){
        tournament.makeGames();
        return tournament.getGames();
    }

    // End Tournament (For Admins)
    private static boolean endTournament(Tournament tournament, Admin admin){
        if (tournament == null) return false;
        tournament.updateStatus();
        if (!tournament.getStatus()) return false;

        // Remove tournament from admin list
        admin.removeTournament(tournament);

        // Remove tournament from all teams active tournaments list
        ArrayList<Team> teams = tournament.getTeams();
        for(Team t : teams) {
            t.removeTournament(tournament);
        }

        return true;
    }

    // Report Scores (For Admins)
    private static void reportScores(Queue<Game> games){
        boolean quit = false;

        while (!games.isEmpty() && !quit) {
            Game game = games.peek();
            String option;
            int s1, s2;

            System.out.println("Current game: " + game.toString());
            System.out.print("Would you like to report this game (y/n/q to quit): ");
            option = scanner.next();
            option = option.toLowerCase();

            if (option.equals("y")){
                while (true) {
                    System.out.print("\nEnter the score of " + game.getTeams()[0]);
                    s1 = scanner.nextInt();

                    System.out.print("\nEnter the score of " + game.getTeams()[1]);
                    s2 = scanner.nextInt();

                    int[] score = {s1, s2};
                    game.setScore(score);

                    System.out.println("\n" + game.finalScore() + "\n");
                    System.out.print("Is this correct? (y/n): ");
                    option = scanner.next();
                    option.toLowerCase();
                    
                    if (option.equals("y")) {
                        game.winner();
                        games.remove();
                        break;
                    }
                }

            } else if(option.equals("n")){
                System.out.println("\n");
                games.remove();
                games.add(game);
            } 
            else quit = true;
        }

    }

    // Display Games
    private static void displayGames(Queue<Game> games){
        for (Game g : games){
            System.out.println(g.toString());
        }
    }

    // Display Records
    private static String showRecords(String name, ArrayList<Record> records){
        String s = name + " Accolaides :\n";

        // Display standings
        for (Record r : records) {
            s = s + r.toString() + "\n";
        }
        return s;
    }

    // Helper Functions

    // Sort out only the players from users
    private static ArrayList<Player> onlyPlayers(ArrayList<User> users) {
        ArrayList<Player> players = new ArrayList<Player>();
        
        for(User u : users){
            if (u instanceof Player) {
                players.add((Player) u);
            }
        }
        return players;
    }

    // Find tournament
    public static Tournament findTournament(int tID, ArrayList<Tournament> tournaments) {
        for (Tournament t : tournaments){
            if (t.gettID() == tID) return t;
        }
        return null;
    }

    // Searching for a team
    public static Player findPlayer(String username, ArrayList<Player> players) {
        for(Player player: players) {
            if (player.getUsername().equals(username)) return player;
        }
        return null;
    }

    private static int newTeamID(ArrayList<Team> teams) {
        int ID = 0;
        boolean flag;

        // Generate a unique random key
        while (true) {
            flag = false;
            ID = DataManager.newID();
            
            // Check uniqueness
            for (Team t: teams) {
                if (t.getID() == ID) flag = true;
            }

            // Is unique
            if (!flag) break;
        }
        return ID;
    }

}

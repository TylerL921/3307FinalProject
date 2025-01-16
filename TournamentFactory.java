package JavaBeta;

import java.util.ArrayList;
import java.util.Scanner;

public class TournamentFactory {

    private TournamentFactory(){}

    public static Tournament makeTournament(Admin admin, int option, ArrayList<Tournament> tournaments, Scanner scanner) {
        int nTeams, rounds;
        String title, prize, Schoice;
        String whitelist = "";
        boolean Bchoice;
        Tournament tournament = null;

        System.out.print("\nEnter Tournament Name: ");
        title = scanner.next();

        System.out.print("\nDescibe what the winner earns: ");
        prize = scanner.next();

        System.out.print("\nEnter the max number of teams in the tournament: ");
        nTeams = scanner.nextInt();

        while (whitelist.length() <= 5) {
            System.out.print("\nEnter whitelist password (\"N\" for no password needed): ");
            whitelist = scanner.next();

            if (whitelist.equals("n") || whitelist.equals("N")) {
                whitelist = "";
                break;
            }

            if (whitelist.length() <= 5) System.out.println("\n\nPASSWORD MUST BE GREATER THAN 5 CHARACTERS LONG");
        }

        // Prompt for tournament specific items then create tournament
        switch (option) {
            case 0: // Single Elim
                // NOTE: Seeding is not implemented. Future applications should allow admins to change the seeding.
                // Currently the first manager to sign up is 1st seed, so on so forth
                System.out.print("\nIs this tournament seeded? (y or n): ");
                Schoice = scanner.next();
                Schoice.toLowerCase();
                if (Schoice.equals("y")) Bchoice = true;
                else Bchoice = false;

                // Generate Single Elim Tournament
                tournament = new SingleElim(newTID(tournaments), nTeams, title, whitelist, admin, prize, Bchoice);
        
                break;

            case 1: // Double Elim
                // NOTE: Same as single elim. seeding is not fully implemented
                // NOTE: Can get bad inputs. Defaults to no
                System.out.print("\nIs this tournament seeded? (y or n): ");
                Schoice = scanner.next();
                Schoice.toLowerCase();
                if (Schoice.equals("y")) Bchoice = true;
                else Bchoice = false;

                // Generate Single Elim Tournament
                tournament = new DoubleElim(newTID(tournaments), nTeams, title, whitelist, admin, prize, Bchoice);
        
                break;

            case 2: // Swiss
                System.out.print("\nDoes this tournament have a max number of rounds (y or n): ");
                Schoice = scanner.next();
                Schoice.toLowerCase();

                // Can get bad inputs. Defaults to no
                if (Schoice.equals("y")) {
                    System.out.print("\nEnter the max number of rounds: ");
                    rounds = scanner.nextInt();
                    tournament = new Swiss(newTID(tournaments), nTeams, title, whitelist, admin, prize, rounds);

                } else {
                    tournament = new Swiss(newTID(tournaments), nTeams, title, whitelist, admin, prize);
                }

                break;

            case 3: // Round Robin
                tournament = new RoundRobin(newTID(tournaments), nTeams, title, whitelist, admin, prize);
                break;
        
        }
        return tournament;
    }

    private static int newTID(ArrayList<Tournament> tournaments) {
        int ID = 0;
        boolean flag;

        // Generate a unique random key
        while (true) {
            flag = false;
            ID = DataManager.newID();

            // Check uniqueness
            for (Tournament t: tournaments) {
                if (t.gettID() == ID) flag = true;
            }

            // Is unique
            if (!flag) break;
        }
        return ID;
    }
}

package JavaBeta;

// Imports
import java.util.Scanner;

/* Main Class.
 *  Startup for the program. Gets Inputs for password manager
 */

class Main {
    public static void main(String[] args) {
        User user;
        boolean exit = false;
        String masterString = "C:\\Users\\Tyler\\project-deliverable-2-tlin\\code\\JavaBeta\\MasterPage.txt"; //Change filepath to match
        String s1, s2, s3;
        int option;
        
        @SuppressWarnings("resource")
        Scanner s = new Scanner(System.in);
        DataManager dm = new DataManager(masterString);
        PasswordManager pm = new PasswordManager(dm.getUsers());

        while (!exit) {
            System.out.println("\n\nEnter \"Q\" to Exit.");
            System.out.println("Enter \"N\" to Create an account.");
            System.out.println("Enter \"L\" to login.");

            // Get Input
            System.out.print("What would you like to do?: ");
            s1 = s.next();
            s1.toLowerCase();

            switch (s1) {
                case "q": // Quit Program
                    exit = true;
                    break;

                case "l": // Log in
                    while (true){
                        System.out.print("\nEnter username (\"B\" to go back): ");
                        s1 = s.next();

                        if (s1.equals("b") || s1.equals("B")) break;

                        System.out.print("\nEnter password: ");
                        s2 = s.next();


                        user = pm.login(s1, s2);
                        if(user == null) System.out.println("\nError. Incorrect Username or Password.");
                        else UserActions.prompt(user, dm);
                        break;
                    }
                    break;

                case "n": // New User
                    while (true){
                        System.out.print("\nEnter username (\"B\" to go back): ");
                        s1 = s.next();

                        if (s1.equals("b") || s1.equals("B")) break;

                        System.out.print("\nEnter password: ");
                        s2 = s.next();

                        System.out.print("\nWhat is your name?: ");
                        s3 = s.next();

                        System.out.println("\n\nPlayer = 0, Manager = 1, Admin = 2");
                        System.out.print("\nEnter the number of the account you want to make: ");
                        option = s.nextInt();

                        if (option > 2 || option < 0) {
                            System.out.println("\n INVALID INPUT \n");
                        } else if (!pm.newUser(s1, s2, s3, option)) {
                            System.out.println("\n User all ready exits. Please select a new username. \n");
                        } else {
                            System.out.println("\n" + s1 + " Has been successfully created");
                            break;
                        }
                    }
                    break;
            
                default: // Invalid Input
                    System.out.println(" \nInvalid Input.\n");
                    break;
            }

            // Save data
            dm.save();
        }

    }

}

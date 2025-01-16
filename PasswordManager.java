package JavaBeta;

/* PasswordManager
 * This class handles logging in/out and account creation.
 */

// Imports
import java.util.ArrayList;

public class PasswordManager {
    // Variables
    private ArrayList<User> users;

    // Constuctor
    public PasswordManager(ArrayList<User> p) {
        users = p;
    }

    // Methods
    public User login(String u, String p){
        User user = DataManager.findUser(u, users);
        if (user == null) return null;
        if (user.getPassword().equals(p)) return user;
        return null;
    }

    // Creates a new user
    public Boolean newUser(String u, String p, String n, int o) {
        // Check if user exists
        if (DataManager.findUser(u, users) != null) return false;

        // Create user and add to the list of users
        User user = null;
        switch (o) {
            case 0: // Create Player
                user = new Player(u, p, n);
                break;

            case 1: // Create Manager
                user = new Manager(u, p, n);
                break;
            
            case 2: // Create Admin
                user = new Admin(u, p, n);
                break;

            default:
                return false;
        }
        users.add(user);
        return true;
    }

    // Change a users password
    public static boolean changePassword(String oldP, String newP, User user) {
        
        if (user.getPassword().equals(oldP)) {
            user.setPassord(newP);
            return true;
        } else return false;
    }

    // Getters and Setters
    public ArrayList<User> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
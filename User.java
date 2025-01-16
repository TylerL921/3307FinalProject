package JavaBeta;

/* User
 * Generic user class. All users have username, password, and a name
 */

public abstract class User {
    // Variables
    private String username, password, name;

    // Constructor
    public User(String u, String p, String n){
        username = u;
        password = p;
        name = n;
    }

    // Funcitons
    public Boolean login(String p) {
        if (password == p) return true; // Password must match to login to be permitted
        else return false;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getName(){
        return name;
    }

    public void setUsername(String u){
        username = u;
    }

    public void setPassord(String p){
        password = p;
    }

    public void setName(String n) {
        name = n;
    }
}

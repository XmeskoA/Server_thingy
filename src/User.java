public class User {
    private int ID;
    private String username;
    private String password;
    private String email;
    private int godmode;

    //Constructor
    public User(int ID,String username, String password, String email, int godmode) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.godmode = godmode;
    }

    public void addBook () {

    }

    //Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void getEmail(String newEmail) {
        this.email = newEmail;
    }

    public void getID(int newID) {
        this.ID = newID;
    }

    public void getGodmode (int newGodmode) {
        this.godmode = newGodmode;
    }

    //Setters
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setID(int newID) {
        this.ID = newID;
    }

    public void setGodmode (int newGodmode) {
        this.godmode= newGodmode;
    }
}
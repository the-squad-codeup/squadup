package pro.squadup.models;

// Used to pass simple user information for signup validation
public class UserSlim {

    private String username;
    private String email;

    public UserSlim() {
    }

    public UserSlim(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private  String username;

    @Column(nullable = false, length = 75, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preferences_id")
    private Preferences preferences;

    @OneToMany(mappedBy = "userOne")
    private Set<Recruit> recruits;

    @OneToMany(mappedBy = "userTwo")
    @JsonIgnore
    private Set<Recruit> recruitsMatchedWithUser;

    @OneToMany(mappedBy = "userOne")
    private Set<Recruit> comrades;

    @OneToMany(mappedBy = "userTwo")
    @JsonIgnore
    private Set<Recruit> comradesOfUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public Set<Recruit> getRecruits() {
        return recruits;
    }

    public void setRecruits(Set<Recruit> recruits) {
        this.recruits = recruits;
    }

    public Set<Recruit> getRecruitsMatchedWithUser() {
        return recruitsMatchedWithUser;
    }

    public void setRecruitsMatchedWithUser(Set<Recruit> recruitsMatchedWithUser) {
        this.recruitsMatchedWithUser = recruitsMatchedWithUser;
    }

    public Set<Recruit> getComrades() {
        return comrades;
    }

    public void setComrades(Set<Recruit> comrades) {
        this.comrades = comrades;
    }

    public Set<Recruit> getComradesOfUser() {
        return comradesOfUser;
    }

    public void setComradesOfUser(Set<Recruit> comradesOfUser) {
        this.comradesOfUser = comradesOfUser;
    }

    public User() {
    }

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User (User copy) {
        this.id = copy.id;
        this.username = copy.username;
        this.email = copy.email;
        this.password = copy.password;
    }

}

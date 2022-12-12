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
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToMany
    @JoinTable(
            name = "recruits",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "recruit_id")}
    )
    private Set<User> matchedRecruits;

    @ManyToMany(mappedBy = "matchedRecruits")
    @JsonIgnore
    private Set<User> recruitsMatchedWithUser;

    @ManyToMany
    @JoinTable(
            name = "comrades",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "comrade_id")}
    )
    private Set<User> userComrades;

    @ManyToMany(mappedBy = "userComrades")
    @JsonIgnore
    private Set<User> comradesOfUser;

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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<User> getMatchedRecruits() {
        return matchedRecruits;
    }

    public void setMatchedRecruits(Set<User> matchedRecruits) {
        this.matchedRecruits = matchedRecruits;
    }

    public Set<User> getRecruitsMatchedWithUser() {
        return recruitsMatchedWithUser;
    }

    public void setRecruitsMatchedWithUser(Set<User> recruitsMatchedWithUser) {
        this.recruitsMatchedWithUser = recruitsMatchedWithUser;
    }

    public Set<User> getUserComrades() {
        return userComrades;
    }

    public void setUserComrades(Set<User> userComrades) {
        this.userComrades = userComrades;
    }

    public Set<User> getComradesOfUser() {
        return comradesOfUser;
    }

    public void setComradesOfUser(Set<User> comradesOfUser) {
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

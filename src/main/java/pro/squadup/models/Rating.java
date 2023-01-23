package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

// Rating object, saved in database
@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int igdbId;

    @Column(nullable = false)
    private String rating;

    @OneToMany(mappedBy = "rating")
    @JsonIgnore
    private Set<Preferences> preferences;

    @OneToMany(mappedBy = "rating")
    @JsonIgnore
    private Set<Game> games;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIgdbId() {
        return igdbId;
    }

    public void setIgdbId(int igdbId) {
        this.igdbId = igdbId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Set<Preferences> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Preferences> preferences) {
        this.preferences = preferences;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Rating() {
    }

    public Rating(int igdbId) {
        this.igdbId = igdbId;
    }

    public Rating(int igdbId, String rating, Set<Preferences> preferences) {
        this.igdbId = igdbId;
        this.rating = rating;
        this.preferences = preferences;
    }

    public Rating(Long id, String rating, Set<Preferences> preferences) {
        this.id = id;
        this.rating = rating;
        this.preferences = preferences;
    }

}

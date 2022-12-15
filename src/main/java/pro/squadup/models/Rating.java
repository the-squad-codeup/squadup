package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    public String rating;

    @OneToMany(mappedBy = "game_age_rating")
    @JsonIgnore
    private Set<Preferences> preferences;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Rating() {
    }

    public Rating(String rating, Set<Preferences> preferences) {
        this.rating = rating;
        this.preferences = preferences;
    }

    public Rating(Long id, String rating, Set<Preferences> preferences) {
        this.id = id;
        this.rating = rating;
        this.preferences = preferences;
    }

}

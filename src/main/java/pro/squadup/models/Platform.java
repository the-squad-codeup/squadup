package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="platforms")
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "platformId")
    private Set<PlatformMapping> igdbId;

    @Column(nullable = false, length = 25)
    private String type;

    @ManyToMany(mappedBy = "platforms")
    @JsonIgnore
    private Set<Preferences> preferences;

    @ManyToMany(mappedBy = "platforms")
    @JsonIgnore
    private Set<Game> games;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Set<PlatformMapping> getIgdbId() {
        return igdbId;
    }

    public void setIgdbId(Set<PlatformMapping> igdbId) {
        this.igdbId = igdbId;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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

    public Platform(){

    }

    public Platform(String type) {
        this.type = type;
    }

    public Platform(Long id, String type) {
        this.id = id;
        this.type = type;
    }
}

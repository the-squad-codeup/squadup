package pro.squadup.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="games")
public class Game {


    //* to be created when those specific models are created NOTE ** update constructors at this time
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long igbdId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String artwork;

    @Column(length = 25)
    private String rating;

    private Set<Preferences> preferences;

    private Set<Genre> genres;

    private Set<Platform> platforms;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getIgbdId() {
        return igbdId;
    }

    public void setIgbdId(Long igbdId) {
        this.igbdId = igbdId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtwork() {
        return artwork;
    }
    public void setArtwork(String artwork) {
        this.artwork = artwork;
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

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public Game(){
    }

    public Game(String title, String artwork, String rating) {
        this.title = title;
        this.artwork = artwork;
        this.rating = rating;
    }

    public Game(long id, String title, String artwork, String rating) {
        this.id = id;
        this.title = title;
        this.artwork = artwork;
        this.rating = rating;
    }
}

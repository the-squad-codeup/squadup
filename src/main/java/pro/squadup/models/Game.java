package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToMany(mappedBy = "games")
    @JsonIgnore
    private Set<Profile> profiles;

    @ManyToMany
    @JoinTable(
            name = "game_genre",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "game_platform",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "platform_id")}
    )
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

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
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

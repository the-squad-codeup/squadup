package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pro.squadup.utils.GameDeserializer;

import javax.persistence.*;
import java.util.Set;

// Game object, saved in database
@Entity
@Table(name="games")
@JsonDeserialize(using = GameDeserializer.class)
public class Game {


    //* to be created when those specific models are created NOTE ** update constructors at this time
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long igdbId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String artwork;

    @Column
    private int year;

    @ManyToOne
    @JoinColumn(name = "rating_id")
    private Rating rating;

    @ManyToMany(mappedBy = "games")
    @JsonIgnore
    private Set<Preferences> preferences;

    @OneToMany(mappedBy = "favoriteGame")
    @JsonIgnore
    private Set<Preferences> favorites;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "game_genre",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER)
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

    public Long getIgdbId() {
        return igdbId;
    }

    public void setIgdbId(Long igdbId) {
        this.igdbId = igdbId;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Set<Preferences> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Preferences> preferences) {
        this.preferences = preferences;
    }

    public Set<Preferences> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Preferences> favorites) {
        this.favorites = favorites;
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

    public Game(String title, String artwork, int year, Rating rating) {
        this.title = title;
        this.artwork = artwork;
        this.year = year;
        this.rating = rating;
    }

    public Game(long id, String title, String artwork, int year, Rating rating) {
        this.id = id;
        this.title = title;
        this.artwork = artwork;
        this.year = year;
        this.rating = rating;
    }

    public Game(Long igdbId, String title, String artwork, int year, Rating rating, Set<Genre> genres, Set<Platform> platforms) {
        this.igdbId = igdbId;
        this.title = title;
        this.artwork = artwork;
        this.year = year;
        this.rating = rating;
        this.genres = genres;
        this.platforms = platforms;
    }
}

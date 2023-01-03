package pro.squadup.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "preferences")
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false)
    private String gamertag;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "preferences_language",
            joinColumns = {@JoinColumn(name = "preferences_id")},
            inverseJoinColumns = {@JoinColumn(name = "language_id")}
    )
    private Set<Language> languages;

    @Column(nullable = false)
    private boolean matureLanguage;

    @ManyToOne
    @JoinColumn(name = "rating_id")
    private Rating rating;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "preferences")
    @JsonIgnore
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "preferences_platform",
            joinColumns = {@JoinColumn(name = "preferences_id")},
            inverseJoinColumns = {@JoinColumn(name = "platform_id")}
    )
    private Set<Platform> platforms;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "preferences_game",
            joinColumns = {@JoinColumn(name = "preferences_id")},
            inverseJoinColumns = {@JoinColumn(name = "game_id")}
    )
    private Set<Game> games;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "preferences_genre",
            joinColumns = {@JoinColumn(name = "preferences_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<Genre> genres;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGamertag() {
        return gamertag;
    }

    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isMatureLanguage() {
        return matureLanguage;
    }

    public void setMatureLanguage(boolean matureLanguage) {
        this.matureLanguage = matureLanguage;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Preferences() {
        this.bio = "";
        this.gamertag = "";
//        this.location = "";
//        this.language = "";
        this.matureLanguage = false;
//        this.game_age_rating = "";
    }

    public Preferences(String bio, String gamertag, Location location, Set<Language> languages, boolean matureLanguage, Rating rating) {
        this.bio = bio;
        this.gamertag = gamertag;
        this.location = location;
        this.languages = languages;
        this.matureLanguage = matureLanguage;
        this.rating = rating;
    }

    public Preferences(Long id, Location location, Set<Language> languages, boolean matureLanguage, Rating rating) {
        this.id = id;
        this.location = location;
        this.languages = languages;
        this.matureLanguage = matureLanguage;
        this.rating = rating;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

}

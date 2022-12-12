package pro.squadup.models;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String mature_language;

    @Column(nullable = false)
    private String game_age_rating;

    @OneToOne(mappedBy = "profiles")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "profile_platform",
            joinColumns = {@JoinColumn(name = "profile_id")},
            inverseJoinColumns = {@JoinColumn(name = "platform_id")}
    )
    private Set<Platform> platforms;

    @ManyToMany
    @JoinTable(
            name = "profile_game",
            joinColumns = {@JoinColumn(name = "profile_id")},
            inverseJoinColumns = {@JoinColumn(name = "game_id")}
    )
    private Set<Game> games;

    @ManyToMany
    @JoinTable(
            name = "profile_genre",
            joinColumns = {@JoinColumn(name = "profile_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "profile_language",
            joinColumns = {@JoinColumn(name = "profile_id")},
            inverseJoinColumns = {@JoinColumn(name = "language_id")}
    )
    private Set<Language> languages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMature_language() {
        return mature_language;
    }

    public void setMature_language(String mature_language) {
        this.mature_language = mature_language;
    }

    public String getGame_age_rating() {
        return game_age_rating;
    }

    public void setGame_age_rating(String game_age_rating) {
        this.game_age_rating = game_age_rating;
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

    public Profile() {
    }

    public Profile(String location, String language, String mature_language, String game_age_rating) {
        this.location = location;
        this.language = language;
        this.mature_language = mature_language;
        this.game_age_rating = game_age_rating;
    }

    public Profile(Long id, String location, String language, String mature_language, String game_age_rating) {
        this.id = id;
        this.location = location;
        this.language = language;
        this.mature_language = mature_language;
        this.game_age_rating = game_age_rating;
    }

}

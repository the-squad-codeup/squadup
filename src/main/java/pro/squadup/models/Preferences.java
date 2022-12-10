package pro.squadup.models;


import javax.persistence.*;

@Entity
@Table(name = "preferences")
public class Preferences {

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

    public Preferences() {
    }

    public Preferences(String location, String language, String mature_language, String game_age_rating) {
        this.location = location;
        this.language = language;
        this.mature_language = mature_language;
        this.game_age_rating = game_age_rating;
    }

    public Preferences(Long id, String location, String language, String mature_language, String game_age_rating) {
        this.id = id;
        this.location = location;
        this.language = language;
        this.mature_language = mature_language;
        this.game_age_rating = game_age_rating;
    }

}

package pro.squadup.models;

import javax.persistence.*;

@Entity
@Table(name="games")
public class GameModel {

    //* to be created when those specific models are created NOTE ** update constructors at this time
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//*   private long igbdId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String artwork;

    @Column(length = 25)
    private String rating;

//*    private String genre;

//*    private String platform;

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

    public GameModel(){
    }

    public GameModel(String title, String artwork, String rating) {
        this.title = title;
        this.artwork = artwork;
        this.rating = rating;
    }

    public GameModel(long id, String title, String artwork, String rating) {
        this.id = id;
        this.title = title;
        this.artwork = artwork;
        this.rating = rating;
    }
}

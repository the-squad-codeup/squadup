package pro.squadup.models;

import javax.persistence.*;

@Entity
@Table(name="platforms")
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 25)
    private String type;

    //Relationship to games still need to be established

//    private List<Games> games;

    @Column
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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

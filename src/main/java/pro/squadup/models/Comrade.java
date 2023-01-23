package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

// Comrade object, saved in database
@Entity
@Table(name = "comrades")
public class Comrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Timestamp dateComraded;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userOne;

    @ManyToOne
    @JoinColumn(name = "comrade_id")
    private User userTwo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDateComraded() {
        return dateComraded;
    }

    public void setDateComraded(Timestamp dateComraded) {
        this.dateComraded = dateComraded;
    }


    @JsonIgnore
    public User getUserOne() {
        return userOne;
    }

    public void setUserOne(User userOne) {
        this.userOne = userOne;
    }

    public User getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(User userTwo) {
        this.userTwo = userTwo;
    }

    public Comrade() {
    }

    public Comrade(Timestamp dateComraded) {
        this.dateComraded = dateComraded;
    }

    public Comrade(Timestamp dateComraded, User userOne, User userTwo) {
        this.dateComraded = dateComraded;
        this.userOne = userOne;
        this.userTwo = userTwo;
    }


}

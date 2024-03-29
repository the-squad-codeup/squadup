package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

// Recruit object, saved in database
@Entity
@Table(name = "recruits")
public class Recruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Timestamp dateRecruited;

    @Column(nullable = false)
    private boolean isRejected;

    @Column(nullable = false)
    private boolean isAccepted;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userOne;

    @ManyToOne
    @JoinColumn(name = "recruit_id")
    private User userTwo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDateRecruited() {
        return dateRecruited;
    }

    public void setDateRecruited(Timestamp dateRecruited) {
        this.dateRecruited = dateRecruited;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
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

    public Recruit() {
    }

    public Recruit(Timestamp dateRecruited, boolean isRejected) {
        this.dateRecruited = dateRecruited;
        this.isRejected = isRejected;
    }
}

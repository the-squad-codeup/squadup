package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

// LastSeenMessage object, saved in database
@Entity
@Table(name = "last_seen_messages")
public class LastSeenMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "squad_id")
    private Squad squad;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private SquadChatMessage squadChatMessage;

    public LastSeenMessage() {
    }

    public LastSeenMessage(User user, Squad squad, SquadChatMessage squadChatMessage) {
        this.user = user;
        this.squad = squad;
        this.squadChatMessage = squadChatMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public SquadChatMessage getSquadChatMessage() {
        return squadChatMessage;
    }

    public void setSquadChatMessage(SquadChatMessage squadChatMessage) {
        this.squadChatMessage = squadChatMessage;
    }
}

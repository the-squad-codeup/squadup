package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

// Squad Chat object, saved in database
@Entity
@Table(name = "squad_chats")
public class SquadChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "chat")
    private Squad squad;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chat")
    private Set<SquadChatMessage> messages;

    public SquadChat() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    @JsonIgnore
    public Set<SquadChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<SquadChatMessage> messages) {
        this.messages = messages;
    }
}

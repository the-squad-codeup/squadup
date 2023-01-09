package pro.squadup.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "squad_chats")
public class SquadChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(mappedBy = "chat")
    private Squad squad;

    @OneToMany(mappedBy = "chat")
    private Set<SquadChatMessage> messages;

    public SquadChat() {

    }

    public SquadChat(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public Set<SquadChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<SquadChatMessage> messages) {
        this.messages = messages;
    }
}

package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

// Squad Chat Message object, saved in database
@Entity
@Table(name = "squad_chat_messages")
public class SquadChatMessage {

    public enum MessageType {
        CHAT, JOIN, LEAVE, EDIT, DELETE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private MessageType messageType;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    private boolean edited;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private SquadChat chat;

    @OneToMany(mappedBy = "squadChatMessage")
    private Set<LastSeenMessage> lastSeenMessages;

    public SquadChatMessage() {

    }

    public SquadChatMessage(String content, Timestamp timestamp, boolean edited) {
        this.content = content;
        this.timestamp = timestamp;
        this.edited = edited;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public SquadChat getChat() {
        return chat;
    }

    public void setChat(SquadChat chat) {
        this.chat = chat;
    }

    @JsonIgnore
    public Set<LastSeenMessage> getLastSeenMessages() {
        return lastSeenMessages;
    }

    public void setLastSeenMessages(Set<LastSeenMessage> lastSeenMessages) {
        this.lastSeenMessages = lastSeenMessages;
    }
}

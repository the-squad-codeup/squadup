package pro.squadup.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "squads")
public class Squad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "squad_chat_id")
    private SquadChat chat;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "squad_member",
            joinColumns = {@JoinColumn(name = "squad_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id")}
    )
    private Set<User> members;

    public Squad() {

    }

    public Squad(String name) {
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

    public SquadChat getChat() {
        return chat;
    }

    public void setChat(SquadChat chat) {
        this.chat = chat;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }
}

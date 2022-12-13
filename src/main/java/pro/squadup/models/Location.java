package pro.squadup.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    public String timezone;

    @OneToMany(mappedBy = "location")
    private Set<Profile> profiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public Location() {
    }

    public Location(String timezone, Set<Profile> profiles) {
        this.timezone = timezone;
        this.profiles = profiles;
    }

    public Location(Long id, String timezone, Set<Profile> profiles) {
        this.id = id;
        this.timezone = timezone;
        this.profiles = profiles;
    }

}

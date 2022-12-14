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
    private Set<Preferences> preferences;

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

    public Set<Preferences> getPreferencess() {
        return preferences;
    }

    public void setPreferencess(Set<Preferences> preferences) {
        this.preferences = preferences;
    }

    public Location() {
    }

    public Location(String timezone, Set<Preferences> preferences) {
        this.timezone = timezone;
        this.preferences = preferences;
    }

    public Location(Long id, String timezone, Set<Preferences> preferences) {
        this.id = id;
        this.timezone = timezone;
        this.preferences = preferences;
    }

}

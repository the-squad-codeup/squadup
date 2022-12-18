package pro.squadup.models;

import javax.persistence.*;

@Entity
@Table(name = "platform_mapping")
public class PlatformMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long igdbId;

    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platform platformId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIgdbId() {
        return igdbId;
    }

    public void setIgdbId(Long igdbId) {
        this.igdbId = igdbId;
    }

    public Platform getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Platform platformId) {
        this.platformId = platformId;
    }

    public PlatformMapping() {
    }

    public PlatformMapping(Long igdbId) {
        this.igdbId = igdbId;
    }
}

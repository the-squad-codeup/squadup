package pro.squadup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "profile_pictures")
public class ProfilePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String handle;

    @Column(nullable = false)
    private String mimetype;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String uploadId;

    @Column(nullable = false)
    private String url;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ProfilePicture() {
    }

    public ProfilePicture(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public ProfilePicture(String filename, String handle, String mimetype, long size, String source, String status, String uploadId, String url) {
        this.filename = filename;
        this.handle = handle;
        this.mimetype = mimetype;
        this.size = size;
        this.source = source;
        this.status = status;
        this.uploadId = uploadId;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package pro.squadup.models;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

// Stray object, saved in database
// Used to store Contact Us form information
@Entity
@Table(name="strays")
public class Stray {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String fname;

    @Column
    private String lname;

    @Column
    @NotNull
    private String email;

    @Column
    private Long phoneNumber;

    @Column
    @NotNull
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Stray(){

    }

    public Stray(String fname, String lname, String email, Long phoneNumber, String body) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.body = body;
    }
}

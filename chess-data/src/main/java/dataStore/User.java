package dataStore;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(
                name = "getUserByLogin",
                query = "SELECT DISTINCT u FROM User u " +
                        "WHERE u.login = :login"
        ),
        @NamedQuery(
                name = "getUserListByPointsDescOrder",
                query = "SELECT DISTINCT u FROM User u " +
                        "ORDER BY u.points DESC"
        )
})
@Entity
@Table(name = "users", schema = "public", catalog = "chess")
public class User {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String login;
    private String password;
    private Integer wins;
    private Integer loses;
    private Integer points;
    private Integer ties;
    private Boolean online;
    private Long lastactivity;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "firstname")
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "lastname")
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "wins")
    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    @Basic
    @Column(name = "loses")
    public Integer getLoses() {
        return loses;
    }

    public void setLoses(Integer loses) {
        this.loses = loses;
    }

    @Basic
    @Column(name = "points")
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Basic
    @Column(name = "ties")
    public Integer getTies() {
        return ties;
    }

    public void setTies(Integer ties) {
        this.ties = ties;
    }

    @Basic
    @Column(name = "online")
    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    @Basic
    @Column(name = "lastactivity")
    public Long getLastactivity() {
        return lastactivity;
    }

    public void setLastactivity(Long lastactivity) {
        this.lastactivity = lastactivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User User = (User) o;

        if (id != User.id) return false;
        if (email != null ? !email.equals(User.email) : User.email != null) return false;
        if (firstname != null ? !firstname.equals(User.firstname) : User.firstname != null) return false;
        if (lastactivity != null ? !lastactivity.equals(User.lastactivity) : User.lastactivity != null) return false;
        if (lastname != null ? !lastname.equals(User.lastname) : User.lastname != null) return false;
        if (login != null ? !login.equals(User.login) : User.login != null) return false;
        if (loses != null ? !loses.equals(User.loses) : User.loses != null) return false;
        if (online != null ? !online.equals(User.online) : User.online != null) return false;
        if (password != null ? !password.equals(User.password) : User.password != null) return false;
        if (points != null ? !points.equals(User.points) : User.points != null) return false;
        if (ties != null ? !ties.equals(User.ties) : User.ties != null) return false;
        if (wins != null ? !wins.equals(User.wins) : User.wins != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (wins != null ? wins.hashCode() : 0);
        result = 31 * result + (loses != null ? loses.hashCode() : 0);
        result = 31 * result + (points != null ? points.hashCode() : 0);
        result = 31 * result + (ties != null ? ties.hashCode() : 0);
        result = 31 * result + (online != null ? online.hashCode() : 0);
        result = 31 * result + (lastactivity != null ? lastactivity.hashCode() : 0);
        return result;
    }
}

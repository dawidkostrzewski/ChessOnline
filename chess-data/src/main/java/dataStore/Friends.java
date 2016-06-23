package dataStore;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = "getFriendForUserById",
                query = "SELECT DISTINCT f FROM Friends f " +
                        "WHERE f.friendId = :userId " +
                        "OR f.userId = :userId"
        ),
        @NamedQuery(
                name = "getFriendByUserIdByFriendId",
                query = "SELECT DISTINCT f FROM Friends f " +
                        "WHERE (f.friendId = :friendId AND f.userId = :userId)" +
                        "OR (f.friendId = :userId OR f.userId = :friendId)"
        ),
        @NamedQuery(
                name = "checkIfCanAdd",
                query = "SELECT DISTINCT f FROM Friends f " +
                        "WHERE (f.friendId = :friendId " +
                        "AND f.userId = :userId) " +
                        "OR (f.friendId = :userId " +
                        "AND f.userId = :friendId)"
        )
})
@Entity
public class Friends {
    private int id;
    private Long userId;
    private Long friendId;
    private Boolean accepted;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "userId")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "friendId")
    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    @Basic
    @Column(name = "accepted")
    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friends friends = (Friends) o;

        if (id != friends.id) return false;
        if (accepted != null ? !accepted.equals(friends.accepted) : friends.accepted != null) return false;
        if (friendId != null ? !friendId.equals(friends.friendId) : friends.friendId != null) return false;
        if (userId != null ? !userId.equals(friends.userId) : friends.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (friendId != null ? friendId.hashCode() : 0);
        result = 31 * result + (accepted != null ? accepted.hashCode() : 0);
        return result;
    }
}

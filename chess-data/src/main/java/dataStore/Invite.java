package dataStore;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = "getInvitesByReciverIdOrderByTimeDesc",
                query = "SELECT DISTINCT i FROM Invite i " +
                        "WHERE i.reciverId = :userId " +
                        "ORDER BY i.creationtime DESC"
        )
})
@Entity
@Table(name = "invites", schema = "public", catalog = "chess")
public class Invite {
    private long id;
    private Long creationtime;
    private Long reciverId;
    private Long senderId;
    private Boolean playable;
    private Long gameId;

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
    @Column(name = "creationtime")
    public Long getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(Long creationtime) {
        this.creationtime = creationtime;
    }

    @Basic
    @Column(name = "reciver_id")
    public Long getReciverId() {
        return reciverId;
    }

    public void setReciverId(Long reciverId) {
        this.reciverId = reciverId;
    }

    @Basic
    @Column(name = "sender_id")
    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    @Basic
    @Column(name = "playable")
    public Boolean getPlayable() {
        return playable;
    }

    public void setPlayable(Boolean playable) {
        this.playable = playable;
    }

    @Basic
    @Column(name = "game_id")
    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invite invite = (Invite) o;

        if (id != invite.id) return false;
        if (creationtime != null ? !creationtime.equals(invite.creationtime) : invite.creationtime != null)
            return false;
        if (gameId != null ? !gameId.equals(invite.gameId) : invite.gameId != null) return false;
        if (playable != null ? !playable.equals(invite.playable) : invite.playable != null) return false;
        if (reciverId != null ? !reciverId.equals(invite.reciverId) : invite.reciverId != null) return false;
        if (senderId != null ? !senderId.equals(invite.senderId) : invite.senderId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (creationtime != null ? creationtime.hashCode() : 0);
        result = 31 * result + (reciverId != null ? reciverId.hashCode() : 0);
        result = 31 * result + (senderId != null ? senderId.hashCode() : 0);
        result = 31 * result + (playable != null ? playable.hashCode() : 0);
        result = 31 * result + (gameId != null ? gameId.hashCode() : 0);
        return result;
    }
}

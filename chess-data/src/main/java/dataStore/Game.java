package dataStore;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = "getGamesForUserByUserId",
                query = "SELECT DISTINCT g FROM Game g " +
                        "WHERE g.userId = :userId " +
                        "OR g.opponentId = :userId " +
                        "ORDER BY g.date"
        )
})
@Entity
@Table(name = "games", schema = "public", catalog = "chess")
public class Game {
    private long id;
    private String fen;
    private Long opponentId;
    private Long userId;
    private Long date;
    private Boolean finished;

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
    @Column(name = "fen")
    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    @Basic
    @Column(name = "opponent_id")
    public Long getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(Long opponentId) {
        this.opponentId = opponentId;
    }

    @Basic
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "date")
    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @Basic
    @Column(name = "finished")
    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (id != game.id) return false;
        if (date != null ? !date.equals(game.date) : game.date != null) return false;
        if (fen != null ? !fen.equals(game.fen) : game.fen != null) return false;
        if (finished != null ? !finished.equals(game.finished) : game.finished != null) return false;
        if (opponentId != null ? !opponentId.equals(game.opponentId) : game.opponentId != null) return false;
        if (userId != null ? !userId.equals(game.userId) : game.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (fen != null ? fen.hashCode() : 0);
        result = 31 * result + (opponentId != null ? opponentId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (finished != null ? finished.hashCode() : 0);
        return result;
    }
}

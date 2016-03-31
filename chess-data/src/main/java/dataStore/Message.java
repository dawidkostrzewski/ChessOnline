package dataStore;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = "getMessagesByReciverId",
                query = "SELECT DISTINCT m FROM Message m " +
                        "WHERE m.reciverId = :userId " +
                        "ORDER BY m.creationtime"
        )
})

@Entity
@Table(name = "messages", schema = "public", catalog = "chess")
public class Message {
    private long id;
    private String text;
    private Long senderId;
    private Long creationtime;
    private Long reciverId;
    private String type;

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
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != message.id) return false;
        if (creationtime != null ? !creationtime.equals(message.creationtime) : message.creationtime != null)
            return false;
        if (reciverId != null ? !reciverId.equals(message.reciverId) : message.reciverId != null) return false;
        if (senderId != null ? !senderId.equals(message.senderId) : message.senderId != null) return false;
        if (text != null ? !text.equals(message.text) : message.text != null) return false;
        if (type != null ? !type.equals(message.type) : message.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (senderId != null ? senderId.hashCode() : 0);
        result = 31 * result + (creationtime != null ? creationtime.hashCode() : 0);
        result = 31 * result + (reciverId != null ? reciverId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}

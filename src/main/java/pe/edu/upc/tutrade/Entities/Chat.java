package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="Chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idChat;

    @Column(name = "user_b_idChat",nullable = false)
    private int user_b_idChat;

    @Column(name="created_atChat",nullable = false)
    private LocalDate created_atChat;
    @ManyToOne
    @JoinColumn(name="idUser")
    private User user;
    @ManyToOne
    @JoinColumn(name="idTrade")
    private Trade trade;

    public Chat() {
    }

    public Chat(int idChat, int user_b_idChat, LocalDate created_atChat, User user, Trade trade) {
        this.idChat = idChat;
        this.user_b_idChat = user_b_idChat;
        this.created_atChat = created_atChat;
        this.user = user;
        this.trade = trade;
    }

    public int getIdChat() {
        return idChat;
    }

    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    public int getUser_b_idChat() {
        return user_b_idChat;
    }

    public void setUser_b_idChat(int user_b_idChat) {
        this.user_b_idChat = user_b_idChat;
    }

    public LocalDate getCreated_atChat() {
        return created_atChat;
    }

    public void setCreated_atChat(LocalDate created_atChat) {
        this.created_atChat = created_atChat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }
}

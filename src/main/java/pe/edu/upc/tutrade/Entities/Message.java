package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMessage;

    @Column(name = "contentMessage",nullable = false)
    private String contentMessage;

    @Column(name = "statusMessage",nullable = false)
    private String statusMessage;

    @Column(name="sent-atMessage",nullable = false)
    private LocalDate sent_atMessage;
    @ManyToOne
    @JoinColumn(name="idUser")
    private User user;
    @ManyToOne
    @JoinColumn(name="idChat")
    private Chat chat;


    public Message() {
    }

    public Message(int idMessage, Chat chat, String contentMessage, String statusMessage, LocalDate sent_atMessage, User user) {
        this.idMessage = idMessage;
        this.chat = chat;
        this.contentMessage = contentMessage;
        this.statusMessage = statusMessage;
        this.sent_atMessage = sent_atMessage;
        this.user = user;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public LocalDate getSent_atMessage() {
        return sent_atMessage;
    }

    public void setSent_atMessage(LocalDate sent_atMessage) {
        this.sent_atMessage = sent_atMessage;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}

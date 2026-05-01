package pe.edu.upc.tutrade.DTOs;

import java.time.LocalDateTime;

public class MessageResponseDTO {
    private int idMessage;
    private String content;
    private String status;
    private LocalDateTime sentAt;
    private UserResponseDTO sender;
    private int chatId;

    public int getIdMessage() { return idMessage; }
    public void setIdMessage(int idMessage) { this.idMessage = idMessage; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public UserResponseDTO getSender() { return sender; }
    public void setSender(UserResponseDTO sender) { this.sender = sender; }

    public int getChatId() { return chatId; }
    public void setChatId(int chatId) { this.chatId = chatId; }
}

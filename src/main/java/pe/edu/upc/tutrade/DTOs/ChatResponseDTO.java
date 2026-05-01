package pe.edu.upc.tutrade.DTOs;

import java.time.LocalDate;

public class ChatResponseDTO {
    private int idChat;
    private int tradeId;
    private int userAId;
    private int userBId;
    private LocalDate createdAt;

    public int getIdChat() { return idChat; }
    public void setIdChat(int idChat) { this.idChat = idChat; }

    public int getTradeId() { return tradeId; }
    public void setTradeId(int tradeId) { this.tradeId = tradeId; }

    public int getUserAId() { return userAId; }
    public void setUserAId(int userAId) { this.userAId = userAId; }

    public int getUserBId() { return userBId; }
    public void setUserBId(int userBId) { this.userBId = userBId; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}

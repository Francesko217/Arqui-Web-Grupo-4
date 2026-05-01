package pe.edu.upc.tutrade.DTOs;

public class MessageRequestDTO {
    private int chatId;
    private String content;

    public int getChatId() { return chatId; }
    public void setChatId(int chatId) { this.chatId = chatId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}

package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.ChatResponseDTO;
import pe.edu.upc.tutrade.DTOs.MessageRequestDTO;
import pe.edu.upc.tutrade.DTOs.MessageResponseDTO;

import java.util.List;

public interface IChatService {
    ChatResponseDTO getByTrade(int tradeId, String email);
    List<ChatResponseDTO> listAll();
    MessageResponseDTO sendMessage(MessageRequestDTO dto, String email);
    List<MessageResponseDTO> getMessages(int chatId, String email);
    MessageResponseDTO markAsRead(int messageId, String email);
}

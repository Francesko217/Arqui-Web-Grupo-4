package pe.edu.upc.tutrade.ServicesImplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.ChatResponseDTO;
import pe.edu.upc.tutrade.DTOs.MessageRequestDTO;
import pe.edu.upc.tutrade.DTOs.MessageResponseDTO;
import pe.edu.upc.tutrade.DTOs.UserResponseDTO;
import pe.edu.upc.tutrade.Entities.Chat;
import pe.edu.upc.tutrade.Entities.Message;
import pe.edu.upc.tutrade.Entities.Trade;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IChatRepository;
import pe.edu.upc.tutrade.Repositories.IMessageRepository;
import pe.edu.upc.tutrade.Repositories.ITradeRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IChatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImplement implements IChatService {

    @Autowired
    private IChatRepository chatRepo;

    @Autowired
    private IMessageRepository messageRepo;

    @Autowired
    private ITradeRepository tradeRepo;

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    private ChatResponseDTO toChatDTO(Chat chat) {
        ChatResponseDTO dto = new ChatResponseDTO();
        dto.setIdChat(chat.getIdChat());
        dto.setTradeId(chat.getTrade().getIdTrade());
        dto.setUserAId(chat.getUser().getIdUser());
        dto.setUserBId(chat.getUser_b_idChat());
        dto.setCreatedAt(chat.getCreated_atChat());
        return dto;
    }

    private MessageResponseDTO toMessageDTO(Message msg) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setIdMessage(msg.getIdMessage());
        dto.setContent(msg.getContentMessage());
        dto.setStatus(msg.getStatusMessage());
        dto.setSentAt(msg.getSent_atMessage());
        dto.setSender(modelMapper.map(msg.getUser(), UserResponseDTO.class));
        dto.setChatId(msg.getChat().getIdChat());
        return dto;
    }

    private void validateParticipant(Chat chat, User user) {
        boolean isUserA = chat.getUser().getIdUser() == user.getIdUser();
        boolean isUserB = chat.getUser_b_idChat() == user.getIdUser();
        if (!isUserA && !isUserB) {
            throw new RuntimeException("No eres parte de este chat");
        }
    }

    @Override
    public ChatResponseDTO getByTrade(int tradeId, String email) {
        Trade trade = tradeRepo.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("Trueque no encontrado"));
        User caller = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = caller.getRole() != null &&
                caller.getRole().getNameRole().equalsIgnoreCase("ADMIN");
        boolean isParticipant = trade.getProposer().getIdUser() == caller.getIdUser() ||
                trade.getReceiver().getIdUser() == caller.getIdUser();

        if (!isAdmin && !isParticipant) {
            throw new RuntimeException("No tienes permiso para ver este chat");
        }

        return chatRepo.findByTrade_IdTrade(tradeId)
                .map(this::toChatDTO)
                .orElseThrow(() -> new RuntimeException("Este trueque no tiene un chat asociado"));
    }

    @Override
    public List<ChatResponseDTO> listAll() {
        return chatRepo.findAll().stream().map(this::toChatDTO).collect(Collectors.toList());
    }

    @Override
    public MessageResponseDTO sendMessage(MessageRequestDTO dto, String email) {
        User sender = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Chat chat = chatRepo.findById(dto.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        validateParticipant(chat, sender);

        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new RuntimeException("El mensaje no puede estar vacío");
        }

        Message message = new Message();
        message.setChat(chat);
        message.setUser(sender);
        message.setContentMessage(dto.getContent());
        message.setStatusMessage("SENT");
        message.setSent_atMessage(LocalDateTime.now());

        return toMessageDTO(messageRepo.save(message));
    }

    @Override
    public List<MessageResponseDTO> getMessages(int chatId, String email) {
        Chat chat = chatRepo.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        User caller = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = caller.getRole() != null &&
                caller.getRole().getNameRole().equalsIgnoreCase("ADMIN");

        if (!isAdmin) {
            validateParticipant(chat, caller);
        }

        return messageRepo.findByChatIdOrderBySentAt(chatId)
                .stream().map(this::toMessageDTO).collect(Collectors.toList());
    }

    @Override
    public MessageResponseDTO markAsRead(int messageId, String email) {
        Message message = messageRepo.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        User caller = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Chat chat = message.getChat();
        validateParticipant(chat, caller);

        if (message.getUser().getIdUser() == caller.getIdUser()) {
            throw new RuntimeException("No puedes marcar como leído tu propio mensaje");
        }

        if (message.getStatusMessage().equals("READ")) {
            throw new RuntimeException("El mensaje ya está marcado como leído");
        }

        message.setStatusMessage("READ");
        return toMessageDTO(messageRepo.save(message));
    }
}

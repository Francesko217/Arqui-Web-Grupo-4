package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.tutrade.Entities.Message;

import java.util.List;

public interface IMessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE m.chat.idChat = :chatId ORDER BY m.sent_atMessage ASC")
    List<Message> findByChatIdOrderBySentAt(@Param("chatId") int chatId);
}

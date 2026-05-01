package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tutrade.Entities.Chat;

import java.util.Optional;

public interface IChatRepository extends JpaRepository<Chat, Integer> {
    Optional<Chat> findByTrade_IdTrade(int tradeId);
}

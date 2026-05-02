package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.tutrade.Entities.Trade;
import pe.edu.upc.tutrade.Entities.User;

import java.util.List;

public interface ITradeRepository extends JpaRepository<Trade, Integer> {
    List<Trade> findByProposer_IdUser(int proposerId);
    List<Trade> findByReceiver_IdUser(int receiverId);
    List<Trade> findByStatusTrade(String status);

    // HU39: trades enviados por el usuario (como proposer)
    List<Trade> findByProposer_IdUserAndStatusTrade(int proposerId, String status);

    // HU39: trades recibidos por el usuario (como receiver)
    List<Trade> findByReceiver_IdUserAndStatusTrade(int receiverId, String status);

    // HU21: trades donde un ítem específico participa (como proposer o receiver)
    @Query("SELECT DISTINCT t FROM Trade t JOIN Trade_item ti ON ti.trade = t WHERE ti.item.idItem = :itemId")
    List<Trade> findByItemId(@Param("itemId") int itemId);

    // HU16: usuarios ordenados por cantidad de trades (proposer + receiver)
    @Query("SELECT u, COUNT(t) as total FROM User u JOIN Trade t ON (t.proposer = u OR t.receiver = u) GROUP BY u ORDER BY total DESC")
    List<Object[]> findUsersRankedByTradeCount();
}

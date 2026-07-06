package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.tutrade.DTOs.ConteoDTO;
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

    // Premium: contar trades PENDING donde el usuario participa (como proposer o receiver)
    @Query("SELECT COUNT(t) FROM Trade t WHERE t.proposer.idUser = :userId AND t.statusTrade = 'PENDING'")
    long countPendingTradesByUser(@Param("userId") int userId);

    // Estadisticas: cantidad de trueques por estado
    @Query("SELECT new pe.edu.upc.tutrade.DTOs.ConteoDTO(t.statusTrade, COUNT(t)) FROM Trade t GROUP BY t.statusTrade")
    List<ConteoDTO> contarPorEstado();

    // Estadisticas: ranking de usuarios por cantidad de trueques
    @Query("SELECT new pe.edu.upc.tutrade.DTOs.ConteoDTO(u.usernameUser, COUNT(t)) FROM User u JOIN Trade t ON (t.proposer = u OR t.receiver = u) GROUP BY u.idUser, u.usernameUser ORDER BY COUNT(t) DESC")
    List<ConteoDTO> rankingUsuarios();
}

package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tutrade.Entities.Trade;

import java.util.List;

public interface ITradeRepository extends JpaRepository<Trade, Integer> {
    List<Trade> findByProposer_IdUser(int proposerId);
    List<Trade> findByReceiver_IdUser(int receiverId);
    List<Trade> findByStatusTrade(String status);
}

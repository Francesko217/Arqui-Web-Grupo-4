package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tutrade.Entities.Trade_item;

import java.util.List;

public interface ITradeItemRepository extends JpaRepository<Trade_item, Integer> {
    List<Trade_item> findByTrade_IdTrade(int tradeId);
    boolean existsByItem_IdItemAndTrade_StatusTrade(int itemId, String status);
}

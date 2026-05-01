package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tutrade.Entities.MeetingPoint;

import java.util.Optional;

public interface IMeetingPointRepository extends JpaRepository<MeetingPoint, Integer> {
    Optional<MeetingPoint> findByTrade_IdTrade(int tradeId);
    boolean existsByTrade_IdTrade(int tradeId);
}

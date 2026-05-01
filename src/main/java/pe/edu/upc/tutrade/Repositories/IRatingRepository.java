package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.tutrade.Entities.Rating;

import java.util.List;

public interface IRatingRepository extends JpaRepository<Rating, Integer> {

    @Query("SELECT r FROM Rating r WHERE r.rated_idRating = :ratedId")
    List<Rating> findByRatedUserId(@Param("ratedId") int ratedId);

    List<Rating> findByTrade_IdTrade(int tradeId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.user.idUser = :raterId AND r.trade.idTrade = :tradeId AND r.rated_idRating = :ratedId")
    long countByRaterAndTradeAndRated(@Param("raterId") int raterId, @Param("tradeId") int tradeId, @Param("ratedId") int ratedId);
}

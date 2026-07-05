package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.tutrade.Entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailUser(String emailUser);

    List<User> findByUsernameUserContainingIgnoreCase(String username);

    long countByRole_IdRole(int idRole);

    // HU20: usuarios con al menos un ítem disponible (status=1) O un trade PENDING/ACCEPTED
    @Query("SELECT DISTINCT u FROM User u WHERE " +
           "(SELECT COUNT(i) FROM Item i WHERE i.user = u AND i.statusItem = 1) > 0 " +
           "OR (SELECT COUNT(t) FROM Trade t WHERE (t.proposer = u OR t.receiver = u) AND t.statusTrade IN ('PENDING','ACCEPTED')) > 0")
    List<User> findActiveUsers();

    // HU28: candidatos a deshabilitación — sin ítems disponibles, sin trades en curso, último login hace más de X días
    @Query("SELECT u FROM User u WHERE u.is_enabledUser = true " +
           "AND (SELECT COUNT(i) FROM Item i WHERE i.user = u AND i.statusItem = 1) = 0 " +
           "AND (SELECT COUNT(t) FROM Trade t WHERE (t.proposer = u OR t.receiver = u) AND t.statusTrade IN ('PENDING','ACCEPTED')) = 0 " +
           "AND (u.last_loginUser IS NULL OR u.last_loginUser < :cutoff)")
    List<User> findInactiveUsers(@Param("cutoff") LocalDateTime cutoff);

    // HU50: usuarios con 3 o más trades ACCEPTED (veteranos)
    @Query("SELECT u FROM User u WHERE " +
           "(SELECT COUNT(t) FROM Trade t WHERE (t.proposer = u OR t.receiver = u) AND t.statusTrade = 'ACCEPTED') >= 3")
    List<User> findVeteranUsers();
}

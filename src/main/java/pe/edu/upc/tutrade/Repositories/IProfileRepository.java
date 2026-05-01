package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tutrade.Entities.Profile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByUser_IdUser(int userId);
    List<Profile> findAllByUser_IdUserIn(Collection<Integer> userIds);
}

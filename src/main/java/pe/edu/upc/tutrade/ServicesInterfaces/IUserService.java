package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User insert(User user);

    List<User> list();

    Optional<User> listId(int id);

    void delete(int id);
}

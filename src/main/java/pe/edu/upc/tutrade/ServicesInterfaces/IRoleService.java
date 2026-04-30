package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.Entities.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    Role insert(Role role);

    List<Role> list();

    Optional<Role> listId(int id);

    Role update(int id, Role role);

    void delete(int id);
}

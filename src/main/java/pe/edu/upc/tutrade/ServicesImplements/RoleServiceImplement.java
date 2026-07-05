package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.Entities.Role;
import pe.edu.upc.tutrade.Repositories.IRoleRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IRoleService;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImplement implements IRoleService {
    @Autowired
    private IRoleRepository rR;

    @Autowired
    private IUserRepository uR;

    private static void validateName(Role role) {
        if (role.getNameRole() == null || role.getNameRole().isBlank()) {
            throw new RuntimeException("El nombre del rol es obligatorio");
        }
    }

    @Override
    public Role insert(Role role) {
        validateName(role);
        Optional<Role> existente = rR.findByNameRole(role.getNameRole());
        if (existente.isPresent()) {
            throw new RuntimeException("Rol ya registrado");
        }
        return rR.save(role);
    }

    @Override
    public List<Role> list() {
        return rR.findAll();
    }

    @Override
    public Optional<Role> listId(int id) {
        return rR.findById(id);
    }

    @Override
    public Role update(int id, Role role) {
        Role existente = rR.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        validateName(role);
        existente.setNameRole(role.getNameRole());
        existente.setDescriptionRole(role.getDescriptionRole());
        return rR.save(existente);
    }

    @Override
    public void delete(int id) {
        if (!rR.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        if (uR.countByRole_IdRole(id) > 0) {
            throw new RuntimeException("No se puede eliminar: hay usuarios asignados a este rol");
        }
        rR.deleteById(id);
    }
}

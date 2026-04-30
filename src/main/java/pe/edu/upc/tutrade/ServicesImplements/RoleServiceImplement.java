package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.Entities.Role;
import pe.edu.upc.tutrade.Repositories.IRoleRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IRoleService;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImplement implements IRoleService {
    @Autowired
    private IRoleRepository rR;

    @Override
    public Role insert(Role role) {
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
        existente.setNameRole(role.getNameRole());
        existente.setDescriptionRole(role.getDescriptionRole());
        return rR.save(existente);
    }

    @Override
    public void delete(int id) {
        rR.deleteById(id);
    }
}

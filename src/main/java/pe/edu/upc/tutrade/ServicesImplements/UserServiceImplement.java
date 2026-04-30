package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IUserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplement implements IUserService {
    @Autowired
    private IUserRepository uR;

    @Override
    public User insert(User user) {
        Optional<User> existente = uR.findByEmailUser(user.getEmailUser());
        if (existente.isPresent()) {
            throw new RuntimeException("Email ya registrado");
        }
        user.setIs_premiumUser(false);
        user.setIs_verifiedUser(false);
        user.setCreated_atUser(LocalDate.now());
        user.setUpdated_atUser(LocalDate.now());
        return uR.save(user);
    }

    @Override
    public List<User> list() {
        return uR.findAll();
    }

    @Override
    public Optional<User> listId(int id) {
        return uR.findById(id);
    }

    @Override
    public void delete(int id) {
        uR.deleteById(id);

    }
}

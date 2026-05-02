package pe.edu.upc.tutrade.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IUserRepository;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailUser(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        if (Boolean.FALSE.equals(user.getIs_enabledUser())) {
            throw new DisabledException("Cuenta deshabilitada");
        }

        String role = user.getRole() != null
                ? "ROLE_" + user.getRole().getNameRole().toUpperCase()
                : "ROLE_USER";

        return new org.springframework.security.core.userdetails.User(
                user.getEmailUser(),
                user.getPassword_hashUser(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}

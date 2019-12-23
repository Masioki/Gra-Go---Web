package GoOnline.services;

import GoOnline.domain.Player;
import GoOnline.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import GoOnline.dto.LoginData;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Player p = new Player();
        return p;//return userRepository.getUser(s);
    }


    public boolean registerUser(LoginData loginData) {
        return false;
    }
}

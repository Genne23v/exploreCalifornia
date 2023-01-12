package com.example.ec.security;

import com.example.ec.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.example.ec.domain.User;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Component
public class ExplorecaliUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(String.format("User with name %s does not exist", s)));

        return withUsername(user.getUsername()).password(user.getPassword()).authorities(user.getRoles())
                .accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(false).build();
    }

    public Optional<UserDetails> loadUserByJwtToken(String token) {
        if (jwtProvider.isValidToken(token)) {
            return Optional.of(withUsername(jwtProvider.getUsername(token)).authorities(jwtProvider.getRoles(token)).password("").accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(false).build());
        }
        return Optional.empty();
    }

    public Optional<UserDetails> loadUserByJwtTokenAndDatabase(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(loadUserByUsername(jwtProvider.getUsername(jwtToken)));
        } else {
            return Optional.empty();
        }
    }
}

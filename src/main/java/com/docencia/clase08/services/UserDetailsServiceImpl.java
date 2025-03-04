package com.docencia.clase08.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docencia.clase08.models.Usuario;
import com.docencia.clase08.repositories.UsuarioRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        Set<GrantedAuthority> grantList = new HashSet<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(appUser.getRole());
        grantList.add(grantedAuthority);

        UserDetails user = new User(username, appUser.getPassword(), grantList);

        return user;
    }
}

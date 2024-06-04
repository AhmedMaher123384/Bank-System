package com.App.BankingSystem.Security;


import com.App.BankingSystem.model.entity.Role;
import com.App.BankingSystem.model.entity.Users;
import com.App.BankingSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CustomeUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = this.userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("username not found"));
        return  new User(user.getUsername(),user.getPassword(),this.mapRolesToAuthorities(user.getRoles()));
    }


    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles){
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

}

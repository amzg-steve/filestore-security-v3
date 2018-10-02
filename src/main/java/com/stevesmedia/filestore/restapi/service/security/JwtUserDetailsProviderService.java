package com.stevesmedia.filestore.restapi.service.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stevesmedia.filestore.restapi.domainmodel.JwtUser;
import com.stevesmedia.filestore.restapi.domainmodel.security.User;
import com.stevesmedia.filestore.restapi.repository.UserRepository;

@Service
public class JwtUserDetailsProviderService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return new JwtUser(user.getId(),
								user.getUsername(),
								user.getFirstname(),
								user.getLastname(),
								user.getEmail(),
								user.getPassword(),
								mapToGrantedAuthorities(user),
								user.getEnabled(),
								user.getLastPasswordResetDate());
		}
	}

	private Set<SimpleGrantedAuthority> mapToGrantedAuthorities(User user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getAuthorities().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
		});
		return authorities;
	}

}

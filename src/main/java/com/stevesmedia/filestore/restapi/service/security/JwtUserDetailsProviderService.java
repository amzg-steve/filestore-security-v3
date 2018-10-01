package com.stevesmedia.filestore.restapi.service.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stevesmedia.filestore.restapi.domainmodel.JwtUser;
import com.stevesmedia.filestore.restapi.domainmodel.security.Authority;
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
								mapToGrantedAuthorities(user.getAuthorities()),
								user.getEnabled(),
								user.getLastPasswordResetDate());
		}
	}

	private List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
		return authorities
				.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
				.collect(Collectors.toList());
	}

}

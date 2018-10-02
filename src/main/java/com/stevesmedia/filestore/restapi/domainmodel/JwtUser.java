package com.stevesmedia.filestore.restapi.domainmodel;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class JwtUser implements UserDetails {

	private static final long serialVersionUID = 312482437213000780L;

	@JsonIgnore
    private  Long id;
    private  String username;
    private  String firstname;
    private  String lastname;
    private  String password;
    private  String email;
    private  Collection<? extends GrantedAuthority> authorities;
    private  boolean enabled;
    private  Date lastPasswordResetDate;

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public JwtUser() {
		
	}
	
	public JwtUser(Long id, 
					String username, 
					String firstname, 
					String lastname, 
					String email, 
					String password,
					Collection<? extends GrantedAuthority> authorities,
					Boolean enabled, 
					Date lastPasswordResetDate) {
		
		this.id = id;
		this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
	}

}

package com.stevesmedia.filestore.restapi.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stevesmedia.filestore.restapi.domainmodel.security.User;

public interface UserRepository extends Serializable, JpaRepository<User, Long> {
	
    User findByUsername(String username);

}

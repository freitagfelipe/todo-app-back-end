package com.freitagfelipe.todoapp.repositories.user;

import com.freitagfelipe.todoapp.domain.user.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {

    UserDetails findByUsername(String username);

}

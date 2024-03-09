package com.example.login.Repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.login.Models.UserEntity;

@Repository
public interface UserRepository {
	
	Optional<UserEntity> findByUsername(String username);
	
	Boolean existsByUsername(String username);
}

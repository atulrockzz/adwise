package com.spiralforge.adwise.repository;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.util.AddWiseEnum.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByMobileNumberAndPassword(Long mobileNumber, String password);

	Optional<User> findByUserIdAndRole(@Valid Long userId, Role role);

}

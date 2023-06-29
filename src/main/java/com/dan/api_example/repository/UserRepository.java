package com.dan.api_example.repository;

import com.dan.api_example.common.entity.BaseEntity;
import com.dan.api_example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByIdAndState(Long id, BaseEntity.State state);
    Optional<User> findByEmail(String email);
}

package com.spf.control.feature.user.domain.repository;

import com.spf.control.feature.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByUserName(String userName);
        Optional<User> findByEmail(String email);
        List<User> findAllByEmailIn(List<String> emails);
        Optional<User> findByUserNameOrEmail(String userName, String email);
}

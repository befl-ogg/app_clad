package com.spf.control.feature.token.domain.repository;

import com.spf.control.feature.token.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "select t from Token t inner join User u on t.user.id = u.id where u.id = :id and t.expiredAccessToken = false")
    List<Token> findAllValidTokenByUser(@Param("id") Long id);

    Optional<Token> findByToken(String token);

    Optional<Token> findByRefreshTokenAndExpiredRefreshToken(String refreshToken, Boolean expiredRefreshToken);

    @Query(value = "SELECT t FROM Token t INNER JOIN t.user u WHERE u.userName = :userName AND t.expiredAccessToken = false")
    Optional<Token> findExpiredTokenByUserName(String userName);
}

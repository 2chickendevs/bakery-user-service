package com.twochickendevs.bakeryuserservice.auth.repository;

import com.twochickendevs.bakeryuserservice.auth.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM TokenEntity t " + "WHERE t.user.id = :userId")
    void removeAllTokenByUserId(@Param("userId") Long userId);

    Optional<TokenEntity> findByToken(String token);
}

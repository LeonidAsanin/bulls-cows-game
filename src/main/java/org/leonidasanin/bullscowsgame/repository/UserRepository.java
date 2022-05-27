package org.leonidasanin.bullscowsgame.repository;

import org.leonidasanin.bullscowsgame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsernameIgnoreCase(String username);

    @Query("SELECT gameCount FROM User WHERE id = ?1")
    int getGameCountByUserId(long id);

    @Query("SELECT averageAttemptNumberToWin FROM User WHERE id = ?1")
    double getAverageAttemptNumberToWinByUserId(long id);

    @Query("UPDATE User SET gameCount = gameCount + 1 WHERE id = ?1")
    @Modifying
    @Transactional
    void incrementGameCountByUserId(long id);

    @Query("UPDATE User SET averageAttemptNumberToWin = ?2 WHERE id = ?1")
    @Modifying
    @Transactional
    void setAverageAttemptNumberToWinByUserId(long id, double averageAttemptNumberToWin);
}
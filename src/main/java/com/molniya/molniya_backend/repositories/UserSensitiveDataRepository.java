package com.molniya.molniya_backend.repositories;

import com.molniya.molniya_backend.models.UserSensitiveData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSensitiveDataRepository extends JpaRepository<UserSensitiveData, Long> {
    Optional<UserSensitiveData> findByUserId(Long userId);
}

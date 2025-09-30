package com.molniya.molniya_backend.repositories;

import com.molniya.molniya_backend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Находит пользователя по номеру телефона
     *
     * @param phone Номер телефона пользователя
     * @return Optional с объектом пользователя
     */
    Optional<User> findByPhone(String phone);

    /**
     * Находит пользователя по телефону и получает его пароль за один запрос с использованием проекции.
     *
     * @param phone Телефон пользователя
     * @return Проекция, содержащая пользователя и его пароль
     */
    @Query(value = """
            SELECT u as user, sd.password as password
            FROM User u
            JOIN UserSensitiveData sd ON u.id = sd.user.id
            WHERE u.phone = :phone
            """)
    Optional<com.molniya.molniya_backend.dtos.user.UserWithPasswordDto> findByPhoneWithPassword(@Param("phone") String phone);
}

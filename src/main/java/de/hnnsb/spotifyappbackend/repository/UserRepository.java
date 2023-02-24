package de.hnnsb.spotifyappbackend.repository;

import de.hnnsb.spotifyappbackend.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    @Query("SELECT user FROM UserEntity user WHERE user.name = (:name) AND user.password = (:password)")
    UserEntity findByNameAndPassword(@Param("name") String name, @Param("password") String password);
}

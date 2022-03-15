/*
 * package com.scube.chargingstation.repository;
 * 
 * import java.util.Optional;
 * 
 * import org.springframework.data.jpa.repository.JpaRepository; import
 * org.springframework.stereotype.Repository;
 * 
 * import com.scube.chargingstation.dto.UserDto; import
 * com.scube.chargingstation.model.UserEntity;
 * 
 * @Repository public interface UserRepository extends JpaRepository<UserEntity,
 * String> {
 * 
 * Optional<UserEntity> findById(String userId); // Optional<UserEntity>
 * findById(String id);
 * 
 * boolean existsById(String id);
 * 
 * void deleteById(String id);
 * 
 * UserEntity save(UserDto user);
 * 
 * UserEntity findByEmail(String usernm);
 * 
 * }
 */
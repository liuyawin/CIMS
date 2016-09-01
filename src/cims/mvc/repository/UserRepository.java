package cims.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cims.mvc.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User u where id = :id")
	public User findById(@Param("id") Long id);

	@Query("select u from User u ")
	public List<User> findAlls();

	@Query("select count(id) from User u ")
	public Long countId();

}

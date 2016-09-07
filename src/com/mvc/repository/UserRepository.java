package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.User;

/**
 * 
 * @author zjn
 * @date 2016年9月7日
 */
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User u where id = :id")
	public User findById(@Param("id") Long id);

	@Query("select u from User u ")
	public List<User> findAlls();

	@Query("select count(id) from User u ")
	public Long countId();

}

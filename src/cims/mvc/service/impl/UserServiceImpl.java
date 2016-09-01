package cims.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cims.mvc.entity.User;
import cims.mvc.repository.UserRepository;
import cims.mvc.service.UserService;

/**
 * User相关Service层接口实现
 * 
 * @author zjn
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;

	public User save(User user) {
		return userRepository.saveAndFlush(user);
	}

}

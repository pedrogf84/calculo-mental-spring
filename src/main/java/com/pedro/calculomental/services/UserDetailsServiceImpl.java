package com.pedro.calculomental.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pedro.calculomental.dao.MongoConnectorDao;
import com.pedro.calculomental.dtos.UserDto;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	/** The logger. */
	private static Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	/** CRUD for accessing to user info */
	@Autowired
	private MongoConnectorDao<UserDto> userDao;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
		
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.info("UserDetailsServiceImpl.loadUserByUsername.in");
		
		List<UserDto> users = this.userDao.read("id", username);
		
		if (users.isEmpty() || users.size() != 1)
			throw new UsernameNotFoundException(String.format("user \"%s\" not found", username));
		
		//Using user role for logging granted permissions.
		List<GrantedAuthority> grantedAuthorityList = 
				List.of(new SimpleGrantedAuthority(users.get(0).getRole().toString()));
	
		UserDetails userDetail = 
				(UserDetails) new User(users.get(0).getId(), this.passwordEncoder.encode(users.get(0).getPassword()), grantedAuthorityList);

		LOGGER.info("UserDetailsServiceImpl.loadUserByUsername.out");
		return userDetail;
	}

}

package com.pedro.calculomental;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.mongodb.MongoException;
import com.pedro.calculomental.dao.MongoConnectorDao;
import com.pedro.calculomental.dtos.UserDto;
import com.pedro.calculomental.dtos.UserDto.UserRoles;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CalculomentalApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
class CalculomentalApplicationTests {

	private static Logger LOGGER = LoggerFactory.getLogger(CalculomentalApplicationTests.class);
	

	private UserDto testUserDto;
	
	@Autowired
	private MongoConnectorDao<UserDto> userDao;
	
	@BeforeEach
	public void setup() {
		this.testUserDto = new UserDto();
		this.testUserDto.setId("test@ggmail.com");
		this.testUserDto.setPassword("1234");
		this.testUserDto.setRole(UserRoles.ADMIN);
		this.testUserDto.setCompletedActivityIds(new ArrayList<>(List.of("Actividad 1", "Actividad 2")));
	}
	
	
	@Test
	void userDaoTest() throws MongoException {
		LOGGER.info("userDaoTest.in");
		
		this.userDao.create(this.testUserDto);
		
		List<UserDto> result = this.userDao.read("id", "test@ggmail.com");
		assertFalse("No hay entradas", result.isEmpty());
		assertFalse("Multiples documentos", result.size()>1);
		
		UserDto entry = result.get(0);
		entry.setPassword("new_password");
		assertTrue ("Numero de documentos modificados incorrecto", this.userDao.update(entry)==1);
		
		result = this.userDao.read("id", "test@ggmail.com");
		assertFalse("No hay entradas", result.isEmpty());
		assertTrue("Multiples documentos", result.size()==1);
		assertTrue("Password incorrecto", "new_password".equals(result.get(0).getPassword()));
		
		assertTrue("Debe existir al menos un usuario", !this.userDao.read().isEmpty());
		
		
		assertTrue("Numero de documentos borrados erroneo", this.userDao.delete(result.get(0)) == 1L);
		
		
		LOGGER.info("userDaoTest.out");
	}
	


}

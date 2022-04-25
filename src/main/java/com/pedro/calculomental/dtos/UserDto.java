package com.pedro.calculomental.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class User.
 *
 */
public class UserDto extends BaseDto{

	/**
	 * The Enum UserRoles. To be inplemented un v2.
	 */
	public static enum UserRoles {
		ALUMNO, TUTOR, ADMIN;
	}
	
	
	/** The Constant serialVersionUID. */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	/** The password. */
	private String password;
	
	/** The role. To be inplemented un v2.*/
	private UserRoles role;
	
	/** The completed activity ids. Displays activities completed by user:role Alumno*/
	private List<String> completedActivityIds = new ArrayList<>();

	/**
	 * GETTERS AND SETTERS
	 */

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the completedActivityIds
	 */
	public List<String> getCompletedActivityIds() {
		return completedActivityIds;
	}

	/**
	 * @param completedActivityIds the completedActivityIds to set
	 */
	public void setCompletedActivityIds(List<String> completedActivityIds) {
		this.completedActivityIds = completedActivityIds;
	}

	/**
	 * @return the role
	 */
	public UserRoles getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(UserRoles role) {
		this.role = role;
	}

}

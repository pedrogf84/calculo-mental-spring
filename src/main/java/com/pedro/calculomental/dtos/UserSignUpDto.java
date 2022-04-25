package com.pedro.calculomental.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The Class UserSignUpDto extends UserDto adding input password confirmation.
 */
public class UserSignUpDto extends UserDto {

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	/**
	 * Password Confirmed property.
	 */
	@JsonIgnore
	private String passwordConfirmed;

	/**
	 * @return the passwordConfirmed
	 */
	public String getPasswordConfirmed() {
		return passwordConfirmed;
	}

	/**
	 * @param passwordConfirmed the passwordConfirmed to set
	 */
	public void setPasswordConfirmed(String passwordConfirmed) {
		this.passwordConfirmed = passwordConfirmed;
	}

}
